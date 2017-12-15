package com.franroa.roottranslator.services.bookmaker;

import com.franroa.roottranslator.services.translatorgui.TranslationGuiException;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public class HttpClient<T> {
    private final Client client;
    private final String host;
    private String method = "";
    private String url = "";
    private Object request = null;
    private Object mediaType = APPLICATION_JSON;

    public HttpClient(String host) {
        this.client = createHttpClient();
        this.host = host;
    }

    public void closeResponse(Response response) {
        if (response != null) {
            response.close();
        }
    }

    public HttpClient post(String url, Object request) throws TranslationGuiException {
        this.method = "POST";
        this.url = url;
        this.request = request;

        return this;
    }

    public HttpClient put(String url, Object request) throws TranslationGuiException {
        this.method = "PUT";
        this.url = url;
        this.request = request;

        return this;
    }

    public HttpClient get(String url) throws TranslationGuiException {
        this.method = "GET";
        this.url = url;

        return this;
    }

    public Response expects(Integer httpStatus) throws TranslationGuiException {
        Response response;

        if (request == null) {
            response = requestTo(url).method(method);
        } else {
            response = requestTo(url).method(method, Entity.entity(request, (String) mediaType));
        }

        if (httpStatus != null && response.getStatus() != httpStatus) {
            throw new TranslationGuiException("MCS responded with status " + response.getStatus() + " but " + httpStatus + " was expected.");
        }

        return response;
    }

    private Invocation.Builder requestTo(String endpoint) {
        return client.target(host + "/" + endpoint)
                .request();
//                .header("Authorization", "Bearer " + token);
//                .header(CorrelationIdRequestFilter.CORRELATION_ID_MDC, CorrelationIdRequestFilter.getCorrelationId());
    }

    private Client createHttpClient() {
        return ClientBuilder.newClient()
                .property(ClientProperties.CONNECT_TIMEOUT, 5000)
                .property(ClientProperties.READ_TIMEOUT, 5000);
    }

    public HttpClient mediaType(Object mediaType) {
        this.mediaType = mediaType;

        return this;
    }

    public HttpClient register(Class<T> clazz) {
        client.register(clazz);

        return this;
    }
}
