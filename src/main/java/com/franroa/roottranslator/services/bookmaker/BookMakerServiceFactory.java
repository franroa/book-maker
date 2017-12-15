package com.franroa.roottranslator.services.bookmaker;

import com.franroa.roottranslator.container.Factory;

public class BookMakerServiceFactory implements Factory<BookMakerService>{
    private BookMakerServiceConfiguration configuration;

    public BookMakerServiceFactory(BookMakerServiceConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public BookMakerService create() {
//        if (configuration.isUseInMemoryAsDefault()) {
////            return (new TranslatorGuiServiceInMemory()).getInstance();
//        }

        return new BookMakerServiceImpl(configuration);
    }
}
