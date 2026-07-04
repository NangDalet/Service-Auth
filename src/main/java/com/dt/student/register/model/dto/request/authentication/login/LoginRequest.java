package com.dt.student.register.model.dto.request.authentication.login;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {

    @ApiModelProperty(position = 1)
    private String deviceId;

    @ApiModelProperty(position = 2)
    private String deviceName;

    @ApiModelProperty(position = 3)
    private String password;

    @ApiModelProperty(position = 4)
    private String username;

    @Schema(hidden = true)
    @ApiModelProperty(position = 5)
    private String siteKey;

}
