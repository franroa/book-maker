package com.franroa.roottranslator.correlation;

import org.slf4j.MDC;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.UUID;

@Provider
@Priority(666) // set priority of this filter before authorization.
public class CorrelationIdRequestFilter implements ContainerRequestFilter {
    public static final String CORRELATION_ID_MDC = "correlationId";

    private String headerKey;

    public CorrelationIdRequestFilter(String headerKey) {
        this.headerKey = headerKey;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String correlationId = containerRequestContext.getHeaderString(headerKey);

        if (correlationId == null ) {
            correlationId = UUID.randomUUID().toString();
        }

        MDC.put(CORRELATION_ID_MDC, correlationId);
    }

    public static String getCorrelationId() {
        return MDC.get(CORRELATION_ID_MDC);
    }
}
