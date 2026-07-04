package com.dt.student.register.config;

import com.dt.student.register.model.users.CustomUserDetails;
import com.dt.student.register.service.AuthorizationUserClaimsService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Configuration
public class AuthorizationServerConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public org.springframework.security.web.SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http
    ) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http
                .securityMatcher("/oauth2/**", "/.well-known/**")
                .csrf(csrf -> csrf.ignoringRequestMatchers("/oauth2/**", "/.well-known/**"))
                .cors(Customizer.withDefaults())
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                (request, response, authException) -> {
                                    response.setStatus(401);
                                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                    response.getWriter().write("{\"error\":\"unauthorized\"}");
                                },
                                request -> request.getRequestURI().contains("/oauth2/")
                        )
                );

        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(
            @Value("${oauth2.client-id:service-auth-client}") String clientId,
            @Value("${oauth2.client-secret:{noop}service-auth-secret}") String clientSecret,
            @Value("${oauth2.client-scopes:read,write}") List<String> scopes
    ) {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(normalizeClientSecret(clientSecret))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .scope("openid")
                .scopes(existingScopes -> {
                    existingScopes.addAll(scopes);
                })
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(RegisteredClientRepository registeredClientRepository) {
        return new InMemoryOAuth2AuthorizationService();
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(RegisteredClientRepository registeredClientRepository) {
        return new InMemoryOAuth2AuthorizationConsentService();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(
            @Value("${oauth2.issuer-uri:${api.authUrl}}") String issuer
    ) {
        return AuthorizationServerSettings.builder()
                .issuer(issuer)
                .build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(
            @Value("${jwt.private.key.path}") Resource privateKeyResource,
            @Value("${jwt.public.key.path}") Resource publicKeyResource
    ) {
        RSAPublicKey publicKey = readPublicKey(publicKeyResource);
        RSAPrivateKey privateKey = readPrivateKey(privateKeyResource);

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(generateKeyId(publicKey))
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(
            AuthorizationUserClaimsService authorizationUserClaimsService
    ) {
        return context -> {
            if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                return;
            }

            context.getClaims().claim("client_id", context.getRegisteredClient().getClientId());
            context.getClaims().claim("scope", context.getAuthorizedScopes());

            Authentication principal = context.getPrincipal();
            if (principal == null || principal.getPrincipal() == null) {
                return;
            }

            Object principalObject = principal.getPrincipal();
            if (principalObject instanceof CustomUserDetails userDetails) {
                context.getClaims().claim("id", userDetails.getId());
                context.getClaims().claim("username", userDetails.getUsername());
                context.getClaims().claim("roles", authorizationUserClaimsService.getRoleNames(userDetails.getId()));
                context.getClaims().claim("permissions", authorizationUserClaimsService.getPermissionCodes(userDetails.getId()));
            }
        };
    }

    private String normalizeClientSecret(String clientSecret) {
        if (clientSecret.startsWith("{")) {
            return clientSecret;
        }
        return "{noop}" + clientSecret;
    }

    private RSAPrivateKey readPrivateKey(Resource resource) {
        try {
            byte[] keyBytes = resource.getInputStream().readAllBytes();
            String keyContent = new String(keyBytes)
                    .replaceAll("-----[A-Z ]+-----", "")
                    .replaceAll("\\s+", "");
            byte[] decodedKey = Base64.getDecoder().decode(keyContent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to load private key for authorization server.", ex);
        }
    }

    private RSAPublicKey readPublicKey(Resource resource) {
        try {
            byte[] keyBytes = resource.getInputStream().readAllBytes();
            String keyContent = new String(keyBytes)
                    .replaceAll("-----[A-Z ]+-----", "")
                    .replaceAll("\\s+", "");
            byte[] decodedKey = Base64.getDecoder().decode(keyContent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to load public key for authorization server.", ex);
        }
    }

    private String generateKeyId(RSAPublicKey publicKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyHash = digest.digest(publicKey.getEncoded());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(keyHash).substring(0, 16);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to generate key id.", ex);
        }
    }
}
