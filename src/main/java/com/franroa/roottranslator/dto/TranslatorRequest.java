package com.franroa.roottranslator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class TranslatorRequest {
    @JsonProperty("word")
    public String word;

    @JsonProperty("translations")
    public List<TranslationRequest> translationRequestList;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<TranslationRequest> getTranslationRequestList() {
        return translationRequestList;
    }

    public void setTranslationRequestList(List<TranslationRequest> translationRequestList) {
        this.translationRequestList = translationRequestList;
    }
}
