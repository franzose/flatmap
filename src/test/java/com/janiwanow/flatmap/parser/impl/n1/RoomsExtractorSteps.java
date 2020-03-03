package com.janiwanow.flatmap.parser.impl.n1;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomsExtractorSteps {
    private Document document;
    private int actualNumberOfRooms;

    @Given("there is an N1 offer page entitled {string}")
    public void setUpDocument(String title) {
        document = new Document("");
        document
            .appendElement("div")
            .addClass("offer-card-header")
            .appendElement("div")
            .addClass("deal-title")
            .text(title);
    }

    @When("I pass the document to the N1 rooms extractor")
    public void extractTheNumberOfRooms() {
        actualNumberOfRooms = RoomsExtractor.extract(document);
    }

    @Then("I must get {int} as the number of rooms")
    public void ensureTheActualNumberOfRoomsIsValid(int expectedNumberOfRooms) {
        assertEquals(expectedNumberOfRooms, actualNumberOfRooms);
    }
}
