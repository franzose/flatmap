package com.janiwanow.flatmap.realty.provider.n1;

import io.cucumber.java.en.And;
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

    @Given("there is an N1 offer page")
    public void setUpDocument() {
        document = new Document("");
    }

    @And("that offer has {int} rooms listed among other parameters")
    public void setUpRooms(int rooms) {
        document.appendElement("div").addClass("card-living-content-params-list__name").text("Комнат");
        document.appendElement("div").text(String.valueOf(rooms));
    }

    @When("I pass the document to the N1 rooms extractor")
    public void extractTheNumberOfRooms() {
        actualNumberOfRooms = new RoomsExtractor().apply(document);
    }

    @Then("I must get {int} rooms for N1 property details")
    public void ensureTheActualNumberOfRoomsIsValid(int expectedNumberOfRooms) {
        assertEquals(expectedNumberOfRooms, actualNumberOfRooms);
    }
}
