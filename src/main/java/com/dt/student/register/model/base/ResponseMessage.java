package com.dt.student.register.model.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResponseMessage<T> {

    private ResponseHeader header;

    private boolean success;

    private T body;

    public boolean success() {
        return header != null && header.getResult();
    }

    public boolean isSuccess() {
        return header != null && header.getResult();
    }

    public ResponseHeader getHeader() {
        if (header == null) {
            header = new ResponseHeader();
        }
        return header;
    }

}