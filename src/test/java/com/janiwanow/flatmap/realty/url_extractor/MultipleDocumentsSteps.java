package com.janiwanow.flatmap.realty.url_extractor;

import com.janiwanow.flatmap.realty.URLExtractor;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MultipleDocumentsSteps {
    private Map<Document, List<String>> documents = new HashMap<>();
    private Set<URI> extractedURLs;

    @Given("I fetched an HTML document from {string} containing the following URLs")
    public void documentContainsURLs(String origin, DataTable paths) {
        var pathList = paths.asList();
        var document = new Document(origin);
        var container = document.appendElement("div").attr("class", "links-container");

        for (String url : pathList) {
            container.appendElement("a").attr("href", url);
        }

        documents.put(document, pathList);
    }

    @When("I pass the documents to the extractor")
    public void theExtractorTakesTheseDocuments() {
        extractedURLs = URLExtractor.extract(documents.keySet(), ".links-container > a");
    }

    @Then("I must get a combined list of URLs")
    public void itShouldReturnCombinedListOfURLs() throws URISyntaxException {
        var expectedSize = documents.values().stream().mapToLong(Collection::size).sum();

        assertEquals(expectedSize, extractedURLs.size());

        for (Map.Entry<Document, List<String>> entry : documents.entrySet()) {
            var origin = Origin.getOrigin(new URI(entry.getKey().baseUri()));

            for (String path : entry.getValue()) {
                assertTrue(extractedURLs.contains(new URI(origin + path)));
            }
        }
    }
}
