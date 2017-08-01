package com.franroa.roottranslator;


import com.fasterxml.jackson.annotation.JsonProperty;
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

    @NotNull
    @JsonProperty("uploadPath")
    private String uploadPath;

    @NotNull
    @JsonProperty("fakedDependenciesEnabled")
    private Boolean fakedDependenciesEnabled;


    public DataSourceFactory getDataSourceFactory() {
        database.setPassword(readDatabasePassword());

        return database;
    }

    public String getUploadPath() {
        return this.uploadPath;
    }

    public Boolean areFakedDependenciesEnabled() {
        return fakedDependenciesEnabled;
    }

    private String readDatabasePassword() {
        try {
            return new String(Files.readAllBytes(Paths.get(DATABASE_PASSWORD_PATH)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
