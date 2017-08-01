package com.franroa.roottranslator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TranslationRequest {
    @JsonProperty("translation")
    public String translation;

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
