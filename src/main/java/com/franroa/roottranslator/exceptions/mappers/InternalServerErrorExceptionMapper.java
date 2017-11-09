package com.franroa.roottranslator.exceptions.mappers;


import com.franroa.roottranslator.dto.error.InternalServerErrorResponse;
import com.franroa.roottranslator.exceptions.InternalServerErrorException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class InternalServerErrorExceptionMapper implements ExceptionMapper<InternalServerErrorException> {
    @Override
    public Response toResponse(InternalServerErrorException e) {
        return Response.status(InternalServerErrorResponse.HTTP_CODE).entity(new InternalServerErrorResponse()).build();
    }
}
