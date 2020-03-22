package com.janiwanow.flatmap.realty.database;

import com.janiwanow.flatmap.internal.sql.TestDbConnectionFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MarkObsoleteSteps {
    private final RelevanceTestSteps context;
    private int affectedRows;

    public MarkObsoleteSteps(RelevanceTestSteps context) {
        this.context = context;
    }

    @When("I mark those property details obsolete")
    public void markObsolete() {
        affectedRows = new MarkObsolete(TestDbConnectionFactory.INSTANCE).apply(context.getUrls());
    }

    @Then("{int} properties must be marked obsolete")
    public void ensureObsolete(int expectedRows) {
        assertEquals(expectedRows, affectedRows);
    }
}
