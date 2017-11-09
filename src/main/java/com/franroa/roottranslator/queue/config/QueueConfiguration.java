package com.franroa.roottranslator.queue.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class QueueConfiguration {
    @JsonProperty("type")
    public SupportedQueues type;

    @JsonProperty("quartz")
    public Map<String, String> quartz = new HashMap<>();
}
