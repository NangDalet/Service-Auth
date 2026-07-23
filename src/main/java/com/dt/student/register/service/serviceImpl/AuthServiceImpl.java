package com.dt.student.register.service.serviceImpl;

import com.dt.student.register.authentication.util.AESUtil;
import com.dt.student.register.authentication.util.CookieUtils;
import com.dt.student.register.authentication.util.JwtUtil;
import com.dt.student.register.mapper.primary.auth.AuthMapper;
import com.dt.student.register.mapper.primary.user.PermissionMapper;
import com.dt.student.register.mapper.primary.user.RoleMapper;
import com.dt.student.register.model.base.BaseResult;
import com.dt.student.register.model.base.MessageService;
import com.dt.student.register.model.base.ResponseMessage;
import com.dt.student.register.model.base.ResponseMessageUtils;
import com.dt.student.register.model.dto.request.authentication.login.LoginRequest;
import com.dt.student.register.model.dto.response.authentication.token.AccessTokenResponse;
import com.dt.student.register.model.users.User;
import com.dt.student.register.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthMapper authMapper;
    private final JwtUtil jwtUtil;
    private final MessageService messageService;
    private final PasswordEncoder passwordEncoder;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    public AuthServiceImpl(
            AuthMapper authMapper,
            JwtUtil jwtUtil,
            MessageService messageService,
            PasswordEncoder passwordEncoder,
            RoleMapper roleMapper,
            PermissionMapper permissionMapper
    ) {
        this.authMapper = authMapper;
        this.jwtUtil = jwtUtil;
        this.messageService = messageService;
        this.passwordEncoder = passwordEncoder;
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public ResponseMessage<BaseResult> handleLogin(
            LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            User user = authMapper.findUserDetails(loginRequest.getUsername());

            if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseMessageUtils.makeResponse(
                        false,
                        messageService.message("Invalid username or password.", false)
                );
            }

            List<String> roles = roleMapper.getRoleNamesByUserId(user.getId());
            List<String> permissions = permissionMapper.getPermissionCodesByUserId(user.getId());
            List<AccessTokenResponse> tokens = jwtUtil.generateToken(
                    user.getId(),
                    user.getUsername(),
                    roles,
                    permissions
            );

            AccessTokenResponse token = tokens.getFirst();
            jwtUtil.setSecureCookies(
                    request,
                    response,
                    token.getAccessToken(),
                    token.getRefreshToken()
            );

            BaseResult result = new BaseResult();
            result.setData(tokens);
            return ResponseMessageUtils.makeResponse(true, result);
        } catch (Exception exception) {
            log.error("Login failed for user {}", loginRequest.getUsername(), exception);
            return ResponseMessageUtils.makeResponse(
                    false,
                    messageService.message("Login failed.", false)
            );
        }
    }

    @Override
    public ResponseMessage<BaseResult> refreshAccessToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            String encryptedRefreshToken = CookieUtils.getCookie(request, "refreshToken")
                    .map(Cookie::getValue)
                    .orElse(null);

            if (encryptedRefreshToken == null) {
                return ResponseMessageUtils.makeResponse(
                        false,
                        messageService.message("Refresh token missing.", false)
                );
            }

            String refreshToken = AESUtil.decrypt(encryptedRefreshToken);
            String newAccessToken = jwtUtil.refreshAccessToken(refreshToken);
            jwtUtil.setSecureCookies(request, response, newAccessToken, refreshToken);

            Map<String, String> tokenData = new HashMap<>();
            tokenData.put("accessToken", newAccessToken);
            tokenData.put("refreshToken", refreshToken);

            BaseResult result = new BaseResult();
            result.setData(Collections.singletonList(tokenData));
            return ResponseMessageUtils.makeResponse(true, result);
        } catch (Exception exception) {
            log.warn("Refresh token validation failed", exception);
            return ResponseMessageUtils.makeResponse(
                    false,
                    messageService.message("Invalid refresh token.", false)
            );
        }
    }

    @Override
    public ResponseMessage<BaseResult> handleLogout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        SecurityContextHolder.clearContext();
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        jwtUtil.deleteCookies(request, response);
        return ResponseMessageUtils.makeResponse(
                true,
                messageService.message("Logout successful.", true)
        );
    }
}
