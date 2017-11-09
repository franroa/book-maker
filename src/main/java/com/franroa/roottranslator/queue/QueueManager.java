package com.franroa.roottranslator.queue;

import com.franroa.roottranslator.container.Container;
import io.dropwizard.lifecycle.Managed;

public class QueueManager implements Managed {
    @Override
    public void start() throws Exception {
        Container.resolve(Queue.class).start();
    }

    @Override
    public void stop() throws Exception {
        Container.resolve(Queue.class).stop();
    }
}
