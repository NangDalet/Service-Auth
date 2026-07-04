package com.dt.student.register.authentication.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class CustomCorsFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CustomCorsFilter.class);

    // Configurable allowed origins set via application properties
    @Value("${cors.allowedOrigins}")
    private String[] allowedOriginsConfig;
    private Set<String> allowedOrigins;

    @Override
    public void initFilterBean() {
        allowedOrigins = new HashSet<>(Arrays.asList(allowedOriginsConfig));
        logger.info("Allowed CORS origins: {}", allowedOrigins);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (allowedOrigins == null) {
            allowedOrigins = new HashSet<>(Arrays.asList(allowedOriginsConfig));
            logger.info("Allowed CORS origins initialized: {}", allowedOrigins);
        }

        String origin = request.getHeader("Origin");
        setCorsHeaders(response, origin);
        setSecurityHeaders(response);

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void setCorsHeaders(HttpServletResponse response, String origin) {
        if (allowedOrigins.contains(origin) || allowedOrigins.contains("*")) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, X-Requested-With, Authorization, X-XSRF-TOKEN");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Expose-Headers", "Authorization, Content-Type");
            response.setHeader("Access-Control-Max-Age", "3600"); // Cache preflight response for 1 hour
        } else {
            logger.warn("CORS request from unauthorized origin: {}", origin);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private void setSecurityHeaders(HttpServletResponse response) {
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'self'; object-src 'none';");
    }
}
