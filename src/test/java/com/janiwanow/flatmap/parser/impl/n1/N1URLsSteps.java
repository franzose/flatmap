package com.janiwanow.flatmap.parser.impl.n1;

import com.janiwanow.flatmap.parser.Pagination;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class N1URLsSteps {
    private N1URLs utility;
    private Set<String> cities;
    private int startFrom;
    private int traverse;
    private Set<URI> actualURLs;

    @Given("I wanted to get some N1 URLs for the following cities")
    public void setUpUtility(DataTable data) {
        cities = new HashSet<>(data.asList());
        utility = new N1URLs(cities);
    }

    @Given("I wanted to get some N1 URLs")
    public void setUpUtility() {
        cities = Set.of("novosibirsk", "barnaul");
        utility = new N1URLs(cities);
    }

    @And("to start parsing N1 from {int} page")
    public void setStartFrom(int startFrom) {
        this.startFrom = startFrom;
    }

    @And("to parse {int} pages of N1")
    public void setTraverse(int traverse) {
        this.traverse = traverse;
    }

    @When("I use N1URLs for that purpose")
    public void getURLs() {
        actualURLs = utility.getURLs(new Pagination(startFrom, traverse));
    }

    @Then("I must get a set of valid N1 URLs")
    public void ensureAllURLsAreValid() throws URISyntaxException {
        var expectedSize = cities.size() * traverse * N1URLs.OFFER_TYPES.size() * N1URLs.PROPERTY_TYPES.size();

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
            random.nextInt(traverse - 1) + 1
        ));

        try {
            assertTrue(actualURLs.contains(url));
        } catch (Throwable e) {
            System.out.println(url.toString());
            throw e;
        }
    }

    @Then("I must get N1 URLs from {int} to {int} page")
    public void ensureNumberOfPagesIsValid(int start, int end) {
        var queries = IntStream
            .rangeClosed(start, end)
            .mapToObj(i -> "page=" + i)
            .collect(toSet());

        var result = actualURLs
            .stream()
            .map(URI::getQuery)
            .collect(toSet())
            .containsAll(queries);

        assertTrue(result);
    }
}
