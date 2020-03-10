package com.janiwanow.flatmap.db;

import com.janiwanow.flatmap.db.cli.TruncateTablesCommand;
import io.cucumber.java.Before;

public final class DatabaseSteps {
    @Before("@db")
    public void resetDatabase() {
        new TruncateTablesCommand(TestConnectionFactory.getInstance()).execute();
    }
}
