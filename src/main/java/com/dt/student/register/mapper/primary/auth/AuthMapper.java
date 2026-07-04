package com.dt.student.register.mapper.primary.auth;

import com.dt.student.register.model.users.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuthMapper {


    User findUserDetails(String username);

    Boolean insertClientId(String clientId, String clientName, String clientSecure, Long tokenValidate, Long refreshTokenValidate);

    Long checkUserValid(@Param("username") String username);

    String checkUserLogout(@Param("username") String username, @Param("clientId") String clientId);

    Boolean clearUserToken(@Param("username") String username, @Param("clientId") String clientId, @Param("tokenId") String tokenId);

    Boolean insertUserSession(@Param("username") String username);

}
