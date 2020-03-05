package com.janiwanow.flatmap.parser;

import com.janiwanow.flatmap.data.ApartmentInfo;
import com.janiwanow.flatmap.data.Price;
import com.janiwanow.flatmap.data.Space;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ApartmentInfoExtractorSteps {
    private static final String ADDRESS = "Nowhere City, Abandonded str. 30";
    private static final Space SPACE = new Space(30, 20, 10, 1);
    private static final Price PRICE = Price.inDollars(1000_000);
    private Document document;
    private ApartmentInfoExtractor extractor;
    private Optional<ApartmentInfo> info;

    @Given("there is an HTML document of the {string} offer page")
    public void setUpDocument(String url) {
        document = new Document(url);
        extractor = new ApartmentInfoExtractor(
            document -> ADDRESS,
            document -> SPACE,
            document -> PRICE
        );
    }

    @Given("there is an incomplete HTML document of an offer page")
    public void setUpAnIncompleteDocument() {
        document = new Document("https://example.com");

        var random = new Random();
        extractor = new ApartmentInfoExtractor(
            document -> {
                if (random.nextBoolean()) {
                    throw new NullPointerException();
                }
                return ADDRESS;
            },
            document -> {
                if (random.nextBoolean()) {
                    throw new NullPointerException();
                }
                return SPACE;
            },
            document -> {
                if (random.nextBoolean()) {
                    throw new NullPointerException();
                }
                return PRICE;
            }
        );
    }

    @When("I pass the HTML document to the apartment information extractor")
    public void extract() {
        info = extractor.extract(document);
    }

    @Then("I must get a valid apartment information")
    public void ensureApartmentInfoIsValid() {
        assertTrue(info.isPresent());
        var data = info.get();
        assertEquals(ADDRESS, data.address);
        assertSame(SPACE, data.space);
        assertSame(PRICE, data.price);
    }

    @Then("I must get an empty apartment information")
    public void ensureApartmentInfoIsEmpty() {
        assertTrue(info.isEmpty());
    }
}
