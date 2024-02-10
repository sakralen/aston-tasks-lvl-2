package edu.sakralen.task2.util.connectionmanager.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.sakralen.task2.exception.DriverClassNotFoundException;
import edu.sakralen.task2.util.PropertiesUtils;
import edu.sakralen.task2.util.connectionmanager.ConnectionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariCpConnectionManager implements ConnectionManager {
    private static final HikariCpConnectionManager INSTANCE = new HikariCpConnectionManager();

    private static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
    private static final DataSource DATA_SOURCE;

    static {
        loadDriverClass();
        DATA_SOURCE = new HikariDataSource(new HikariConfig(PropertiesUtils.getProperties()));
    }

    private HikariCpConnectionManager() {
    }

    public static HikariCpConnectionManager getInstance() {
        return INSTANCE;
    }

    private static void loadDriverClass() {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            throw new DriverClassNotFoundException(e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}
