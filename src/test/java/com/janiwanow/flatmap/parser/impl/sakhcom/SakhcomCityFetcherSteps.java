package com.janiwanow.flatmap.parser.impl.sakhcom;

import com.janiwanow.flatmap.data.Price;
import com.janiwanow.flatmap.data.PropertyDetails;
import com.janiwanow.flatmap.data.Area;
import com.janiwanow.flatmap.http.Delay;
import com.janiwanow.flatmap.http.DocumentFetcher;
import com.janiwanow.flatmap.http.JsoupHttpConnection;
import com.janiwanow.flatmap.parser.PropertyDetailsExtractor;
import com.janiwanow.flatmap.parser.PropertyDetailsFetcher;
import com.janiwanow.flatmap.util.Numbers;
import com.janiwanow.flatmap.util.ResourceFile;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.janiwanow.flatmap.WireMockPathToURL.toAbsoluteURL;
import static org.junit.jupiter.api.Assertions.*;

public class SakhcomCityFetcherSteps {
    private static final String PATH = "/sakhcom";
    private String city;
    private URI url;
    private Set<PropertyDetails> propertyDetails;

    @Given("I want to get property details from {string}")
    public void setUp(String city) throws URISyntaxException, IOException {
        this.city = city;
        this.url = toAbsoluteURL(PATH);

        prepareOfferList();
        prepareOfferPages();
    }

    private void prepareOfferList() {
        var document = new Document(url.toString());
        var offers = document.appendElement("div").addClass("offers");

        for (var idx = 1; idx <= 4; idx++) {
            offers.appendElement("div")
                .addClass("item")
                .appendElement("div")
                .addClass("content")
                .appendElement("a")
                .attr("href", "/offer" + idx);
        }

        stubFor(get(PATH)
            .withCookie("city", matching(city))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/html")
                .withBody(document.html())
            )
        );
    }

    private void prepareOfferPages() throws IOException {
        var content = ResourceFile.readToString(SakhcomCityFetcherSteps.class, "offer.html");

        for (var idx = 1; idx <= 4; idx++) {
            stubFor(get("/offer" + idx)
                .withCookie("city", matching(city))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "text/html")
                    .withBody(content)
                )
            );
        }
    }

    @When("I pass the city to the Sakh.com city fetcher")
    public void fetchFromCity() {
        var fetcher = new SakhcomCityFetcher(
            Set.of(url),
            JsoupHttpConnection.builder(),
            new Delay(1, 1)
        );

        propertyDetails = fetcher.fetchFromCity(city).join();
    }

    @Then("I must get a set of property details from that city")
    public void ensure() {
        assertEquals(4, propertyDetails.size());
    }
}
