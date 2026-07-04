package com.dt.student.register.service;

import com.dt.student.register.model.base.BaseResult;
import com.dt.student.register.model.base.ResponseMessage;
import com.dt.student.register.model.base.filter.Filter;
import com.dt.student.register.model.dto.request.authentication.role.RoleDataUpdate;
import com.dt.student.register.model.dto.response.authentication.role.RoleData;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.net.UnknownHostException;

public interface RoleService {

    ResponseMessage<BaseResult> getList(Filter filter, HttpServletRequest httpServletRequest) throws UnknownHostException;

    ResponseMessage<BaseResult> getOne(Long id, HttpServletRequest httpServletRequest) throws UnknownHostException;

    ResponseMessage<BaseResult> menu(HttpServletRequest httpServletRequest) throws UnknownHostException;

    ResponseMessage<BaseResult> insert(RoleData roleData, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws UnknownHostException;

    ResponseMessage<BaseResult> update(RoleDataUpdate roleDataUpdate, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws UnknownHostException;

    ResponseMessage<BaseResult> delete(Long id, HttpServletRequest httpServletRequest) throws UnknownHostException;
}
