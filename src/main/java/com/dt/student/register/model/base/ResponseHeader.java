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
    private String token;
    private Pagination pagination;

    public ResponseHeader() {
        Date date = new Date();
        this.serverTimestamp = date.getTime();  // Sets the current timestamp in milliseconds
    }
}
