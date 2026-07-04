package com.dt.student.register.model.dto.response.authentication.module;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ModuleList implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long moduleId;
}
