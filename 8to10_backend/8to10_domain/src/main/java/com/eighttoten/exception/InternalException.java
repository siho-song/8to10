package com.eighttoten.exception;

public class InternalException extends RuntimeException{
    private final String message;

    public InternalException(ExceptionCode exceptionCode) {
        this.message = exceptionCode.getMessage();
    }
}
