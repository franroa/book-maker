package com.franroa.roottranslator;

import com.franroa.roottranslator.config.Connection;
import com.franroa.roottranslator.jobs.SendTranslateWordToScrapperJob;
import com.franroa.roottranslator.listeners.DatabaseApplicationListener;
import com.franroa.roottranslator.provider.core.Registry;
import com.franroa.roottranslator.queue.QueueManager;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.setup.Environment;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.ws.rs.WebApplicationException;
import java.util.EnumSet;

public class ROOTranslatorApplication extends Application<ROOTranslatorConfiguration> {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ROOTranslatorApplication.class);

    private Environment environment;
    private ROOTranslatorConfiguration configuration;
    private Registry registry;

    public static void main(final String[] args) throws Exception {
        new ROOTranslatorApplication().run(args);
    }

    @Override
    public void run(ROOTranslatorConfiguration configuration, Environment environment) throws Exception {
        this.configuration = configuration;
        this.environment = environment;
        this.registry = new Registry(configuration);
        registry.initializeProviders();

        initializeDb();
        runMigrations();
        registerFeatures();
        initializeResources();

        configureCors(environment);
        environment.lifecycle().manage(new QueueManager());

        new SendTranslateWordToScrapperJob().dispatch();
    }

    private void initializeResources() {
        registry.getResources().forEach(resource -> {
            if (resource instanceof Class) {
                environment.jersey().register((Class) resource);
            } else {
                environment.jersey().register(resource);
            }
        });
    }

    private void registerFeatures() {
        environment.jersey().register(MultiPartFeature.class); // that is needed to upload files
    }

    private void initializeDb() {
        environment.jersey().register(new DatabaseApplicationListener());

        DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();
        ManagedDataSource ds = dataSourceFactory.build(environment.metrics(), "pool");
        Connection.setDatasource(ds);
    }

    private void runMigrations() {
        try {
            Connection.open();
            Liquibase migrator = new Liquibase(
                    configuration.getMigrationsMasterFilePath(),
                    new ClassLoaderResourceAccessor(),
                    new JdbcConnection(Connection.get())
            );
            migrator.update("");
        } catch (LiquibaseException e) {
            String msg = "There was an error while running the migrations on the database: " + e.getMessage();
            LOGGER.error(msg, e);
            throw new WebApplicationException(e);
        } finally {
            Connection.close();
        }
    }

    private void configureCors(Environment environment) {
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter("allowCredentials", "true");
    }
}
