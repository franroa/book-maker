package com.franroa.roottranslator.services.translatorgui;


import com.franroa.roottranslator.container.Factory;

public class TranslatorGuiServiceFactory implements Factory<TranslatorGuiService> {
    private TranslatorGuiConfiguration configuration;

    public TranslatorGuiServiceFactory(TranslatorGuiConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public TranslatorGuiService create() {
//        if (configuration.isUseInMemoryAsDefault()) {
//            return (new TranslatorGuiServiceInMemory()).getInstance();
//        }

        return new TranslatorGuiServiceImpl(configuration);
    }
}
