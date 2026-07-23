package com.dt.student.register.model.base;


import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public BaseResult message(String text, boolean status) {
        BaseResult baseResult = new BaseResult();
        baseResult.setMessage(text);
        baseResult.setStatus(status);
        return baseResult;
    }

}
