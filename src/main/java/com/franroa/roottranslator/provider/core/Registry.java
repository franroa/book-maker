package com.franroa.roottranslator.provider.core;


import com.franroa.roottranslator.ROOTranslatorConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Registry {
    private ROOTranslatorConfiguration configuration;
    private List<Object> resources = new ArrayList<>();

    public Registry(ROOTranslatorConfiguration configuration) {
        this.configuration = configuration;
    }

    public void add(Object resourceClass) {
        resources.add(resourceClass);
    }

    public List<Object> getResources() {
        return resources;
    }

    public ROOTranslatorConfiguration getConfiguration() {
        return configuration;
    }

    public void initializeProviders() {
        new CoreProvider().boot(this);
    }
}
