package com.example.externalapi.exception;

public class IncorrectOperation extends RuntimeException {

    public IncorrectOperation() {
    }

    public IncorrectOperation(String message) {
        super(message);
    }

    public IncorrectOperation(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectOperation(Throwable cause) {
        super(cause);
    }

    public IncorrectOperation(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
