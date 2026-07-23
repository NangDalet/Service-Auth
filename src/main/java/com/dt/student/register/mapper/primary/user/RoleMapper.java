package com.dt.student.register.mapper.primary.user;

import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Repository
public interface RoleMapper {

    List<String> getRoleNamesByUserId(@Param("userId") Long userId);



}
