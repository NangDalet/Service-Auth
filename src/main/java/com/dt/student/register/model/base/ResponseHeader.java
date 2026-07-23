package com.dt.student.register.model.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResponseHeader {

    private Long serverTimestamp;
    private Boolean result;
    private Integer statusCode;
    private String errorCode;
    private String errorText;

    public ResponseHeader() {
        this.serverTimestamp = new Date().getTime();
    }
}
