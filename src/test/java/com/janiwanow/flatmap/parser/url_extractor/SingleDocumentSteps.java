package com.janiwanow.flatmap.parser.url_extractor;

import com.janiwanow.flatmap.parser.URLExtractor;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SingleDocumentSteps {
    private Document document;
    private URI href;
    private Set<URI> extractedURLs;

    @Given("I fetched an HTML document from {string}")
    public void createDocument(String href) throws URISyntaxException {
        this.href = new URI(href);

        document = new Document(href);
        document
            .appendElement("div")
            .attr("class", "foo")
            .appendElement("div")
            .attr("class", "bar");
    }

    @And("it contains the following links")
    public void addLinks(DataTable links) {
        var container = document.selectFirst(".foo > .bar");

        for (var url : links.asList()) {
            container
                .appendElement("a")
                .attr("href", url);
        }
    }

    @When("I pass the document to the extractor")
    public void extractURLs() {
        extractedURLs = URLExtractor.extract(document, ".foo > .bar > a");
    }

    @Then("I must get absolute URLs of these paths")
    public void ensureExtractionIsCorrect(DataTable paths) {
        var pathList = paths.asList();

        assertEquals(pathList.size(), extractedURLs.size());

        var origin = Origin.getOrigin(href);
        var urls = pathList.stream().map(path -> origin + path).collect(toList());

        assertTrue(extractedURLs.stream().allMatch(url -> urls.contains(url.toString())));
    }
}
