package com.patent.evaluator.util.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super("ValidationException : " + message);
    }
}
