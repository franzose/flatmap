package com.janiwanow.flatmap;

import static com.janiwanow.flatmap.db.ConnectionFactory.connect;
import static com.janiwanow.flatmap.db.DatabaseSetup.createTablesIfNotCreated;

/**
 * The entry point of the console application.
 */
public class EntryPoint {
    public static void main(String[] args) {
        try (var connection = connect()) {
            createTablesIfNotCreated(connection);

            // TODO: parsing, persisting, exporting to JSON for the frontend
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
