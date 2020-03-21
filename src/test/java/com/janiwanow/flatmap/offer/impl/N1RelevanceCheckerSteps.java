package com.janiwanow.flatmap.offer.impl;

import com.janiwanow.flatmap.data.Area;
import com.janiwanow.flatmap.data.Price;
import com.janiwanow.flatmap.internal.http.document_fetcher.FakeHttpConnection;
import com.janiwanow.flatmap.offer.RelevanceCheckResult;
import com.janiwanow.flatmap.parser.PropertyDetailsExtractor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public final class N1RelevanceCheckerSteps {
    private PropertyDetailsExtractor extractor = new PropertyDetailsExtractor(
        d -> "foo bar",
        d -> Optional.of(new Area(10, 5, 5, 1)),
        d -> Price.inRubles(1_000_000)
    );

    private N1RelevanceChecker checker;
    private RelevanceCheckResult result;

    @Given("I got an active N1 property offer")
    public void setUpActiveOffer() {
        var connection = new FakeHttpConnection(uri -> Optional.of(new Document(uri.toString())));
        checker = new N1RelevanceChecker(connection, extractor);
    }

    @Given("there were no N1 property offer")
    public void setUpEmptyOffer() {
        var connection = new FakeHttpConnection(uri -> Optional.empty());
        checker = new N1RelevanceChecker(connection, extractor);
    }

    @When("I check it using N1 offer relevance checker")
    public void check() throws URISyntaxException {
        result = checker.check(new URI("https://example.com"));
    }

    @Then("the N1 property offer must be relevant")
    public void ensureOfferIsRelevant() {
        assertTrue(result.isRelevant);
    }

    @Then("the N1 property offer must be obsolete")
    public void ensureOfferIsObsolete() {
        assertTrue(result.isObsolete);
    }
}
