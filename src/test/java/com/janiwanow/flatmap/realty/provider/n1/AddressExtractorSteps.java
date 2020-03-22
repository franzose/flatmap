package com.janiwanow.flatmap.realty.provider.n1;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddressExtractorSteps {
    private Document document;
    private String actualAddress;

    @Given("there is an N1 offer page with the following property address components")
    public void setUpDocument(DataTable data) {
        document = new Document("");

        var container = document.createElement("div").addClass("offer-card-geo-tags");

        for (var component : data.asList()) {
            var element = document.createElement("span").addClass("ui-kit-link__inner").text(component);
            container.appendChild(element);
        }

        document.appendChild(container);
    }

    @When("I pass the document to the N1 address extractor")
    public void extractAddress() {
        actualAddress = AddressExtractor.extract(document);
    }

    @Then("I must get {string} as the N1 property address")
    public void ensureAddressIsValid(String expectedAddress) {
        assertEquals(expectedAddress, actualAddress);
    }
}
