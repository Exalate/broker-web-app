package com.example.externalapi.exception;

public class ElementIsUsed extends RuntimeException {

    public ElementIsUsed() {
    }

    public ElementIsUsed(String message) {
        super(message);
    }

    public ElementIsUsed(String message, Throwable cause) {
        super(message, cause);
    }

    public ElementIsUsed(Throwable cause) {
        super(cause);
    }

    public ElementIsUsed(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
