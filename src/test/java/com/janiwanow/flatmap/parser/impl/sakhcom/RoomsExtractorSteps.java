package com.janiwanow.flatmap.parser.impl.sakhcom;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomsExtractorSteps {
    private Document document;
    private int actual;

    @Given("there is an Sakh.com offer page where rooms are {string}")
    public void setUpDocument(String rooms) {
        document = new Document("");
        document
            .appendElement("div")
            .attr("id", "offer")
            .appendElement("h3")
            .text(rooms);
    }

    @When("I pass the document to the Sakh.com rooms extractor")
    public void extractTheNumberOfRooms() {
        actual = RoomsExtractor.extract(document);
    }

    @Then("I must get {int} as the number of rooms of the Sakh.com offer")
    public void ensureTheNumberOfRoomsIsValid(int expected) {
        assertEquals(expected, actual);
    }
}
