package com.janiwanow.flatmap.parser.impl.n1;

import com.janiwanow.flatmap.data.Space;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.jsoup.nodes.Document;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpaceExtractorSteps {
    private Document document;
    private Space space;

    @Given("the following N1 property area information")
    public void setUpDocument(DataTable data) {
        document = new Document("");
        document
            .appendElement("div")
            .addClass("card-living-content-params-list__name")
            .text("Общая площадь");

        document
            .appendElement("div")
            .text(data.column(0).get(0));

        document
            .appendElement("div")
            .addClass("card-living-content-params-list__name")
            .text("Жилая площадь");

        document
            .appendElement("div")
            .text(Optional.ofNullable(data.column(1).get(0)).orElse(""));

        document
            .appendElement("div")
            .addClass("card-living-content-params-list__name")
            .text("Кухня");

        document
            .appendElement("div")
            .text(Optional.ofNullable(data.column(2).get(0)).orElse(""));
    }

    @When("I pass the document to the space extractor")
    public void extractAreaInformation() {
        space = SpaceExtractor.extract(document, document -> 4).get();
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
