package com.dt.student.register.model.users;

import com.dt.student.register.model.base.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class User extends BaseModel implements Serializable {
    @Schema(hidden = true)
    private Long id;
    private String userCode;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String verificationCode;
    private LocalDateTime verificationCodeExpiry;
    private String telephone;
    private Long userType;
    private String sex;
    private String address;
    private String googleId;
    private String telegramId;
    private String deviceId;
    private String deviceName;
    private String token;
    private String password;
    private String clientId;
    private String clientName;

    private List<UserGroupList> userRoleList;
}

