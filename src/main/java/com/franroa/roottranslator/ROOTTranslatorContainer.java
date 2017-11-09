package com.franroa.roottranslator;


import com.franroa.roottranslator.container.Container;
import com.franroa.roottranslator.queue.Queue;
import com.franroa.roottranslator.queue.QueueFactory;
import com.franroa.roottranslator.services.requester.RequesterService;
import com.franroa.roottranslator.services.requester.RequesterServiceFactory;

public class ROOTTranslatorContainer {
    public static void initialize(ROOTranslatorConfiguration configuration) {
        Container.bind(RequesterService.class, new RequesterServiceFactory());
        Container.singleton(Queue.class, new QueueFactory(configuration.getQueueConfiguration()));
    }
}
