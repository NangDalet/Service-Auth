package com.dt.student.register.model.dto.response.authentication.role;

import com.dt.student.register.model.dto.response.authentication.module.ModuleType;
import com.dt.student.register.model.role.RoleUser;
import lombok.Data;

import java.util.List;

@Data
public class RoleResponse {
    private Long id;

    private String name;

    private String Username;

    private String createdBy;

    private String createdDate;

    private String modifiedBy;

    private String modifiedDate;

    private List<RoleUser> userList;

    private List<ModuleType> moduleTypeList;

}
