package com.franroa.roottranslator.services.requester;

import com.franroa.roottranslator.exceptions.InternalServerErrorException;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public class RequesterServiceImpl implements RequesterService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RequesterServiceImpl.class);

    public final Client client;

    public RequesterServiceImpl(Client client) {
        this.client = client;
    }

    public Response post(String url, Entity body) {
        try {
            return client.target(url)
                    .request()
                    .post(body, Response.class);
        } catch(Exception e) {
            LOGGER.error("Error during execution GET callback to partner; url: {}", url);

            throw new InternalServerErrorException();
        }
    }

    @Override
    public Response post(String url, Object body) {
        try {
            return client.target(url)
                    .request()
                    .post(Entity.entity(body, APPLICATION_JSON), Response.class);
        } catch(Exception e) {
            LOGGER.error("Error during execution GET callback to partner; url: {}", url);

            throw new InternalServerErrorException();
        }
    }

    @Override
    public Response get(String url) {
        try {
            return client.target(url).request().get();
        } catch (Exception e) {
            LOGGER.error("Error during execution POST callback to partner; url: {}", url);

            throw new InternalServerErrorException();
        }
    }

    @Override
    public RequesterServiceImpl register(Class<?> var1) {
        client.register(var1);

        return this;
    }
}
