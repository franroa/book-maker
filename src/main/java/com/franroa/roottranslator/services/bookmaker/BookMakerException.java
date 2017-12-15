package com.franroa.roottranslator.services.bookmaker;

public class BookMakerException extends Exception {
    public BookMakerException(String message) {
        super(message);
    }

    public BookMakerException(String message, Throwable cause) {
        super(message, cause);
    }
}
