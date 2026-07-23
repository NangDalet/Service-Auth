package com.dt.student.register.model.base;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public final class ResponseMessageUtils {

    private ResponseMessageUtils() {
    }

    public static <T> ResponseMessage<T> makeResponse(boolean success, T body) {
        ResponseMessage<T> response = new ResponseMessage<>();
        ResponseHeader header = response.getHeader();
        header.setResult(success);
        header.setStatusCode(HttpStatus.OK.value());
        response.setBody(body);
        return response;
    }

    public static <T> ResponseMessage<T> makeResponse(
            boolean success,
            int statusCode,
            String errorCode,
            String errorText
    ) {
        ResponseMessage<T> response = new ResponseMessage<>();
        ResponseHeader header = response.getHeader();
        header.setResult(success);
        header.setStatusCode(statusCode);
        header.setErrorCode(errorCode);
        header.setErrorText(errorText);
        return response;
    }

    public static <T> ResponseMessage<T> makeResponse(
            boolean success,
            BindingResult bindingResult
    ) {
        Map<String, String> errors = new HashMap<>();
        for (Object error : bindingResult.getAllErrors()) {
            FieldError fieldError = (FieldError) error;
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ResponseMessage<T> response = new ResponseMessage<>();
        ResponseHeader header = response.getHeader();
        header.setResult(success);
        header.setStatusCode(HttpStatus.BAD_REQUEST.value());
        header.setErrorText(new JSONObject(errors).toString());
        return response;
    }
}
