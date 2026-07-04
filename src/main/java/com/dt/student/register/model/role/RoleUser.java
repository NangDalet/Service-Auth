package com.dt.student.register.model.role;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RoleUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private Boolean checked;
}
