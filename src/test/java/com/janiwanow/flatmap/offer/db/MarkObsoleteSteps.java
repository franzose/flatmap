package com.janiwanow.flatmap.offer.db;

import com.janiwanow.flatmap.db.TestConnectionFactory;
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
        affectedRows = new MarkObsolete(TestConnectionFactory.INSTANCE).apply(context.getUrls());
    }

    @Then("{int} properties must be marked obsolete")
    public void ensureObsolete(int expectedRows) {
        assertEquals(expectedRows, affectedRows);
    }
}
