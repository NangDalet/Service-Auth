package com.dt.student.register.model.dto.request.authentication.changePassword;

import com.dt.student.register.model.dto.request.authentication.role.RoleIdList;
import lombok.Data;

import java.util.List;

@Data
public class ChangePasswordRequest {

    private Long userId;

    private String username;

    private String currentPassword;

    private String newPassword;

    private List<RoleIdList> roleList;
}
