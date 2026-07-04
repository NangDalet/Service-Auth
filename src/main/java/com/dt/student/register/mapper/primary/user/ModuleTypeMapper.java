package com.dt.student.register.mapper.primary.user;


import com.dt.student.register.model.dto.response.authentication.module.ModuleType;
import com.dt.student.register.model.base.filter.Filter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleTypeMapper {

    List<ModuleType> getList(Filter filter);

    Long countList(Filter filter);

    List<ModuleType> getOne(Long id);

}