package com.janiwanow.flatmap;

import com.janiwanow.flatmap.cli.Application;
import com.janiwanow.flatmap.cli.CommandNotFoundException;
import com.janiwanow.flatmap.db.HikariConnectionFactory;
import com.janiwanow.flatmap.db.cli.PurgeDatabaseCommand;
import com.janiwanow.flatmap.db.cli.SetupDatabaseCommand;

import java.util.Set;

/**
 * The entry point of the console application.
 */
public final class EntryPoint {
    public static void main(String[] args) throws CommandNotFoundException {
        var dbConnectionFactory = HikariConnectionFactory.getInstance();
        var app = new Application(Set.of(
            new SetupDatabaseCommand(dbConnectionFactory),
            new PurgeDatabaseCommand(dbConnectionFactory)
            // more to come
        ));

        app.run(args);
    }
}
