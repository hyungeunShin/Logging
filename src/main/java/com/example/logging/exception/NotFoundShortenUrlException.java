package com.example.logging.exception;

public class NotFoundShortenUrlException extends RuntimeException {
    public NotFoundShortenUrlException(String message) {
        super(message);
    }
}
