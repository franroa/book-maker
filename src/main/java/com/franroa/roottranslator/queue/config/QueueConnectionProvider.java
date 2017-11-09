package com.franroa.roottranslator.queue.config;

import com.franroa.roottranslator.config.Connection;
import org.quartz.utils.ConnectionProvider;

import java.sql.SQLException;

public class QueueConnectionProvider implements ConnectionProvider {
    @Override
    public java.sql.Connection getConnection() throws SQLException {
        return Connection.getDataSource().getConnection();
    }

    @Override
    public void shutdown() throws SQLException {

    }

    @Override
    public void initialize() throws SQLException {

    }
}
