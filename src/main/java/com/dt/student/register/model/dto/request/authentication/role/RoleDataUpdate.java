package com.dt.student.register.model.dto.request.authentication.role;

import com.dt.student.register.model.dto.response.authentication.module.ModuleList;
import com.dt.student.register.model.users.UserList;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RoleDataUpdate implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private List<UserList> userList;

    private List<ModuleList> moduleList;
}
