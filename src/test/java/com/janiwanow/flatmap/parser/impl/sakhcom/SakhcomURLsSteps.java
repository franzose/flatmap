package com.janiwanow.flatmap.parser.impl.sakhcom;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public final class SakhcomURLsSteps {
    private SakhcomURLs utility;
    private int pages;
    private Set<URI> actualURLs;

    @Given("I want to get sakh.com URLs of the first {int} pages")
    public void setUpUtility(int pages) {
        this.utility = new SakhcomURLs();
        this.pages = pages;
    }

    @When("I use SakhcomURLs for that purpose")
    public void getURLs() {
        actualURLs = utility.getURLs(pages);
    }

    @Then("I must get a set of valid sakh.com URLs")
    public void ensureAllURLsAreValid() throws URISyntaxException {
        assertEquals(12 * pages, actualURLs.size());
        assertTrue(actualURLs.contains(new URI("https://dom.sakh.com/flat/sell/list1")));
        assertTrue(actualURLs.contains(new URI("https://dom.sakh.com/flat/lease/list2?s[period]=посуточно")));
    }
}
