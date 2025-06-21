package com.project.authorizationserver.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
