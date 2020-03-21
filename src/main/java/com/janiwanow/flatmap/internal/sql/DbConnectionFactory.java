package com.janiwanow.flatmap.internal.sql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for the database connection factory implementations.
 */
public interface DbConnectionFactory {
    /**
     * Attempts to connect to a database.
     *
     * @return a new database connection
     * @throws SQLException in case of a failure
     */
    Connection getConnection() throws SQLException;
}
