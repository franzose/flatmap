package com.janiwanow.flatmap.realty.provider.sakhcom;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddressExtractorSteps {
    private Document document;
    private String expectedAddress;
    private String actualAddress;

    @Given("the following components of a property address on Sakh.com")
    public void setUpDocument(DataTable data) {
        var addressComponents = data.asList();
        expectedAddress = String.join(" ", addressComponents);

        document = new Document("");
        document
            .appendElement("div")
            .attr("id", "offer")
            .appendElement("h4")
            .html(String.format(
                "%s <a href=\"#\"><span class=\"text\">%s</span><span>/ 15 объявлений</span></a>",
                addressComponents.get(0),
                addressComponents.get(1)
            ));
    }

    @Given("just {string} as a Sakh.com property address")
    public void setUpDocument(String expectedAddress) {
        this.expectedAddress = expectedAddress;

        document = new Document("");

        var offer = document.appendElement("div").attr("id", "offer");
        offer.appendElement("h4");
        offer.appendElement("h4").text(expectedAddress);
    }

    @When("I pass the document to the Sakh.com address extractor")
    public void extractAddress() {
        actualAddress = new AddressExtractor().apply(document);
    }

    @Then("I must get that very Sakh.com property address")
    public void ensureAddressIsValid() {
        assertEquals(expectedAddress, actualAddress);
    }
}
