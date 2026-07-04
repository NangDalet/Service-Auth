package com.dt.student.register.model.branch;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BranchList implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long branchId;

}
