package com.janiwanow.flatmap.db.cli;

import com.beust.jcommander.Parameters;
import com.janiwanow.flatmap.cli.Command;
import com.janiwanow.flatmap.db.ConnectionFactory;
import com.janiwanow.flatmap.util.ResourceFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Command used to drop all database tables.
 */
@Parameters(commandNames = "db:purge", commandDescription = "Used to drop all database tables")
public class PurgeDatabaseCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(PurgeDatabaseCommand.class);
    private static final String DROP_TABLES_QUERY;

    static {
        try {
            DROP_TABLES_QUERY = ResourceFile.readToString("drop_tables.sql");
        } catch (IOException e) {
            throw new IllegalStateException("Could not read SQL query from file", e);
        }
    }

    private final ConnectionFactory factory;

    public PurgeDatabaseCommand(ConnectionFactory factory) {
        Objects.requireNonNull(factory, "SQL connection factory must not be null.");
        this.factory = factory;
    }

    @Override
    public void execute() {
        LOG.info("Start purging database...");

        try (
            var conn = factory.getConnection();
            var stmt = conn.createStatement()
        ) {
            stmt.executeUpdate(DROP_TABLES_QUERY);

            LOG.info("Database has been purged successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
