package com.janiwanow.flatmap.realty.provider.sakhcom;

import com.janiwanow.flatmap.realty.data.Area;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AreaExtractorSteps {
    private Document document;
    private Area actualArea;

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

    @When("I pass the document to the Sakh.com area extractor")
    public void extractSpace() {
        actualArea = AreaExtractor.extract(document, d -> 4).get();
    }

    @Then("I must get {double} square meters of the total area from Sakh.com")
    public void ensureTotalAreaIsValid(double expectedTotalArea) {
        assertEquals(expectedTotalArea, actualArea.total);
    }

    @And("I must get {double} square meters of the living space from Sakh.com")
    public void ensureLivingSpaceIsValid(double expectedLivingSpace) {
        assertEquals(expectedLivingSpace, actualArea.living);
    }

    @And("I must get {double} square meters of the kitchen area from Sakh.com")
    public void ensureKitchenAreaIsValid(double expectedKitchenArea) {
        assertEquals(expectedKitchenArea, actualArea.kitchen);
    }
}
