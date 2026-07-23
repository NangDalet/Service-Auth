package com.dt.student.register.mapper.primary.user;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userPermissionMapper")
public interface PermissionMapper {

    List<String> getPermissionCodesByUserId(@Param("userId") Long userId);




}
