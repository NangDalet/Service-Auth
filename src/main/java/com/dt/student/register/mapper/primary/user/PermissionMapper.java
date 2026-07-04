package com.dt.student.register.mapper.primary.user;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userPermissionMapper")
public interface PermissionMapper {

    Long checkPermission(Long userId, String moduleName);

    Boolean insert(Long roleId, Long moduleId);

    Boolean delete(Long roleId);

    List<String> getPermissionCodesByUserId(Long userId);




}