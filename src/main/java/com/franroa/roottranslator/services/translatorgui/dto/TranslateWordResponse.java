package com.franroa.roottranslator.services.translatorgui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TranslateWordResponse {
    @JsonProperty("word")
    public String word;

    public TranslateWordResponse(String word) {
        this.word = word;
    }
}
