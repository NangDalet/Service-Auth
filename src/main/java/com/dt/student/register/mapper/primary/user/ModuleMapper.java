package com.dt.student.register.mapper.primary.user;

import org.springframework.stereotype.Repository;
import com.dt.student.register.model.role.Module;

import java.util.List;

@Repository
public interface ModuleMapper {

    List<Module> getOne(Long id);

    List<Module> getOneByRoleId(Long id, Long roleId);

    List<Module> getOneByUserId(Long id, Long userId);

}