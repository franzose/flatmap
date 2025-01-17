package com.janiwanow.flatmap.realty.provider.n1;

import com.janiwanow.flatmap.realty.property.Price;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceExtractorSteps {
    private Document document;
    private Price actualPrice;

    @Given("there is an N1 offer page where property price is {string}")
    public void setUpDocument(String priceAsString) {
        document = new Document("");
        document
            .appendElement("div")
            .addClass("offer-card-header")
            .appendElement("div")
            .addClass("price")
            .text(priceAsString);
    }

    @When("I pass the document to the N1 price extractor")
    public void extractPrice() {
        actualPrice = new PriceExtractor().apply(document).get();
    }

    @Then("I must get {double} as the property price")
    public void ensureThePriceIsValid(double expectedPrice) {
        assertEquals(expectedPrice, actualPrice.amount);
        assertEquals(Currency.getInstance("RUB"), actualPrice.currency);
    }
}
