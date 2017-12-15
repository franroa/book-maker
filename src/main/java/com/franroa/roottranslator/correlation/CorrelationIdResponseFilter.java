package com.franroa.roottranslator.correlation;

import org.slf4j.MDC;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorrelationIdResponseFilter implements ContainerResponseFilter {
    private String headerKey;

    public CorrelationIdResponseFilter(String headerKey) {
        this.headerKey = headerKey;
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        response.getHeaders().add(headerKey, MDC.get(CorrelationIdRequestFilter.CORRELATION_ID_MDC));
    }
}
