package com.dt.student.register.exception;

import com.dt.student.register.model.base.ResponseMessage;
import com.dt.student.register.model.base.ResponseMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle Validation Errors (from @Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        ResponseMessage<Object> response = ResponseMessageUtils.makeResponse(false, result);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Generic Runtime Exceptions
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseMessage<Object>> handleRuntimeExceptions(RuntimeException ex) {
        log.error("Runtime exception: ", ex);
        ResponseMessage<Object> response = ResponseMessageUtils.makeResponse(
                false, 
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "INTERNAL_SERVER_ERROR", 
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Catch-all for any other Exception
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage<Object>> handleAllExceptions(Exception ex) {
        log.error("Unexpected error: ", ex);
        ResponseMessage<Object> response = ResponseMessageUtils.makeResponse(
                false, 
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "INTERNAL_SERVER_ERROR", 
                "An unexpected error occurred"
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
