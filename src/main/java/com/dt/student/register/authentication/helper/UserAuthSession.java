package com.dt.student.register.authentication.helper;

import com.dt.student.register.model.users.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to manage user authentication sessions securely and effectively.
 */
public final class UserAuthSession {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthSession.class);

    // Prevent instantiation
    private UserAuthSession() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Retrieves the current Authentication object from the security context, enhancing logging for audit.
     *
     * @return the current Authentication object, or null if no authentication is present
     */
    public static Authentication getUserAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            logger.warn("No authentication found in security context.");
        } else {
            logger.info("Authentication retrieved for user: {}", auth.getName()); // Avoid using `auth.toString()` if it can contain sensitive details
        }
        return auth;
    }

    /**
     * Checks if the current user is authenticated and not marked as anonymous.
     *
     * @return true if the user is authenticated and not anonymous, false otherwise
     */
    public static boolean isAuthenticated() {
        Authentication authentication = getUserAuth();
        if (authentication != null && authentication.isAuthenticated()) {
            boolean isAnonymous = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> "ROLE_ANONYMOUS".equals(grantedAuthority.getAuthority()));
            logger.info("Authentication check performed, is anonymous: {}", isAnonymous);
            return !isAnonymous;
        }
        logger.warn("User is not authenticated.");
        return false;
    }

    /**
     * Clears the authentication from the security context and invalidates the HTTP session if present.
     *
     * @param request The current HTTP request to retrieve the session from.
     */
    public static void clearAuthentication(HttpServletRequest request) {
        if (request != null) {
            try {
                logger.info("Attempting to clear security context for [{}]", request.getRemoteAddr());
                SecurityContextHolder.clearContext();
                invalidateSession(request);
            } catch (Exception e) {
                logger.error("Error while clearing security context for [{}]: {}", request.getRemoteAddr(), e.getMessage(), e);
            }
        } else {
            logger.error("Request object is null; cannot perform operation to clear authentication or invalidate session.");
        }
    }

    /**
     * Invalidates the HTTP session if it exists, documenting the process thoroughly for security audits.
     *
     * @param request The HTTP request from which to retrieve the session.
     */
    private static void invalidateSession(HttpServletRequest request) {
        if (request != null && request.getSession(false) != null) {
            request.getSession(false).invalidate();
            logger.info("HTTP session invalidated successfully for [{}]", request.getRemoteAddr());
        } else {
            assert request != null;
            logger.debug("No session found to invalidate for [{}]", request.getRemoteAddr());
        }
    }
}