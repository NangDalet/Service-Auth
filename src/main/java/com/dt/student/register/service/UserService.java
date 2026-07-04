package com.dt.student.register.service;

import com.dt.student.register.model.base.BaseResult;
import com.dt.student.register.model.base.ResponseMessage;
import com.dt.student.register.model.base.filter.Filter;
import com.dt.student.register.model.dto.request.authentication.changePassword.ChangePasswordRequest;
import com.dt.student.register.model.dto.request.authentication.user.UserUpdateRequest;
import com.dt.student.register.model.dto.response.authentication.user.UserResponse;
import com.dt.student.register.model.users.User;
import com.dt.student.register.model.users.UserRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.net.UnknownHostException;

public interface UserService {
    User getUserAuth();
    ResponseMessage<UserResponse> me(HttpServletRequest httpServletRequest) throws UnknownHostException;
    ResponseMessage<BaseResult> getList(Filter filter, HttpServletRequest httpServletRequest) throws UnknownHostException;
    ResponseMessage<BaseResult> getOne(Long id, HttpServletRequest httpServletRequest) throws UnknownHostException;
    ResponseMessage<BaseResult> insert(UserRequest userRequest, HttpServletRequest httpServletRequest) throws UnknownHostException;
    ResponseMessage<BaseResult> update(UserUpdateRequest updateRequest, HttpServletRequest httpServletRequest) throws UnknownHostException;
    ResponseMessage<String> delete(Long id, HttpServletRequest httpServletRequest) throws UnknownHostException;
    ResponseMessage<String> changePassword(ChangePasswordRequest changePasswordRequest, HttpServletRequest httpServletRequest) throws UnknownHostException;

}
