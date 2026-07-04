package com.dt.student.register.model.dto.response.authentication.user;

import com.dt.student.register.model.dto.response.authentication.module.ModuleType;
import com.dt.student.register.model.users.UserBranch;
import com.dt.student.register.model.users.UserGroupList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserResponse implements Serializable {

    private Long id;

    private Long employeeId;

    private Long roomId;

    private Long nationalityId;

    private String nationalityName;

    private Long userType;

    private String firstName;

    private String lastName;

    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private String address;

    private String telephone;

    private String gender;

    private String dateOfBirth;

    private String expireDate;

    private String signature;

    private String createdBy;

    private String createdDate;

    private String modifiedBy;

    private String modified;

    private List<UserBranch> branchList;

    private List<ModuleType> moduleTypeList;


    private List<UserGroupList> userRoleList;

}
