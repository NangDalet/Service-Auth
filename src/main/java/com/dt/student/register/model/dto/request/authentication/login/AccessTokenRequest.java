package com.dt.student.register.model.dto.request.authentication.login;

import lombok.Data;

@Data
public class AccessTokenRequest {

    private Long id;

    private Long userId;

    private String username;

    private String email;

    private String loginVerifyToken;

    private String accessToken;

    private String tokenType;

    private String refreshToken;

    private Long expiresIn;

    private String scope;

    private Long created;

    private Long isActive;
}
