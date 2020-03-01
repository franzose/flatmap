package com.janiwanow.flatmap.db;

import com.janiwanow.flatmap.util.ResourceFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Utility used to setup database tables.
 */
public class DatabaseSetup {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseSetup.class);
    private static final String CHECK_TABLES_QUERY;

    static {
        try {
            CHECK_TABLES_QUERY = ResourceFile.readToString("check_required_tables.sql");
        } catch (IOException e) {
            throw new IllegalStateException("Could not read SQL query from file.", e);
        }
    }

    /**
     * Creates database tables is they haven't been created yet.
     *
     * @param connection a database connection
     * @throws SQLException in case of any failure during execution of the SQL queries
     * @throws IOException in case of a failure of reading the necessary resource files
     */
    public static void createTablesIfNotCreated(Connection connection) throws SQLException, IOException {
        if (tablesShouldBeCreated(connection)) {
            createTables(connection);
        }
    }

    /**
     * Checks whether required database tables should be created.
     *
     * @param connection a database connection
     * @return <code>true</code> if any of the database tables is missing,
     *         <code>false</code> if all database tables exist
     * @throws SQLException in case of any failure during execution of the SQL queries
     */
    private static boolean tablesShouldBeCreated(Connection connection) throws SQLException {
        LOG.info("Let's check if the installation is required...");

        try (
            var stmt = connection.createStatement();
            var result = stmt.executeQuery(CHECK_TABLES_QUERY)
        ) {
            result.next();

            if (!result.next()) {
                LOG.info("Some database tables might be missing, proceeding to setup...");
                return true;
            }
        }

        LOG.info("Everything is in place.");

        return false;
    }

    /**
     * Creates database tables required for the application to function.
     *
     * @param connection a database connection
     * @throws SQLException in case of any failure during execution of the SQL queries
     * @throws IOException in case of a failure of reading the resource file
     */
    private static void createTables(Connection connection) throws SQLException, IOException {
        LOG.info("Start creating database tables...");

        var queries = ResourceFile.readToString("setup_required_tables.sql").split(";");

        for (var query : queries) {
            try (var stmt = connection.prepareStatement(query)) {
                stmt.executeUpdate();
            }
        }

        LOG.info("All database tables have been created successfully.");
    }
}
