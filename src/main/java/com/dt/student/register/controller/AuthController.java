package com.dt.student.register.controller;

import com.dt.student.register.model.dto.request.authentication.login.LoginRequest;
import com.dt.student.register.authentication.helper.UserAuthSession;
import com.dt.student.register.mapper.primary.auth.AuthMapper;
import com.dt.student.register.model.base.MessageService;
import com.dt.student.register.model.base.ResponseMessageUtils;
import com.dt.student.register.model.base.BaseResult;
import com.dt.student.register.model.base.ResponseMessage;
import com.dt.student.register.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Authentication Controller.")
public class AuthController {

    private final AuthService authService;
    @Autowired
    private AuthMapper authMapper;
    @Value("${jwt.client.secret}")
    private String clientSecret;

    @Value("${jwt.access.token.expiration.ms}")
    private Long tokenValidate;

    @Value("${jwt.refresh.token.expiration.ms}")
    private Long refreshTokenValidate;

    @Autowired
    private MessageService messageService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
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

        // Input validation
        if (!username.isEmpty() && !password.isEmpty() && !deviceId.isEmpty() && !deviceName.isEmpty()) {
            Long checkUser = authMapper.checkUserValid(username);
            if (checkUser > 0) {
                // Convert expiration from ms to seconds
                // 30 days = 30 * 24 * 3600 seconds = 2,592,000 seconds
                long tokenValidate = 2_592_000L;
                // 90 days = 90 * 24 * 3600 seconds = 7,776,000 seconds
                long refreshTokenValidate = 7_776_000L;
                if (authMapper.insertClientId(deviceId, deviceName, clientSecret, tokenValidate, refreshTokenValidate)) {
                    authMapper.insertUserSession(username);
                    return authService.handleLogin(loginRequest, request, response);
                } else {
                    return ResponseMessageUtils.makeResponse(false, 400, "Bad Request", "Invalid Parameter");
                }
            } else {
                return ResponseMessageUtils.makeResponse(false, 401, "Unauthorized", "Invalid username and password");
            }
        } else {
            return ResponseMessageUtils.makeResponse(false, 400, "Bad Request", "Invalid Parameter");
        }
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
