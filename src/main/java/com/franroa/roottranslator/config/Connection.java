package com.franroa.roottranslator.config;

import io.dropwizard.db.ManagedDataSource;
import org.javalite.activejdbc.Base;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import java.sql.SQLException;

public final class Connection {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Connection.class);
    private static ManagedDataSource dataSource;
    private Connection() {}

    public static void open() {
        if (! Base.hasConnection()) {
            Base.open(dataSource);

            try {
                java.sql.Connection connection = Base.connection();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.error("Cannot set autocommit. " + e.getMessage());
                throw new WebApplicationException();
            }
        }
    }

    public static void close() {
        if (Base.hasConnection()) {
            Base.close();
        }
    }

    public static java.sql.Connection get() {
        return Base.connection();
    }

    public static void setDatasource(ManagedDataSource dataSource) {
        Connection.dataSource = dataSource;
    }
}
