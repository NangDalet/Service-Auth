package com.dt.student.register.service;

import com.dt.student.register.model.base.ResponseMessage;
import com.dt.student.register.model.dto.request.authentication.login.LoginRequest;
import com.dt.student.register.model.base.BaseResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {


    ResponseMessage<BaseResult> handleLogin(LoginRequest loginUser, HttpServletRequest httpServletRequest, HttpServletResponse response);

    ResponseMessage<BaseResult> refreshAccessToken(HttpServletRequest request, HttpServletResponse response);

    ResponseMessage<BaseResult> handleLogout(HttpServletRequest request, HttpServletResponse response);


}
