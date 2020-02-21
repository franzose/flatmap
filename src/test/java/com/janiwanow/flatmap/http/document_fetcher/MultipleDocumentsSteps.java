package com.janiwanow.flatmap.http.document_fetcher;

import com.janiwanow.flatmap.http.DocumentFetcher;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MultipleDocumentsSteps {
    private CompletableFuture<Set<Document>> future;
    private Set<Document> fetched;
    private Map<URL, Document> map = new HashMap<>();

    @Given("I scheduled multiple fetching requests")
    public void setUpFetcher(DataTable data) {
        var urls = data.asList()
            .stream()
            .map(MultipleDocumentsSteps::toURL)
            .peek(url -> map.put(url, new Document(url.toString())))
            .collect(toSet());

        future = new DocumentFetcher(url -> Optional.of(map.get(url))).fetchAsync(urls);
    }

    private static URL toURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @When("the documents were fetched successfully")
    public void fetchSuccessfully() {
        fetched = future.join();
    }

    @Then("I must get those HTML documents")
    public void ensureTheDocumentsArePresent() {
        assertTrue(fetched.containsAll(map.values()));
    }
}
