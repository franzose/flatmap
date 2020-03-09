package com.janiwanow.flatmap.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static com.janiwanow.flatmap.util.Env.ENV;

public final class TestConnectionFactory implements ConnectionFactory {
    private static final TestConnectionFactory INSTANCE;

    static {
        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        props.setProperty("dataSource.portNumber", ENV.get("TEST_DB_PORT", ENV.get("DB_PORT")));
        props.setProperty("dataSource.user", ENV.get("TEST_DB_USER", ENV.get("DB_USER")));
        props.setProperty("dataSource.password", ENV.get("TEST_DB_PASSWORD", ENV.get("DB_PASSWORD")));
        props.setProperty("dataSource.databaseName", ENV.get("TEST_DB_NAME"));

        INSTANCE = new TestConnectionFactory(new HikariDataSource(new HikariConfig(props)));
    }

    private final HikariDataSource dataSource;

    private TestConnectionFactory(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static TestConnectionFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
