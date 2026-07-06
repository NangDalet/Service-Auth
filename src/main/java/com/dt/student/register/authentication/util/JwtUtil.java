package com.dt.student.register.authentication.util;

import com.dt.student.register.model.dto.response.authentication.token.AccessTokenResponse;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private final PrivateKey privateKey;
    private final JwtParser jwtParser;
    private final String keyId;

    private static final String TOKEN_TYPE = "Bearer";
    private static final String SCOPE = "read write trust";
    private static final String TOKEN_TYPE_ACCESS = "JWT";
    private static final String TOKEN_TYPE_REFRESH = "RefreshToken";

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    @Value("${jwt.access.token.expiration.ms}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh.token.expiration.ms}")
    private long refreshTokenExpirationMs;

    public JwtUtil(@Value("${jwt.private.key.path}") String privateKeyPath,
                   @Value("${jwt.public.key.path}") String publicKeyPath) throws SecurityException {
        this.privateKey = (PrivateKey) loadKey(privateKeyPath, true);
        PublicKey publicKey = (PublicKey) loadKey(publicKeyPath, false);
        this.jwtParser = Jwts.parser().verifyWith(publicKey).build();
        this.keyId = generateKeyId(publicKey);
    }

    private Key loadKey(String filePath, boolean isPrivate) throws SecurityException {
        try {
            byte[] keyBytes;

            if (filePath.startsWith("classpath:")) {
                String classpathLocation = filePath.replace("classpath:", "").trim();
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(classpathLocation);

                if (inputStream == null) {
                    throw new SecurityException("Resource not found in classpath: " + classpathLocation);
                }
                keyBytes = inputStream.readAllBytes();
                logger.info("✅ Loaded key from classpath: " + classpathLocation);
            } else {
                logger.info("🔍 Reading key from absolute path: " + filePath);
                keyBytes = Files.readAllBytes(Paths.get(filePath));
            }

            String keyContent = new String(keyBytes)
                    .replaceAll("-----[A-Z ]+-----", "")
                    .replaceAll("\\s+", "");
            byte[] decodedKey = Base64.getDecoder().decode(keyContent);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return isPrivate
                    ? keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedKey))
                    : keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
        } catch (Exception e) {
            logger.error("❌ Failed to load RSA key from path: {}", filePath, e);
            throw new SecurityException("Failed to load RSA key.", e);
        }
    }

    public List<AccessTokenResponse> generateToken(
            Long userId,
            String username,
            List<String> roles,
            List<String> permissions
    ) {
        Map<String, Object> claims = createClaims(userId, username, roles, permissions);

        String accessToken = createToken(claims, accessTokenExpirationMs, TokenType.ACCESS);
        String refreshToken = createToken(claims, refreshTokenExpirationMs, TokenType.REFRESH);

        return List.of(
                new AccessTokenResponse(
                        TOKEN_TYPE,
                        accessToken,
                        refreshToken,
                        accessTokenExpirationMs,
                        SCOPE
                )
        );
    }




    public String refreshAccessToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken);

        Long userId = claims.get("id", Long.class);
        String username = claims.getSubject();

        // 🔥 roles & permissions will be reloaded from DB
        Map<String, Object> refreshedClaims = new HashMap<>();
        refreshedClaims.put("id", userId);
        refreshedClaims.put("sub", username);
        refreshedClaims.put("aud", audience);
        refreshedClaims.put("scope", SCOPE);

        Object roles = claims.get("roles");
        if (roles != null) {
            refreshedClaims.put("roles", roles);
        }

        Object permissions = claims.get("permissions");
        if (permissions != null) {
            refreshedClaims.put("permissions", permissions);
        }

        return createToken(refreshedClaims, accessTokenExpirationMs, TokenType.ACCESS);
    }

    private Map<String, Object> createClaims(
            Long userId,
            String username,
            List<String> roles,
            List<String> permissions
    ) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("id", userId);
        claims.put("roles", roles);
        claims.put("permissions", permissions);
        claims.put("jti", UUID.randomUUID().toString());
        claims.put("iss", issuer);
        claims.put("aud", audience);
        claims.put("scope", SCOPE);
        return claims;
    }




    private String createToken(Map<String, Object> claims, long duration, TokenType tokenType) {
        Date now = new Date();
        return Jwts.builder()
                .claims(claims)
                .claim("token_type", tokenType.toString())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + duration))
                .issuer(issuer)
                .header().add("kid", keyId).and()
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    private String generateKeyId(Key key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyHash = digest.digest(key.getEncoded());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(keyHash).substring(0, 16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm is not available.", e);
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = parseClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims parseClaims(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException e) {
            logger.warn("❌ Invalid JWT: {}", e.getMessage());
            return false;
        }
    }

    public void setSecureCookies(HttpServletRequest request, HttpServletResponse response, String accessToken, String refreshToken) {
        try {
            String encryptedRefreshToken = AESUtil.encrypt(refreshToken);

            CookieUtils.addOrUpdateCookie(request, response, "accessToken", accessToken, (int) (accessTokenExpirationMs / 1000), true, "Strict");
            CookieUtils.addOrUpdateCookie(request, response, "refreshToken", encryptedRefreshToken, (int) (refreshTokenExpirationMs / 1000), true, "Strict");
        } catch (Exception e) {
            logger.error("❌ Error encrypting refresh token", e);
        }
    }

    public void deleteCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, "accessToken");
        CookieUtils.deleteCookie(request, response, "refreshToken");
        logger.info("✅ Cookies deleted successfully.");
    }


    public enum TokenType {
        ACCESS(TOKEN_TYPE_ACCESS),
        REFRESH(TOKEN_TYPE_REFRESH);

        private final String type;

        TokenType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }
}
