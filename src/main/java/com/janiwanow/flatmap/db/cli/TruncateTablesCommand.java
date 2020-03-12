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

@Parameters(commandNames = "db:truncate", commandDescription = "Used to truncate all database tables")
public final class TruncateTablesCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(TruncateTablesCommand.class);
    private static final String TRUNCATE_TABLES_QUERY;

    static {
        try {
            TRUNCATE_TABLES_QUERY = ResourceFile.readToString(TruncateTablesCommand.class, "truncate_tables.sql");
        } catch (IOException e) {
            throw new IllegalStateException("Could not read SQL query from file", e);
        }
    }

    private final ConnectionFactory factory;

    public TruncateTablesCommand(ConnectionFactory factory) {
        Objects.requireNonNull(factory, "SQL connection factory must not be null");
        this.factory = factory;
    }

    @Override
    public void execute() {
        LOG.info("Start truncating database tables...");

        try (
            var conn = factory.getConnection();
            var stmt = conn.createStatement()
        ) {
            stmt.executeUpdate(TRUNCATE_TABLES_QUERY);

            LOG.info("Database tables have been truncated successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
