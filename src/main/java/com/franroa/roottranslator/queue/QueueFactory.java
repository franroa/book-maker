package com.franroa.roottranslator.queue;


import com.franroa.roottranslator.container.Factory;
import com.franroa.roottranslator.queue.config.QueueConfiguration;
import com.franroa.roottranslator.queue.config.SupportedQueues;

public class QueueFactory implements Factory<Queue> {
    private QueueConfiguration configuration;

    public QueueFactory(QueueConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Queue create() {
        if (configuration.type.equals(SupportedQueues.QUARTZ)) {
            return new QuartzQueue(configuration);
        }

        if (configuration.type.equals(SupportedQueues.SYNC)) {
            return new SyncQueue();
        }

        throw new RuntimeException(String.format("The configured queue type '%s' is not supported!", configuration.type.toString()));
    }
}
