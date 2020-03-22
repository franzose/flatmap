package com.janiwanow.flatmap.console;

import com.janiwanow.flatmap.internal.sql.TestDbConnectionFactory;
import com.janiwanow.flatmap.realty.RelevanceCheckResult;
import com.janiwanow.flatmap.realty.RelevanceChecker;
import com.janiwanow.flatmap.realty.database.FetchURLsByChunks;
import com.janiwanow.flatmap.realty.database.MarkObsolete;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.net.URI;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public final class CheckRelevanceCommandSteps {
    private List<String> expectedObsoleteURLs;

    @And("the following property offers have become obsolete")
    public void setUpObsoleteProperties(DataTable data) {
        expectedObsoleteURLs = data.asList();
    }

    @When("I run the CheckRelevanceCommand command")
    public void runCommand() {
        // TODO: refactor this to a reusable "run ... command" step
        new CheckRelevanceCommand(
            new FetchURLsByChunks(TestDbConnectionFactory.INSTANCE),
            new MarkObsolete(TestDbConnectionFactory.INSTANCE),
            Set.of(new RelevanceChecker() {
                @Override
                public RelevanceCheckResult check(URI url) {
                    return expectedObsoleteURLs.contains(url.toString())
                        ? RelevanceCheckResult.obsolete(url)
                        : RelevanceCheckResult.relevant(url);
                }

                @Override
                public boolean supports(URI url) {
                    return true;
                }
            })
        ).execute();
    }

    @Then("the following property offers must be marked obsolete")
    public void ensureObsolete(DataTable data) throws SQLException {
        assertRelevance(data.asList(), Assertions::assertNotNull);
    }

    @And("the following property offers must be left unchanged")
    public void ensureUnchanged(DataTable data) throws SQLException {
        assertRelevance(data.asList(), Assertions::assertNull);
    }

    private static String generatePlaceholders(int number) {
        var placeholders = "?,".repeat(number);
        return placeholders.substring(0, placeholders.length() - 1);
    }

    private static void assertRelevance(List<String> urls, Consumer<Timestamp> consumer) throws SQLException {
        var sql = "SELECT * FROM property WHERE offer_url IN (" + generatePlaceholders(urls.size()) + ")";

        try (
            var conn = TestDbConnectionFactory.INSTANCE.getConnection();
            var stmt = conn.prepareStatement(sql)
        ) {
            for (var idx = 1; idx <= urls.size(); idx++) {
                stmt.setString(idx, urls.get(idx - 1));
            }

            try (var result = stmt.executeQuery()) {
                while (result.next()) {
                    consumer.accept(result.getTimestamp("invalidated_at"));
                }
            }
        }
    }
}
