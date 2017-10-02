package com.franroa.roottranslator;

import com.franroa.roottranslator.config.Connection;
import com.franroa.roottranslator.listeners.DatabaseApplicationListener;
import com.franroa.roottranslator.resources.DictionaryResource;
import com.franroa.roottranslator.resources.ImportResource;
import com.franroa.roottranslator.resources.TranslatorResource;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
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

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class ROOTranslatorApplication extends Application<ROOTranslatorConfiguration> {
    private Injector injector;

    private static final String MIGRATIONS_MASTER_FILE_PATH = "changelog/master.xml";

    private Environment environment;
    private ROOTranslatorConfiguration configuration;

    public static void main(final String[] args) throws Exception {
        new ROOTranslatorApplication().run(args);
    }

//    @Override
//    public void initialize(Bootstrap<ROOTranslatorConfiguration> bootstrap) {
//        // nothing to do yet
//    }

    public void run(final ROOTranslatorConfiguration configuration, final Environment environment) {
        this.configuration = configuration;
        this.environment = environment;
//        this.injector = createInjector();

        initializeDb();
        runMigrations();
        registerFeatures();
        registerResources();
        registerEventListeners();

        configureCors(environment);
    }

    private void registerEventListeners() {
        environment.jersey().register(new DatabaseApplicationListener());
    }

    private void registerFeatures() {
        environment.jersey().register(MultiPartFeature.class); // that is needed to upload files
    }

    private void registerResources() {
        environment.jersey().register(new TranslatorResource());
        environment.jersey().register(new ImportResource());
        environment.jersey().register(new DictionaryResource());
    }

    private void initializeDb() {
        DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();
        ManagedDataSource ds = dataSourceFactory.build(environment.metrics(), "pool");
        Connection.setDatasource(ds);
    }

    private void runMigrations() {
        try {
            Connection.open();
            Liquibase migrator = new Liquibase(MIGRATIONS_MASTER_FILE_PATH, new ClassLoaderResourceAccessor(), new JdbcConnection(Connection.get()));
            migrator.update("");
        } catch (LiquibaseException e) {
            e.printStackTrace();
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

    private Injector createInjector() {
        Module appModule = new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(ROOTranslatorConfiguration.class).toInstance(new ROOTranslatorConfiguration());
            }
        };

        return Guice.createInjector(appModule);
    }
}

