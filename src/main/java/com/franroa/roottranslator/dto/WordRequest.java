package com.franroa.roottranslator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WordRequest {
    @JsonProperty("word")
    public String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
