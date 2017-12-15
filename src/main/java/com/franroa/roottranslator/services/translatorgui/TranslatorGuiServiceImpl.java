package com.franroa.roottranslator.services.translatorgui;

import com.fasterxml.jackson.databind.JsonNode;
import com.franroa.roottranslator.services.translatorgui.dto.TranslationRequest;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;

public class TranslatorGuiServiceImpl implements TranslatorGuiService {
    private HttpClient client;

    public TranslatorGuiServiceImpl(TranslatorGuiConfiguration configuration) {
        client = new HttpClient(configuration.host);
    }

    @Override
    public String translateWord(String wordToTranslate, String from, String to) throws TranslationGuiException {
        Response response = null;
        try {
            response = client.post("v1/word", new TranslationRequest(wordToTranslate, from, to)).expects(200);
            return response.readEntity(JsonNode.class).get("word").asText();
        } catch (ProcessingException | TranslationGuiException | IllegalStateException e) {
            throw new TranslationGuiException(e.getMessage());
        } finally {
            client.closeResponse(response);
        }
    }

    @Override
    public JsonNode getTextTranslation(String text, String from, String to) throws TranslationGuiException {
        Response response = null;
        try {
            response = client.post("v1/text", new TranslationRequest(text, from, to)).expects(200);
            return response.readEntity(JsonNode.class).get("words");
        } catch (ProcessingException | TranslationGuiException | IllegalStateException e) {
            throw new TranslationGuiException(e.getMessage());
        } finally {
            client.closeResponse(response);
        }
    }
}
