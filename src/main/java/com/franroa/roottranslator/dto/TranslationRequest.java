package com.franroa.roottranslator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;


public class TranslationRequest {
    @JsonProperty("word")
    public String word;

    @JsonProperty("language_from")
    public String language_from;

    @JsonProperty("language_to")
    public String language_to;

    public String getWord() {
        return word;
    }

    public TranslationRequest(String word, String language_from, String language_to) {
        this.word = word;
        this.language_from = language_from;
        this.language_to = language_to;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLanguage_from() {
        return language_from;
    }

    public void setLanguage_from(String language_from) {
        this.language_from = language_from;
    }

    public String getLanguage_to() {
        return language_to;
    }

    public void setLanguage_to(String language_to) {
        this.language_to = language_to;
    }
}
