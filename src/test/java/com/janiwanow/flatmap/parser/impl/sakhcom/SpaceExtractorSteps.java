package com.janiwanow.flatmap.parser.impl.sakhcom;

import com.janiwanow.flatmap.data.Space;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpaceExtractorSteps {
    private Document document;
    private Space actualSpace;

    @Given("the {string} as property area on Sakh.com")
    public void setUpDocument(String area) {
        document = new Document("");
        document
            .appendElement("div")
            .attr("id", "offer")
            .appendElement("div")
            .addClass("area")
            .text(area);
    }

    @When("I pass the document to the Sakh.com space extractor")
    public void extractSpace() {
        actualSpace = SpaceExtractor.extract(document, d -> 4);
    }

    @Then("I must get {double} square meters of the total area from Sakh.com")
    public void ensureTotalAreaIsValid(double expectedTotalArea) {
        assertEquals(expectedTotalArea, actualSpace.total);
    }

    @And("I must get {double} square meters of the living space from Sakh.com")
    public void ensureLivingSpaceIsValid(double expectedLivingSpace) {
        assertEquals(expectedLivingSpace, actualSpace.living);
    }

    @And("I must get {double} square meters of the kitchen area from Sakh.com")
    public void ensureKitchenAreaIsValid(double expectedKitchenArea) {
        assertEquals(expectedKitchenArea, actualSpace.kitchen);
    }
}
