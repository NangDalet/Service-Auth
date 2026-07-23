package com.dt.student.register.controller;

import com.dt.student.register.model.dto.request.authentication.login.LoginRequest;
import com.dt.student.register.authentication.helper.UserAuthSession;
import com.dt.student.register.mapper.primary.auth.AuthMapper;
import com.dt.student.register.model.base.ResponseMessageUtils;
import com.dt.student.register.model.base.BaseResult;
import com.dt.student.register.model.base.ResponseMessage;
import com.dt.student.register.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Authentication Controller.")
public class AuthController {

    private final AuthService authService;
    private final AuthMapper authMapper;
    private final String clientSecret;

    public AuthController(
            AuthService authService,
            AuthMapper authMapper,
            @Value("${jwt.client.secret}") String clientSecret
    ) {
        this.authService = authService;
        this.authMapper = authMapper;
        this.clientSecret = clientSecret;
    }

    // Login with username and password
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage<BaseResult> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        // Extract values
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String deviceId = loginRequest.getDeviceId();
        String deviceName = loginRequest.getDeviceName();

        if (!StringUtils.hasText(username)
                || !StringUtils.hasText(password)
                || !StringUtils.hasText(deviceId)
                || !StringUtils.hasText(deviceName)) {
            return ResponseMessageUtils.makeResponse(false, 400, "Bad Request", "Invalid Parameter");
        }

        if (authMapper.checkUserValid(username) == 0) {
            return ResponseMessageUtils.makeResponse(false, 401, "Unauthorized", "Invalid username and password");
        }

        long accessTokenValiditySeconds = 2_592_000L;
        long refreshTokenValiditySeconds = 7_776_000L;
        boolean clientRegistered = Boolean.TRUE.equals(authMapper.insertClientId(
                deviceId,
                deviceName,
                clientSecret,
                accessTokenValiditySeconds,
                refreshTokenValiditySeconds
        ));

        if (!clientRegistered) {
            return ResponseMessageUtils.makeResponse(false, 400, "Bad Request", "Invalid Parameter");
        }

        authMapper.insertUserSession(username);
        return authService.handleLogin(loginRequest, request, response);
    }



    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh Token", description = "Refresh Token.")
    public ResponseMessage<BaseResult> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return authService.refreshAccessToken(request, response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Logout.")
    public ResponseMessage<BaseResult> logout(HttpServletRequest request, HttpServletResponse response) {
        return authService.handleLogout(request, response);
    }
    
    @PostMapping("/checkToken")
    @Operation(summary = "Check Token", description = "Check Token.")
    public ResponseMessage<String> checkToken() {
        if (UserAuthSession.getUserAuth() != null) {
            return ResponseMessageUtils.makeResponse(true, "Token Valid");
        } else {
            return ResponseMessageUtils.makeResponse(false, 401, "unauthorized", "No Permission to access");
        }
    }
}
