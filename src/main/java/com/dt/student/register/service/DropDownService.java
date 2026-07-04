package com.dt.student.register.service;

import com.dt.student.register.model.base.BaseResult;
import com.dt.student.register.model.base.ResponseMessage;
import com.dt.student.register.model.base.filter.Filter;
import jakarta.servlet.http.HttpServletRequest;

import java.net.UnknownHostException;

public interface DropDownService {

    ResponseMessage<BaseResult> getListUser(Filter filter, HttpServletRequest httpServletRequest) throws UnknownHostException;

    ResponseMessage<BaseResult> getListGroup(Filter filter, HttpServletRequest httpServletRequest) throws UnknownHostException;

    ResponseMessage<BaseResult> getListBranch(Filter filter, HttpServletRequest httpServletRequest) throws UnknownHostException;

    ResponseMessage<BaseResult> getListProgram(Filter filter, HttpServletRequest httpServletRequest) throws UnknownHostException;

}
