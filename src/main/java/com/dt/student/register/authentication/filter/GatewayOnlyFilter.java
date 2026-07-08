package com.dt.student.register.authentication.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class GatewayOnlyFilter extends OncePerRequestFilter {

    @Value("${gateway.access-control.enabled:true}")
    private boolean enabled;

    @Value("${gateway.internal.header:X-Gateway-Secret}")
    private String gatewayHeader;

    @Value("${gateway.internal.secret:}")
    private String gatewaySecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!enabled || isPublicPath(request) || isGatewayRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Forbidden\",\"message\":\"Access this service through the gateway.\"}");
    }

    private boolean isPublicPath(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String path = request.getServletPath();
        return path.startsWith("/actuator")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/api-docs")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/api/auth/");
    }

    private boolean isGatewayRequest(HttpServletRequest request) {
        if (gatewaySecret == null || gatewaySecret.isBlank()) {
            return false;
        }

        String value = request.getHeader(gatewayHeader);
        if (value == null || value.isBlank()) {
            return false;
        }

        return MessageDigest.isEqual(
                value.getBytes(StandardCharsets.UTF_8),
                gatewaySecret.getBytes(StandardCharsets.UTF_8)
        );
    }
}
