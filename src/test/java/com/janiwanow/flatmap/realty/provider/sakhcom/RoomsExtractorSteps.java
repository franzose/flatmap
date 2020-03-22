package com.janiwanow.flatmap.realty.provider.sakhcom;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomsExtractorSteps {
    private Document document;
    private int actual;

    @Given("there is a Sakh.com offer page where rooms are {string}")
    public void setUpDocument(String rooms) {
        document = new Document("");

        var offer = document
            .appendElement("div")
            .attr("id", "offer");

        offer.appendElement("h1");
        offer.appendElement("h3").text(rooms);
    }

    @Given("there is a Sakh.com offer page without room information")
    public void setUpDocument() {
        document = new Document("");

        var offer = document
            .appendElement("div")
            .attr("id", "offer");

        offer.appendElement("h1");
        offer.appendElement("h3");
    }

    @When("I pass the document to the Sakh.com rooms extractor")
    public void extractTheNumberOfRooms() {
        actual = new RoomsExtractor().apply(document);
    }

    @Then("I must get {int} as the number of rooms of the Sakh.com offer")
    public void ensureTheNumberOfRoomsIsValid(int expected) {
        assertEquals(expected, actual);
    }
}
