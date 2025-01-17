package com.janiwanow.flatmap.internal.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static com.janiwanow.flatmap.internal.util.Env.ENV;

/**
 * A database connection factory utilizing Hikari connection pool under the hood.
 */
public enum HikariDbConnectionFactory implements DbConnectionFactory {
    INSTANCE;

    private final HikariDataSource dataSource;

    HikariDbConnectionFactory() {
        // TODO: fine-tuning
        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        props.setProperty("dataSource.portNumber", ENV.get("DB_PORT"));
        props.setProperty("dataSource.user", ENV.get("DB_USER"));
        props.setProperty("dataSource.password", ENV.get("DB_PASSWORD"));
        props.setProperty("dataSource.databaseName", ENV.get("DB_NAME"));

        dataSource = new HikariDataSource(new HikariConfig(props));
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
