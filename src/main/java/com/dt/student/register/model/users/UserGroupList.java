package com.dt.student.register.model.users;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserGroupList implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String name;
}
