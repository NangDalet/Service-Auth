package com.dt.student.register.mapper.primary.user;

import com.dt.student.register.model.dto.response.authentication.module.ModuleType;
import com.dt.student.register.model.role.Role;
import com.dt.student.register.model.role.RoleUser;
import com.dt.student.register.model.base.filter.Filter;
import com.dt.student.register.model.dto.response.authentication.role.RoleResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {

    List<RoleResponse> getList(Filter filter);

    Long countList(Filter filter);

    List<RoleResponse> getOne(Long id);

    List<RoleUser> getUser(Long id);

    Long checkDuplicate(String name, Long id);

    Boolean insert(Role role);

    Boolean update(Role role);

    Boolean delete(Long id);

    List<ModuleType> getModule(Long userId);

    List<ModuleType> getSettingModule(Long userId);

    Boolean insertUserRole(Long userId, Long roleId);

    Boolean insertRoleUser(Long roleId, Long userId);

    Boolean deleteRoleUser(Long userId);

    //Boolean deleteUserRoleByRoleId(Long roleId);

    List<String> getRoleNamesByUserId(Long userId);



}
