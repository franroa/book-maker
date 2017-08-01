package com.franroa.roottranslator;

import com.franroa.roottranslator.resources.ImportResource;
import com.franroa.roottranslator.resources.TranslatorResource;
import io.dropwizard.testing.junit.ResourceTestRule;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.javalite.activejdbc.Base;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

public class FeatureTestEnvironment {
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addProvider(MultiPartFeature.class)
            .addResource(new ImportResource())
            .addResource(TranslatorResource.class)
            .build();

    private static Configuration config;

    @BeforeClass
    public static void setUp() throws Exception {
        Base.open("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/dictionary_db_test", "postgres", "password");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        Base.close();
    }

    @Before
    public void migrate() throws LiquibaseException {
        new Liquibase("changelog/master.xml", new ClassLoaderResourceAccessor(), new JdbcConnection(Base.connection())).update("");
    }
}
