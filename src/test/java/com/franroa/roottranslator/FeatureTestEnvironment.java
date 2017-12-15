package com.franroa.roottranslator;

import com.franroa.roottranslator.resources.ImportResource;
import io.dropwizard.testing.junit.ResourceTestRule;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.logging.LogFactory;
import liquibase.logging.LogLevel;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.javalite.activejdbc.Base;
import org.junit.*;

import java.sql.SQLException;

public class FeatureTestEnvironment {
    private static boolean migrated = false;

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addProvider(MultiPartFeature.class)
//            .addResource(new MultiPartFeature())
            .addResource(new ImportResource())
            .build();

    @BeforeClass
    public static void setUp() throws Exception {
        openDatabaseConnection();
        migrate();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        closeDatabaseConnection();
    }

    @Before
    public void startTransaction() {
        Base.openTransaction();
    }

    @After
    public void rollbackTransaction() {
        Base.rollbackTransaction();
    }

    private static void openDatabaseConnection() {
        Base.open("org.postgresql.Driver", "jdbc:postgresql://localhost:54322/dictionary_db_test", "postgres", "password");
    }

    private static void closeDatabaseConnection() {
        Base.close();
    }

    private static void migrate() throws LiquibaseException, SQLException {
        if (migrated) {
            return;
        }

        LogFactory.getInstance().getLog().setLogLevel(LogLevel.WARNING);
        new Liquibase("changelog/master.xml", new ClassLoaderResourceAccessor(), new JdbcConnection(Base.connection())).update("");
        Base.connection().setAutoCommit(true);
        migrated = true;
    }
}
