package com.janiwanow.flatmap.db;

import com.janiwanow.flatmap.util.Env;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

import static com.janiwanow.flatmap.util.Env.ENV;

public enum TestConnectionFactory implements ConnectionFactory {
    INSTANCE;

    private final HikariDataSource dataSource;

    TestConnectionFactory() {
        Env.ensureVarsAreSet(Set.of(
            "TEST_DB_PORT",
            "TEST_DB_USER",
            "TEST_DB_PASSWORD",
            "TEST_DB_NAME"
        ));

        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        props.setProperty("dataSource.portNumber", ENV.get("TEST_DB_PORT"));
        props.setProperty("dataSource.user", ENV.get("TEST_DB_USER"));
        props.setProperty("dataSource.password", ENV.get("TEST_DB_PASSWORD"));
        props.setProperty("dataSource.databaseName", ENV.get("TEST_DB_NAME"));

        dataSource = new HikariDataSource(new HikariConfig(props));
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
