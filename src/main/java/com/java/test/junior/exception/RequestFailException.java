package com.java.test.junior.exception;

public class RequestFailException extends RuntimeException {
    public RequestFailException(String message) {
        super(message);
    }
}
