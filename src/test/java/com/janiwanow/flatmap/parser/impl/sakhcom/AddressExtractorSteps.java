package com.janiwanow.flatmap.parser.impl.sakhcom;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddressExtractorSteps {
    private Document document;
    private String actualAddress;

    @Given("there is a Sakh.com offer page with the following property address components")
    public void setUpDocument(DataTable data) {
        var addressComponents = data.asList();

        document = new Document("");
        document
            .appendElement("div")
            .attr("id", "offer")
            .appendElement("h4")
            .html(String.format(
                "%s <a href=\"\"><span class=\"text\">%s</span><span>/ 15 объявлений</span></a>",
                addressComponents.get(0),
                addressComponents.get(1)
            ));
    }

    @When("I pass the document to the Sakh.com address extractor")
    public void extractAddress() {
        actualAddress = AddressExtractor.extract(document);
    }

    @Then("I must get {string} as the Sakh.com property address")
    public void ensureAddressIsValid(String expectedAddress) {
        assertEquals(expectedAddress, actualAddress);
    }
}
