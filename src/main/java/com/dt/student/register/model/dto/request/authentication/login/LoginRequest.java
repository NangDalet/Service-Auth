package com.dt.student.register.model.dto.request.authentication.login;

import lombok.Data;

@Data
public class LoginRequest {

    private String deviceId;

    private String deviceName;

    private String password;

    private String username;
}
