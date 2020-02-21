package com.janiwanow.flatmap.http.document_fetcher;

import com.janiwanow.flatmap.http.DocumentFetcher;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

public class SingleDocumentSteps {
    private Document original;
    private Document fetched;
    private CompletableFuture<Optional<Document>> future;

    @Given("I scheduled a request to {string}")
    public void setUpFetcher(String url) throws MalformedURLException {
        original = new Document(url);
        future = new DocumentFetcher(connectionUrl -> Optional.of(original))
            .fetchAsync(new URL(url));
    }

    @When("the document was fetched successfully")
    public void fetchSuccessfully() {
        future.join()
            .ifPresentOrElse(
                doc -> fetched = doc,
                () -> fail("HTML document must be present.")
            );
    }

    @Then("I must get that HTML document")
    public void ensureTheDocumentIsPresent() {
        assertSame(original, fetched);
    }
}
