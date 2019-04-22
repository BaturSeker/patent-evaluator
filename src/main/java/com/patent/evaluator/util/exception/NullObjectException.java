package com.patent.evaluator.util.exception;

public class NullObjectException extends RuntimeException {
    public NullObjectException(String message) {
        super("NullObjectException : " + message);
    }
}
