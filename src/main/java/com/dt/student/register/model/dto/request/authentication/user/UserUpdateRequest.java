package com.dt.student.register.model.dto.request.authentication.user;

import lombok.Data;

@Data
public class UserUpdateRequest {

    private Long id;

    private String firstName;

    private String lastName;

    private String gender;

    private String dateOfBirth;

    private String telephone;

    private String nationalityId;

    private String address;

    private String email;

    private String signature;

    private Long[] branchId;

}
