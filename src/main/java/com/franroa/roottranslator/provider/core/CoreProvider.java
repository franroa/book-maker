package com.franroa.roottranslator.provider.core;


import com.franroa.roottranslator.container.Container;
import com.franroa.roottranslator.correlation.CorrelationIdRequestFilter;
import com.franroa.roottranslator.correlation.CorrelationIdResponseFilter;
import com.franroa.roottranslator.queue.Queue;
import com.franroa.roottranslator.queue.QueueFactory;
import com.franroa.roottranslator.resources.ImportResource;
import com.franroa.roottranslator.resources.TranslatorResource;
import com.franroa.roottranslator.services.bookmaker.BookMakerService;
import com.franroa.roottranslator.services.bookmaker.BookMakerServiceFactory;
import com.franroa.roottranslator.services.mail.EmailService;
import com.franroa.roottranslator.services.mail.EmailServiceImpl;
import com.franroa.roottranslator.services.translatorgui.TranslatorGuiService;
import com.franroa.roottranslator.services.translatorgui.TranslatorGuiServiceFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class CoreProvider implements Provider {
    @Override
    public void boot(Registry registry) {
        Container.bind(EmailService.class, () -> new EmailServiceImpl(registry.getConfiguration().getEmailConfiguration()));
        Container.bind(TranslatorGuiService.class, new TranslatorGuiServiceFactory(registry.getConfiguration().getTranslatorGuiConfiguration()));
        Container.bind(BookMakerService.class, new BookMakerServiceFactory(registry.getConfiguration().getBookMakerConfiguation()));

        Container.singleton(Queue.class, new QueueFactory(registry.getConfiguration().getQueueConfiguration()));

        registry.add(new CorrelationIdRequestFilter(registry.getConfiguration().getCorrelation().headerKey));
        registry.add(new CorrelationIdResponseFilter(registry.getConfiguration().getCorrelation().headerKey));

        registry.add(ImportResource.class);
        registry.add(TranslatorResource.class);
        registry.add(MultiPartFeature.class);
    }
}
