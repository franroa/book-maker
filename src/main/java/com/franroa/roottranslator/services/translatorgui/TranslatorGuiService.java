package com.franroa.roottranslator.services.translatorgui;

import com.fasterxml.jackson.databind.JsonNode;

public interface TranslatorGuiService {
    String translateWord(String wordToTranslate, String from, String to) throws TranslationGuiException;
    JsonNode getTextTranslation(String text, String from, String to) throws TranslationGuiException;
}
