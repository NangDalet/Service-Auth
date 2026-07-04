package com.dt.student.register.service;

import com.dt.student.register.model.base.BaseResult;
import com.dt.student.register.model.base.ResponseMessage;
import com.dt.student.register.model.base.filter.ModuleTypeFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.net.UnknownHostException;

public interface ModuleTypeService {

    ResponseMessage<BaseResult> getList(ModuleTypeFilter filter, HttpServletRequest httpServletRequest) throws UnknownHostException;

    ResponseMessage<BaseResult> getOne(Long id, HttpServletRequest httpServletRequest) throws UnknownHostException;

}
