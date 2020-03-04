package com.janiwanow.flatmap.parser.impl.n1;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class N1URLsSteps {
    private N1URLs utility;
    private Set<String> cities;
    private int pages;
    private Set<URI> actualURLs;

    @Given("I want to get URLs of the first {int} pages in the following cities")
    public void setUpUtility(int pages, DataTable data) {
        this.pages = pages;
        cities = new HashSet<>(data.asList());
        utility = new N1URLs(cities);
    }

    @When("I use N1URLs for that purpose")
    public void getURLs() {
        actualURLs = utility.getURLs(pages);
    }

    @Then("I must get a set of valid N1 URLs")
    public void ensureAllURLsAreValid() throws URISyntaxException {
        var expectedSize = cities.size() * pages * N1URLs.OFFER_TYPES.size() * N1URLs.PROPERTY_TYPES.size();

        assertEquals(expectedSize, actualURLs.size());

        var random = new Random();
        var randomCity = cities.toArray(new String[0])[random.nextInt(cities.size())];
        var randomOfferType = N1URLs.OFFER_TYPES.toArray(new String[0])[random.nextInt(N1URLs.OFFER_TYPES.size())];
        var randomPropertyType = N1URLs.PROPERTY_TYPES.toArray(new String[0])[random.nextInt(N1URLs.PROPERTY_TYPES.size())];
        var url = new URI(String.format(
            N1URLs.PATTERN,
            randomCity,
            randomOfferType,
            randomPropertyType,
            random.nextInt(pages - 1) + 1
        ));

        try {
            assertTrue(actualURLs.contains(url));
        } catch (Throwable e) {
            System.out.println(url.toString());
            throw e;
        }

    }
}
