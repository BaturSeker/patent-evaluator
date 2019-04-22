package com.patent.evaluator.util.exception;

public class PasswordNotGenerateException extends RuntimeException {
    public PasswordNotGenerateException(String message) {
        super("PasswordNotGenerateException : " + message);
    }
}
