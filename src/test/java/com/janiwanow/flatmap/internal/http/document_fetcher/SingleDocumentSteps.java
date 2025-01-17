package com.janiwanow.flatmap.internal.http.document_fetcher;

import com.janiwanow.flatmap.internal.http.DelayRange;
import com.janiwanow.flatmap.internal.http.DocumentFetcher;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

public class SingleDocumentSteps {
    private Document original;
    private Document fetched;
    private CompletableFuture<Optional<Document>> future;

    @Given("I scheduled a request to {string}")
    public void setUpFetcher(String url) throws URISyntaxException {
        original = new Document(url);
        future = new DocumentFetcher(new FakeHttpConnection(u -> Optional.of(original)), new DelayRange(1, 1))
            .fetchAsync(new URI(url));
    }

    @When("the document is fetched")
    public void fetch() {
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
