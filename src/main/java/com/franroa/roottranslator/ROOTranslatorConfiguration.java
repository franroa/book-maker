package com.franroa.roottranslator;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.franroa.roottranslator.correlation.CorrelationConfig;
import com.franroa.roottranslator.queue.config.QueueConfiguration;
import com.franroa.roottranslator.services.bookmaker.BookMakerServiceConfiguration;
import com.franroa.roottranslator.services.mail.config.EmailServiceConfiguration;
import com.franroa.roottranslator.services.translatorgui.TranslatorGuiConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ROOTranslatorConfiguration extends Configuration {
    private static final String DATABASE_PASSWORD_PATH = "secrets/database";

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("email")
    private EmailServiceConfiguration email;

    @JsonProperty("queue")
    private QueueConfiguration queue;

    @JsonProperty("correlation")
    private CorrelationConfig correlation;

    @JsonProperty("translatorGui")
    private TranslatorGuiConfiguration translatorGui;

    @JsonProperty("bookMaker")
    private BookMakerServiceConfiguration bookMaker;

    public DataSourceFactory getDataSourceFactory() {
        if (database.getPassword() == null) {
            database.setPassword(readDatabasePassword());
        }

        return database;
    }

    private String readDatabasePassword() {
        try {
            return new String(Files.readAllBytes(Paths.get(DATABASE_PASSWORD_PATH)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public EmailServiceConfiguration getEmailConfiguration() {
        return email != null ? email : new EmailServiceConfiguration();
    }

    public QueueConfiguration getQueueConfiguration() {
        return queue != null ? queue : new QueueConfiguration();
    }

    public CorrelationConfig getCorrelation() {
        return correlation != null ? correlation : new CorrelationConfig();
    }

    public String getMigrationsMasterFilePath() {
        return "changelog/master.xml";
    }

    public TranslatorGuiConfiguration getTranslatorGuiConfiguration() {
        return translatorGui;
    }

    public BookMakerServiceConfiguration getBookMakerConfiguation() {
        return bookMaker;
    }
}
