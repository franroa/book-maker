package com.franroa.roottranslator.listeners;

import com.franroa.roottranslator.config.Connection;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEvent.Type;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

public class DatabaseRequestEventListener implements RequestEventListener {
    @Override
    public void onEvent(RequestEvent event) {
        if (event.getType() == Type.REQUEST_MATCHED) {
            Connection.open();
        }

        if (event.getType() == Type.RESOURCE_METHOD_FINISHED) {
            Connection.close();
        }
    }
}
