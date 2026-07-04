package com.dt.student.register.model.dto.response.authentication.role;

import com.dt.student.register.model.dto.response.authentication.module.ModuleList;
import com.dt.student.register.model.users.UserList;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RoleData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private String Username;

    private List<UserList> userList;

    private List<ModuleList> moduleList;
}
