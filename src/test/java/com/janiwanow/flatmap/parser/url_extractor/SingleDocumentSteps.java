package com.janiwanow.flatmap.parser.url_extractor;

import com.janiwanow.flatmap.parser.URLExtractor;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SingleDocumentSteps {
    private Document document;
    private URL href;
    private Set<URL> extractedURLs;

    @Given("we fetched an HTML document from {string}")
    public void anHtmlDocument(String href) throws MalformedURLException {
        this.href = new URL(href);

        document = new Document(href);
        document
            .appendElement("div")
            .attr("class", "foo")
            .appendElement("div")
            .attr("class", "bar");
    }

    @And("it contains a list of links")
    public void itContainsListOfLinks(DataTable links) {
        var container = document.selectFirst(".foo > .bar");

        for (var url : links.asList()) {
            container
                .appendElement("a")
                .attr("href", url);
        }
    }

    @When("the extractor takes the document")
    public void theExtractorTakesTheDocument() {
        extractedURLs = URLExtractor.extract(document, ".foo > .bar > a");
    }

    @Then("it should return a list of absolute URLs of the following paths")
    public void itShouldReturnListOfURLs(DataTable paths) {
        var pathList = paths.asList();

        assertEquals(pathList.size(), extractedURLs.size());

        var origin = Origin.getOrigin(href);
        var urls = pathList.stream().map(path -> origin + path).collect(toList());

        assertTrue(extractedURLs.stream().allMatch(url -> urls.contains(url.toString())));
    }
}
