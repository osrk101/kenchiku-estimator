package com.kenchiku_estimator.exception;

public class DataBaseAccessException extends RuntimeException {
    public DataBaseAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
