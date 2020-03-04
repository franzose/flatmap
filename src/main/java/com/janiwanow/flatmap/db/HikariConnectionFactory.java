package com.janiwanow.flatmap.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static com.janiwanow.flatmap.util.Env.ENV;

/**
 * A database connection factory utilizing Hikari connection pool under the hood.
 */
public final class HikariConnectionFactory implements ConnectionFactory {
    private static final HikariConnectionFactory INSTANCE;

    static {
        // TODO: fine-tuning
        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        props.setProperty("dataSource.portNumber", ENV.get("DB_PORT"));
        props.setProperty("dataSource.user", ENV.get("DB_USER"));
        props.setProperty("dataSource.password", ENV.get("DB_PASSWORD"));
        props.setProperty("dataSource.databaseName", ENV.get("DB_NAME"));

        INSTANCE = new HikariConnectionFactory(new HikariDataSource(new HikariConfig(props)));
    }

    private final HikariDataSource dataSource;

    private HikariConnectionFactory(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static HikariConnectionFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
