package com.dt.student.register.mapper.primary.auth;

import com.dt.student.register.model.users.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthMapper {


    User findUserDetails(@Param("username") String username);

    Boolean insertClientId(
            @Param("clientId") String clientId,
            @Param("clientName") String clientName,
            @Param("clientSecure") String clientSecure,
            @Param("tokenValidate") Long tokenValidate,
            @Param("refreshTokenValidate") Long refreshTokenValidate
    );

    Long checkUserValid(@Param("username") String username);

    Boolean insertUserSession(@Param("username") String username);

}
