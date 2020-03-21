package com.janiwanow.flatmap.console;

import com.beust.jcommander.Parameters;
import com.janiwanow.flatmap.internal.console.Command;
import com.janiwanow.flatmap.internal.sql.DbConnectionFactory;
import com.janiwanow.flatmap.util.ResourceFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Command used to setup database tables.
 */
@Parameters(commandNames = "db:setup", commandDescription = "Sets up database tables and data")
public final class SetupDatabaseCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(SetupDatabaseCommand.class);
    private static final String CHECK_TABLES_QUERY;

    static {
        try {
            CHECK_TABLES_QUERY = ResourceFile.readToString(
                SetupDatabaseCommand.class,
                "check_required_tables.sql"
            );
        } catch (IOException e) {
            throw new IllegalStateException("Could not read SQL query from file.", e);
        }
    }

    private final DbConnectionFactory factory;

    public SetupDatabaseCommand(DbConnectionFactory factory) {
        Objects.requireNonNull(factory, "SQL connection factory must not be null.");
        this.factory = factory;
    }

    /**
     * Creates database tables if they haven't been created yet.
     */
    @Override
    public void execute() {
        try {
            if (tablesShouldBeCreated()) {
                createTables();
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks whether required database tables should be created.
     *
     * @return <code>true</code> if any of the database tables is missing,
     *         <code>false</code> if all database tables exist
     * @throws SQLException in case of any failure during execution of the SQL queries
     */
    private boolean tablesShouldBeCreated() throws SQLException {
        LOG.info("Let's check if the installation is required...");

        try (
            var conn = factory.getConnection();
            var stmt = conn.createStatement();
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
     * @throws SQLException in case of any failure during execution of the SQL queries
     * @throws IOException in case of a failure of reading the resource file
     */
    private void createTables() throws SQLException, IOException {
        LOG.info("Start creating database tables...");

        var queries = ResourceFile.readToString(
            SetupDatabaseCommand.class,
            "setup_required_tables.sql"
        ).split(";");

        try (var conn = factory.getConnection()) {
            for (var query : queries) {
                try (var stmt = conn.prepareStatement(query)) {
                    stmt.executeUpdate();
                }
            }
        }

        LOG.info("All database tables have been created successfully.");
    }
}
