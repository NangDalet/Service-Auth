package com.dt.student.register.service;

import com.dt.student.register.mapper.primary.user.PermissionMapper;
import com.dt.student.register.mapper.primary.user.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizationUserClaimsService {

    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    public AuthorizationUserClaimsService(
            RoleMapper roleMapper,
            PermissionMapper permissionMapper
    ) {
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    public List<String> getRoleNames(Long userId) {
        return roleMapper.getRoleNamesByUserId(userId);
    }

    public List<String> getPermissionCodes(Long userId) {
        return permissionMapper.getPermissionCodesByUserId(userId);
    }
}