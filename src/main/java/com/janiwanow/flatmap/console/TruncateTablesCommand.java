package com.janiwanow.flatmap.console;

import com.beust.jcommander.Parameters;
import com.janiwanow.flatmap.internal.console.Command;
import com.janiwanow.flatmap.internal.sql.DbConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Objects;

@Parameters(commandNames = "db:truncate", commandDescription = "Used to truncate all database tables")
public final class TruncateTablesCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(TruncateTablesCommand.class);
    private final DbConnectionFactory factory;

    public TruncateTablesCommand(DbConnectionFactory factory) {
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
            var sql =
                "DO $$ DECLARE\n" +
                "r RECORD;\n" +
                "BEGIN\n" +
                "FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP\n" +
                "  EXECUTE 'TRUNCATE TABLE ' || quote_ident(r.tablename) || ' CASCADE';\n" +
                "END LOOP;\n" +
                "END\n" +
                "$$;";

            stmt.executeUpdate(sql);

            LOG.info("Database tables have been truncated successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
