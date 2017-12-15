package com.franroa.roottranslator.services.translatorgui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TranslateTextResponse {
    @JsonProperty("id")
    public Long id;

    public TranslateTextResponse(Long id) {
        this.id = id;
    }
}
