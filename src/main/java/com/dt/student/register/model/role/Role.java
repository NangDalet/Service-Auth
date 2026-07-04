package com.dt.student.register.model.role;

import com.dt.student.register.model.base.BaseModel;
import com.dt.student.register.model.dto.response.authentication.module.ModuleType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Role extends BaseModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String modifiedDate;

    private List<RoleUser> userList;

    private List<ModuleType> moduleTypeList;

}
