package com.janiwanow.flatmap.db;

import com.janiwanow.flatmap.db.cli.TruncateTablesCommand;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

public final class DatabaseSteps {
    @Before("@db")
    public void resetDatabase() {
        new TruncateTablesCommand(TestConnectionFactory.getInstance()).execute();
    }

    @BeforeStep
    public void beforeStep(Scenario scenario) {
        var tags = scenario.getSourceTagNames();

        if (tags.contains("@db") && tags.contains("@outline")) {
            resetDatabase();
        }
    }
}
