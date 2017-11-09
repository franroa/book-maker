package com.franroa.roottranslator.services.requester;

import com.franroa.roottranslator.container.Factory;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class RequesterServiceFactory implements Factory<RequesterService> {
    @Override
    public RequesterService create() {
        return new RequesterServiceImpl(createCallbackClient());
    }

    private Client createCallbackClient() {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder()
                .credentials("test", "pass")
                .build();

        return ClientBuilder.newClient(new ClientConfig().register(feature))
                .property(ClientProperties.CONNECT_TIMEOUT, 5000)
                .property(ClientProperties.READ_TIMEOUT, 5000);
    }
}
