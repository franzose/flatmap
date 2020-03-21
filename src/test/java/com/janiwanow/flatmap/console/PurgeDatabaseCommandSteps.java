package com.janiwanow.flatmap.console;

import com.janiwanow.flatmap.db.TestConnectionFactory;
import com.janiwanow.flatmap.internal.console.ApplicationContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class PurgeDatabaseCommandSteps {
    private ApplicationContext context;

    public PurgeDatabaseCommandSteps(ApplicationContext context) {
        this.context = context;
    }

    @Given("the database had the following tables")
    public void createDatabaseTables(DataTable data) throws SQLException {
        try (var conn = TestConnectionFactory.INSTANCE.getConnection()) {
            for (var table : data.asList()) {
                try (var stmt = conn.createStatement()) {
                    stmt.executeUpdate("CREATE TABLE " + table + " ()");
                }
            }
        }
    }

    @And("I registered the \"db:purge\" command in the console application")
    public void setUpApplication() {
        context.setUpApplication(new PurgeDatabaseCommand(TestConnectionFactory.INSTANCE));
    }

    @Then("the database must be empty")
    public void ensureDatabaseIsEmpty() throws SQLException {
        var sql = "SELECT COUNT(*) FROM pg_tables WHERE schemaname = current_schema()";

        try (
            var conn = TestConnectionFactory.INSTANCE.getConnection();
            var result = conn.prepareStatement(sql).executeQuery()
        ) {
            result.next();

            assertEquals(0, result.getInt(1));
        }
    }
}
