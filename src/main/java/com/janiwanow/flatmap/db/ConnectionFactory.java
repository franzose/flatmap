package com.janiwanow.flatmap.db;

import com.janiwanow.flatmap.util.Env;
import com.janiwanow.flatmap.util.EnvVariablesMissingException;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import static com.janiwanow.flatmap.util.Env.ENV;

/**
 * Database connection factory.
 */
public final class ConnectionFactory {
    /**
     * Environment variables required to establish a new database connection.
     */
    private static final Set<String> ENV_KEYS = Set.of("DB_USER", "DB_PASSWORD", "DB_PORT", "DB_NAME");

    /**
     * Attempts to connect to the database using credentials from the environment variables.
     *
     * @return a new database connection
     * @throws EnvVariablesMissingException if any of the required environment variables is missing
     * @throws SQLException in case of failure establishing a connection
     */
    public static Connection connect() throws EnvVariablesMissingException, SQLException {
        Env.ensureVarsAreSet(ENV_KEYS);

        // Simple data source is used for... simplicity
        // I'm likely to experiment with pooling but later
        var dataSource = new PGSimpleDataSource();
        dataSource.setPortNumbers(new int[] { Integer.parseInt(ENV.get("DB_PORT")) });
        dataSource.setUser(ENV.get("DB_USER"));
        dataSource.setPassword(ENV.get("DB_PASSWORD"));
        dataSource.setDatabaseName(ENV.get("DB_NAME"));

        return dataSource.getConnection();
    }
}
