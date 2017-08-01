package com.franroa.roottranslator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Configuration {
    private Map<String, String> database;

    public Map<String, String> getDatabase() {
        return database;
    }
}
