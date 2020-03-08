package com.janiwanow.flatmap.parser;

import com.janiwanow.flatmap.data.PropertyDetails;
import com.janiwanow.flatmap.data.Price;
import com.janiwanow.flatmap.data.Space;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyDetailsExtractorSteps {
    private static final String ADDRESS = "Nowhere City, Abandonded str. 30";
    private static final Space SPACE = new Space(30, 20, 10, 1);
    private static final Price PRICE = Price.inDollars(1000_000);
    private Document document;
    private PropertyDetailsExtractor extractor;
    private Optional<PropertyDetails> details;

    @Given("there is an HTML document of the {string} offer page")
    public void setUpDocument(String url) {
        document = new Document(url);
        extractor = new PropertyDetailsExtractor(
            document -> ADDRESS,
            document -> Optional.of(SPACE),
            document -> PRICE
        );
    }

    @Given("there is an incomplete HTML document of an offer page")
    public void setUpAnIncompleteDocument() {
        document = new Document("https://example.com");

        extractor = new PropertyDetailsExtractor(
            document -> { throw new NullPointerException(); },
            document -> Optional.of(SPACE),
            document -> PRICE
        );
    }

    @When("I pass the HTML document to the property details extractor")
    public void extract() {
        details = extractor.extract(document);
    }

    @Then("I must get valid property details")
    public void ensurePropertyDetailsAreValid() {
        assertTrue(details.isPresent());
        var data = details.get();
        assertEquals(ADDRESS, data.address);
        assertSame(SPACE, data.space);
        assertSame(PRICE, data.price);
    }

    @Then("I must get empty property details")
    public void ensurePropertyDetailsAreEmpty() {
        assertTrue(details.isEmpty());
    }
}
