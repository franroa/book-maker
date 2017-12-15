package com.franroa.roottranslator.services.mail;

public class EmailServiceException extends RuntimeException {
    public EmailServiceException(String s) {
        super(s);
    }

    public EmailServiceException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
