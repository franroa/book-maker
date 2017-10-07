package com.franroa.roottranslator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

public class BookTextResponse {
    @JsonProperty("text")
    public String text;

    @JsonProperty("words")
    public HashSet words;

    public BookTextResponse(String text, HashSet words) {
        this.text = text;
        this.words = words;
    }
}
