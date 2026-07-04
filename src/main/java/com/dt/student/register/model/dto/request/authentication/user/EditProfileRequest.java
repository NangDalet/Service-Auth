package com.dt.student.register.model.dto.request.authentication.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EditProfileRequest {

    @Schema(hidden = true)
    private Long id;

    private Long userType;

    private String firstName;

    private String lastName;

    private String address;

    private String telephone;

    private String sex;
}
