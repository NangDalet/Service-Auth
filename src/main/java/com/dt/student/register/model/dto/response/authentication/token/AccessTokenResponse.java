package com.dt.student.register.model.dto.response.authentication.token;

import lombok.Data;

@Data
public class AccessTokenResponse {

    private String tokenType;

    private String accessToken;

    private String refreshToken;

    private Long expiresIn;

    private String scope;

    public AccessTokenResponse(String tokenType, String accessToken, String refreshToken, long expiresIn, String scope) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
    }
}
