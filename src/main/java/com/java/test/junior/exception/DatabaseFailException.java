package com.java.test.junior.exception;

public class DatabaseFailException extends RuntimeException {
    public DatabaseFailException(String message) {
        super(message);
    }
}
