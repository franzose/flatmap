package com.janiwanow.flatmap.http;

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
import static org.junit.jupiter.api.Assertions.*;

public class DocumentFetcherSteps {
    private Document document;
    private Document fetchedDocument;
    private CompletableFuture<Optional<Document>> documentFuture;
    private CompletableFuture<Set<Document>> documentSetFuture;
    private Set<Document> fetchedDocuments;
    private Map<URL, Document> urlToDocument = new HashMap<>();

    @Given("I scheduled a request to {string}")
    public void setUpFetcher(String url) throws MalformedURLException {
        document = new Document(url);
        documentFuture = new DocumentFetcher(connectionUrl -> Optional.of(document))
            .fetchAsync(new URL(url));
    }

    @Given("I scheduled multiple fetching requests")
    public void setUpFetcher(DataTable data) {
        var urls = data.asList()
            .stream()
            .map(DocumentFetcherSteps::toURL)
            .peek(url -> urlToDocument.put(url, new Document(url.toString())))
            .collect(toSet());

        documentSetFuture = new DocumentFetcher(url -> Optional.of(urlToDocument.get(url)))
            .fetchAsync(urls);
    }

    private static URL toURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @When("fetching completed successfully")
    public void fetchSuccessfully() {
        if (documentFuture != null) {
            documentFuture.join()
                .ifPresentOrElse(
                    doc -> fetchedDocument = doc,
                    () -> fail("HTML document must be present.")
                );
        }

        if (documentSetFuture != null) {
            fetchedDocuments = documentSetFuture.join();
        }
    }

    @Then("I must get that HTML document")
    public void ensureTheDocumentIsPresent() {
        assertSame(document, fetchedDocument);
    }

    @Then("I must get those HTML documents")
    public void ensureTheDocumentsArePresent() {
        assertTrue(fetchedDocuments.containsAll(urlToDocument.values()));
    }
}
