package com.janiwanow.flatmap.parser.impl.sakhcom;

import com.janiwanow.flatmap.data.Price;
import com.janiwanow.flatmap.data.PropertyDetails;
import com.janiwanow.flatmap.data.Space;
import com.janiwanow.flatmap.http.DocumentFetcher;
import com.janiwanow.flatmap.http.JsoupHttpConnection;
import com.janiwanow.flatmap.parser.PropertyDetailsExtractor;
import com.janiwanow.flatmap.parser.PropertyDetailsFetcher;
import com.janiwanow.flatmap.util.Numbers;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

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
    public void setUp(String city) throws URISyntaxException {
        this.city = city;
        this.url = toAbsoluteURL(PATH);

        prepareOfferList();
        prepareOfferPages();
    }

    private void prepareOfferList() {
        var document = new Document(url.toString());
        var offers = document.appendElement("div").addClass("offers");

        for (var idx = 1; idx <= 4; idx++) {
            offers.appendElement("a").attr("href", "/offer" + idx);
        }

        stubFor(get(PATH)
            .withCookie("city", matching(city))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/html")
                .withBody(document.html())
            )
        );
    }

    private void prepareOfferPages() {
        for (var idx = 1; idx <= 4; idx++) {
            var path = "/offer" + idx;
            var document = new Document(path);
            // we don't need to repeat the original document structure
            // since the purpose of the test is to check
            // whether "city" cookie is applied properly
            var offer = document.appendElement("div").attr("id", "offer");
            offer.appendElement("div").addClass("address").text("Корсаков, ул. Советская, 26");
            offer.appendElement("div").addClass("total-area").text("30");
            offer.appendElement("div").addClass("living-space").text("20");
            offer.appendElement("div").addClass("kitchen-area").text("10");
            offer.appendElement("div").addClass("rooms").text("1");
            offer.appendElement("div").addClass("price").text("1 000 000 руб.");

            stubFor(get(path)
                .withCookie("city", matching(city))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "text/html")
                    .withBody(document.html())
                )
            );
        }
    }

    @When("I pass the city to the Sakh.com city fetcher")
    public void fetchFromCity() {
        var fetcher = new SakhcomCityFetcher(
            Set.of(url),
            JsoupHttpConnection.builder(),
            connection -> new PropertyDetailsFetcher(
                new DocumentFetcher(connection),
                // we don't need to repeat the original document structure
                // since the purpose of the test is to check
                // whether "city" cookie is applied properly
                new PropertyDetailsExtractor(
                    document -> document.selectFirst("#offer .address").text(),
                    document -> Optional.of(new Space(
                        Numbers.parseDouble(document.selectFirst("#offer .total-area").text()),
                        Numbers.parseDouble(document.selectFirst("#offer .living-space").text()),
                        Numbers.parseDouble(document.selectFirst("#offer .kitchen-area").text()),
                        Numbers.parseInt(document.selectFirst("#offer .rooms").text())
                    )),
                    document -> Price.inRubles(Numbers.parseDouble(document.selectFirst("#offer .price").text()))
                ),
                ".offers > a"
            )
        );

        propertyDetails = fetcher.fetchFromCity(city).join();
    }

    @Then("I must get a set of property details from that city")
    public void ensure() {
        assertEquals(4, propertyDetails.size());
    }
}
