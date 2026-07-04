package com.dt.student.register.service;

import com.dt.student.register.model.base.BaseResult;
import com.dt.student.register.model.base.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.net.UnknownHostException;

public interface FileUploadService {

    ResponseMessage<BaseResult> insert(MultipartFile file, HttpServletRequest httpServletRequest) throws UnknownHostException;

}
