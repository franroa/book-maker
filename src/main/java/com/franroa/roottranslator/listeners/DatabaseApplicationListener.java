package com.franroa.roottranslator.listeners;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import javax.ws.rs.ext.Provider;

@Provider
public class DatabaseApplicationListener implements ApplicationEventListener {
    @Override
    public void onEvent(ApplicationEvent applicationEvent) { }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return new DatabaseRequestEventListener();
    }
}
