package com.franroa.roottranslator.dto.error;

import org.eclipse.jetty.http.HttpStatus;

public class InternalServerErrorResponse extends AbstractErrorResponse {
    public static final Integer HTTP_CODE = HttpStatus.INTERNAL_SERVER_ERROR_500;
    public static final String ERROR_CODE = "INTERNAL_SERVER_ERROR";
    public static final String ERROR_MESSAGE = "There was an internal server error. Please try again later.";

    public InternalServerErrorResponse() {
        this.errorCode = ERROR_CODE;
        this.errorMessage = ERROR_MESSAGE;
    }
}
