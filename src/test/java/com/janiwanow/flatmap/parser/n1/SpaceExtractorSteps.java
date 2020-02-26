package com.janiwanow.flatmap.parser.n1;

import com.janiwanow.flatmap.data.Space;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.jsoup.nodes.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpaceExtractorSteps {
    private Document document;
    private Space space;

    @Given("the following apartment area information")
    public void setUpDocument(DataTable data) {
        document = new Document("");
        document
            .appendElement("div")
            .addClass("offer-card-header")
            .appendElement("div")
            .addClass("deal-title")
            .text("продам 3-к");

        var factoids = document.appendElement("div").addClass("offer-card-factoids");

        for (var value : data.asList()) {
            factoids.appendElement("div").addClass("text").text(value);
        }

        document.appendChild(factoids);
    }

    @When("I pass the document to the space extractor")
    public void extractAreaInformation() {
        space = SpaceExtractor.extract(document);
    }

    @Then("I must get {int} square meters of the total area")
    public void ensureTotalAreaIsValid(int expectedTotalArea) {
        assertEquals(expectedTotalArea, space.total);
    }

    @And("I must get {int} square meters of the living space")
    public void ensureLivingSpaceIsValid(int expectedLivingSpace) {
        assertEquals(expectedLivingSpace, space.living);
    }

    @And("I must get {int} square meters of the kitchen area")
    public void ensureKitchenAreaIsValid(int expectedKitchenArea) {
        assertEquals(expectedKitchenArea, space.kitchen);
    }
}
