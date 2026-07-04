package com.dt.student.register.model.dto.response.authentication.module;

import com.dt.student.register.model.role.Module;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class ModuleType implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String nameOther;

    private List<Module> moduleList;

    private Map<String, List<Module>> moduleLists;
}
