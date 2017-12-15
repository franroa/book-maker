package com.franroa.roottranslator.services.mail.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SmtpConfiguration {
    @JsonProperty("host")
    public String host;

    @JsonProperty("port")
    public String port;
}
