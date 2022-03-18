package com.example.externalapi.exception;

public class DataConflict extends RuntimeException {

    public DataConflict() {
    }

    public DataConflict(String message) {
        super(message);
    }

    public DataConflict(String message, Throwable cause) {
        super(message, cause);
    }

    public DataConflict(Throwable cause) {
        super(cause);
    }

    public DataConflict(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
