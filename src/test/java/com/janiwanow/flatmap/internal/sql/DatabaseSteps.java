package com.janiwanow.flatmap.internal.sql;

import com.janiwanow.flatmap.console.SetupDatabaseCommand;
import com.janiwanow.flatmap.console.TruncateTablesCommand;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

public final class DatabaseSteps {
    @Before("@db")
    public void resetDatabase() {
        new TruncateTablesCommand(TestDbConnectionFactory.INSTANCE).execute();
    }

    @BeforeStep
    public void beforeStep(Scenario scenario) {
        var tags = scenario.getSourceTagNames();

        if (tags.contains("@db") && tags.contains("@outline")) {
            resetDatabase();
        }
    }

    @After("@db_purger")
    public void recoverDatabase() {
        new SetupDatabaseCommand(TestDbConnectionFactory.INSTANCE).execute();
    }
}
