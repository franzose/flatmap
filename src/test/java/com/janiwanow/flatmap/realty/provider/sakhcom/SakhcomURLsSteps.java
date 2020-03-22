package com.janiwanow.flatmap.realty.provider.sakhcom;

import com.janiwanow.flatmap.realty.Pagination;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SakhcomURLsSteps {
    private SakhcomURLs utility;
    private Set<URI> actualURLs;
    private int startFrom;
    private int traverse;

    @Given("I wanted to get some URLs from sakh.com")
    public void setUpUtility() {
        this.utility = new SakhcomURLs();
    }

    @And("to start parsing sakh.com from {int} page")
    public void setStartFrom(int startFrom) {
        this.startFrom = startFrom;
    }

    @And("to parse {int} pages of sakh.com")
    public void setTraverse(int traverse) {
        this.traverse = traverse;
    }

    @When("I use SakhcomURLs for that purpose")
    public void getURLs() {
        System.out.println(startFrom);
        System.out.println(traverse);
        actualURLs = utility.getURLs(new Pagination(startFrom, traverse));
    }

    @Then("I must get a set of valid sakh.com URLs")
    public void ensureAllURLsAreValid() throws URISyntaxException {
        assertEquals(9 * traverse, actualURLs.size());
        assertTrue(actualURLs.contains(new URI("https://dom.sakh.com/flat/sell/list1")));
        assertTrue(actualURLs.contains(new URI("https://dom.sakh.com/flat/lease/list2?s[period]=посуточно")));
    }

    @Then("I must get sakh.com URLs from {int} to {int} page")
    public void ensureNumberOfPagesIsValid(int start, int end) {
        var params = IntStream
            .rangeClosed(start, end)
            .mapToObj(i -> "list" + i)
            .collect(toSet());

        var result = actualURLs
            .stream()
            .map(SakhcomURLsSteps::getListParameter)
            .collect(toSet())
            .containsAll(params);

        assertTrue(result);
    }

    private static String getListParameter(URI url) {
        var str = url.toString();
        var indexOfList = str.lastIndexOf("list");

        return str.contains("?")
            ? str.substring(indexOfList, str.indexOf('?'))
            : str.substring(indexOfList);
    }
}
