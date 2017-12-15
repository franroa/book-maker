package com.franroa.roottranslator.services.mail.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class EmailServiceConfiguration {
    @JsonProperty("driver")
    @NotEmpty
    public SupportedMailDrivers driver;

    @JsonProperty("from")
    @NotNull
    public FromConfiguration from;

    @JsonProperty("smtp")
    public SmtpConfiguration smtp;
}
