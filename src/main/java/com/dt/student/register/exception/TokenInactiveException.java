package com.dt.student.register.exception;

public class TokenInactiveException extends RuntimeException {
    public TokenInactiveException(String message) {
        super(message);
    }
}
