package com.janiwanow.flatmap;

import com.janiwanow.flatmap.cli.Application;
import com.janiwanow.flatmap.db.ConnectionFactory;
import com.janiwanow.flatmap.db.SetupDatabaseCommand;

import java.sql.SQLException;
import java.util.Set;

/**
 * The entry point of the console application.
 */
public final class EntryPoint {
    public static void main(String[] args) throws SQLException {
        try (var connection = ConnectionFactory.connect()) {
            var app = new Application(Set.of(
                new SetupDatabaseCommand(connection)
                // more to come
            ));

            app.run(args);
        }
    }
}
