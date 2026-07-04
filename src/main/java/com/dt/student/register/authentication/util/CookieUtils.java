package com.dt.student.register.authentication.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;
import java.util.logging.Logger;

public class CookieUtils {

    private static final Logger LOGGER = Logger.getLogger(CookieUtils.class.getName());

    /**
     * Retrieves a cookie by name from the request.
     *
     * @param request The HTTP request.
     * @param name    The name of the cookie.
     * @return An optional containing the cookie if found.
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Adds or updates a cookie in the response.
     *
     * @param request   The HTTP request.
     * @param response  The HTTP response.
     * @param name      The name of the cookie.
     * @param value     The value of the cookie.
     * @param maxAge    The max age (in seconds) before the cookie expires.
     * @param isHttpOnly Whether the cookie should be HttpOnly.
     * @param sameSite  The SameSite attribute (Strict, Lax, None).
     */
    public static void addOrUpdateCookie(HttpServletRequest request, HttpServletResponse response,
                                         String name, String value, int maxAge,
                                         boolean isHttpOnly, String sameSite) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(isHttpOnly); // ✅ Prevent JavaScript access
        cookie.setSecure(true); // ✅ Ensure HTTPS-only
        cookie.setMaxAge(maxAge);

        // ✅ Add the cookie to the response
        response.addCookie(cookie);

        // ❌ DO NOT add "Set-Cookie" header for refreshToken
        if (!"refreshToken".equals(name)) {
            StringBuilder cookieHeader = new StringBuilder();
            cookieHeader.append(name).append("=").append(value)
                    .append("; Path=/")
                    .append("; Max-Age=").append(maxAge)
                    .append("; HttpOnly; Secure");

            if (sameSite != null && !sameSite.isEmpty()) {
                cookieHeader.append("; SameSite=").append(sameSite);
            }

            response.addHeader("Set-Cookie", cookieHeader.toString());
        }
    }

    /**
     * Deletes a cookie from the response by setting its max age to 0.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @param name     The name of the cookie to delete.
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        getCookie(request, name).ifPresent(cookie -> {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            cookie.setHttpOnly(true);
            cookie.setSecure(request.isSecure());

            response.addCookie(cookie);
            LOGGER.info("Cookie deleted: " + name);
        });
    }
}
