package com.janiwanow.flatmap.http;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentFetcherSteps {
    private Document document;
    private Document fetchedDocument;
    private CompletableFuture<Optional<Document>> future;

    @Given("I scheduled a request to {string}")
    public void setUpFetcher(String url) throws MalformedURLException {
        document = new Document(url);
        future = new DocumentFetcher(connectionUrl -> Optional.of(document)).fetchAsync(new URL(url));
    }

    @When("fetching completed successfully")
    public void fetchSuccessfully() {
        future.join()
            .ifPresentOrElse(
                doc -> fetchedDocument = doc,
                () -> fail("HTML document must be present.")
            );
    }

    @Then("I must get that HTML document")
    public void ensureTheDocumentIsPresent() {
        assertSame(document, fetchedDocument);
    }
}
