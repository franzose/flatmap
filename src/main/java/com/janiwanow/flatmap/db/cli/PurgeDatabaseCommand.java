package com.janiwanow.flatmap.db.cli;

import com.beust.jcommander.Parameters;
import com.janiwanow.flatmap.cli.Command;
import com.janiwanow.flatmap.util.ResourceFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

@Parameters(commandNames = "db:purge")
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

    private final Connection connection;

    public PurgeDatabaseCommand(Connection connection) {
        Objects.requireNonNull(connection, "SQL connection must not be null.");
        this.connection = connection;
    }

    @Override
    public void execute() {
        LOG.info("Start purging database...");

        try (var stmt = connection.createStatement()) {
            stmt.executeUpdate(DROP_TABLES_QUERY);

            LOG.info("Database has been purged successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
