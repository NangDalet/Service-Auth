package com.dt.student.register.authentication.filter;


import com.dt.student.register.authentication.util.JwtUtil;
import com.dt.student.register.model.users.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtRequestFilter(@Lazy UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7); // "Bearer " is 7 characters
            try {
                processTokenAuthentication(jwt, request);
            } catch (ExpiredJwtException e) {
                handleException(response, "Expired token.");
                return;
            } catch (JwtException e) {
                handleException(response, "Invalid token.");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private void processTokenAuthentication(String jwt, HttpServletRequest request) {
        Claims claims = jwtUtil.parseClaims(jwt);

        if ("RefreshToken".equals(claims.get("token_type", String.class))) {
            throw new JwtException("Refresh tokens cannot be used as bearer access tokens.");
        }

        String subject = claims.getSubject();
        if (subject == null || subject.isBlank()) {
            throw new JwtException("Token subject is missing.");
        }

        try {
            setupUserSecurityContext(subject, request);
        } catch (RuntimeException ex) {
            if (!isOAuth2ClientToken(claims)) {
                throw new JwtException("Token subject is not a valid application user.", ex);
            }
            setupOAuth2ClientSecurityContext(subject, claims, request);
        }
    }

    private void setupUserSecurityContext(String username, HttpServletRequest request) {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void setupOAuth2ClientSecurityContext(String subject, Claims claims, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                subject,
                null,
                extractScopeAuthorities(claims)
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean isOAuth2ClientToken(Claims claims) {
        Object clientId = claims.get("client_id");
        return clientId instanceof String value && !value.isBlank();
    }

    private Collection<GrantedAuthority> extractScopeAuthorities(Claims claims) {
        Collection<GrantedAuthority> authorities = new LinkedHashSet<>();
        addScopeAuthorities(authorities, claims.get("scope"));
        addScopeAuthorities(authorities, claims.get("scp"));
        return authorities;
    }

    private void addScopeAuthorities(Collection<GrantedAuthority> authorities, Object claimValue) {
        if (claimValue instanceof String scopes) {
            for (String scope : scopes.split("\\s+")) {
                addScopeAuthority(authorities, scope);
            }
            return;
        }

        if (claimValue instanceof Collection<?> scopes) {
            scopes.forEach(scope -> addScopeAuthority(authorities, String.valueOf(scope)));
        }
    }

    private void addScopeAuthority(Collection<GrantedAuthority> authorities, String scope) {
        if (scope == null || scope.isBlank()) {
            return;
        }
        authorities.add(new SimpleGrantedAuthority("SCOPE_" + scope.trim()));
    }

    private void handleException(HttpServletResponse response, String message) throws IOException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + message + "\"}");
    }
}
