package com.janiwanow.flatmap.realty.provider.sakhcom;

import com.janiwanow.flatmap.internal.http.document_fetcher.FakeHttpConnection;
import com.janiwanow.flatmap.realty.PropertyDetailsExtractor;
import com.janiwanow.flatmap.realty.RelevanceCheckResult;
import com.janiwanow.flatmap.realty.property.Area;
import com.janiwanow.flatmap.realty.property.Price;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SakhcomRelevanceCheckerSteps {
    private PropertyDetailsExtractor extractor = new PropertyDetailsExtractor(
        d -> "foo bar",
        d -> Optional.of(new Area(10, 5, 5, 1)),
        d -> Price.inRubles(1_000_000)
    );

    private SakhcomRelevanceChecker checker;
    private RelevanceCheckResult result;

    @Given("I got an active Sakh.com property offer")
    public void setUpActiveOffer() {
        var connection = new FakeHttpConnection(uri -> Optional.of(new Document(uri.toString())));
        checker = new SakhcomRelevanceChecker(connection, extractor);
    }

    @Given("there were no Sakh.com property offer")
    public void setUpEmptyOffer() {
        var connection = new FakeHttpConnection(uri -> Optional.empty());
        checker = new SakhcomRelevanceChecker(connection, extractor);
    }

    @Given("I got {string} within a Sakh.com property offer")
    public void setUpObsoleteOffer(String title) {
        var document = new Document("");
        document.html(title);

        var connection = new FakeHttpConnection(uri -> Optional.of(document));
        checker = new SakhcomRelevanceChecker(connection, extractor);
    }

    @When("I check it using Sakh.com offer relevance checker")
    public void check() throws URISyntaxException {
        result = checker.check(new URI("https://example.com"));
    }

    @Then("the Sakh.com property offer must be relevant")
    public void ensureOfferIsRelevant() {
        assertTrue(result.isRelevant);
    }

    @Then("the Sakh.com property offer must be obsolete")
    public void ensureOfferIsObsolete() {
        assertTrue(result.isObsolete);
    }
}
