package com.franroa.roottranslator.services.translatorgui;

import com.fasterxml.jackson.databind.JsonNode;

public class TranslatorGuiServiceInMemory implements TranslatorGuiService{
    private static TranslatorGuiServiceInMemory singleton = null;

    protected TranslatorGuiServiceInMemory() {}

    public static TranslatorGuiServiceInMemory getInstance() {
        if(singleton == null) {
            singleton = new TranslatorGuiServiceInMemory();
        }

        return singleton;
    }

    @Override
    public String translateWord(String wordToTranslate, String from, String to) throws TranslationGuiException {
        return wordToTranslate;
    }

    @Override
    public JsonNode getTextTranslation(String text, String from, String to) throws TranslationGuiException {
        return null;
    }
}
