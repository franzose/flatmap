package com.janiwanow.flatmap.realty.provider.sakhcom;

import com.janiwanow.flatmap.realty.property.Price;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceExtractorSteps {
    private Document document;
    private Price actualPrice;

    @Given("there is a Sakh.com offer page where property price is {string}")
    public void setUpDocument(String price) {
        document = new Document("");
        document
            .appendElement("div")
            .attr("id", "offer")
            .appendElement("div")
            .addClass("price")
            .appendElement("div")
            .addClass("value")
            .text(price);
    }

    @When("I pass the document to the Sakh.com price extractor")
    public void extractPrice() {
        actualPrice = new PriceExtractor().apply(document).get();
    }

    @Then("I must get {double} as the Sakh.com property price")
    public void ensurePriceIsValid(double price) {
        assertEquals(actualPrice.amount, price);
    }
}
