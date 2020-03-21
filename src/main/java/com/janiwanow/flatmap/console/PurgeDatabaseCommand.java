package com.janiwanow.flatmap.console;

import com.beust.jcommander.Parameters;
import com.janiwanow.flatmap.internal.console.Command;
import com.janiwanow.flatmap.db.ConnectionFactory;
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
            var sql =
                "DO $$ DECLARE\n" +
                "r RECORD;\n" +
                "BEGIN\n" +
                "FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP\n" +
                "  EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';\n" +
                "END LOOP;\n" +
                "END $$;";

            stmt.executeUpdate(sql);

            LOG.info("Database has been purged successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
