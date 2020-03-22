package com.janiwanow.flatmap.realty.database;

import com.janiwanow.flatmap.internal.sql.TestDbConnectionFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;

public final class FetchURLsByChunksSteps {
    private final RelevanceTestSteps context;
    private int chunkSize;
    private FetchURLsByChunks query;
    private Consumer<List<URI>> consumer;

    public FetchURLsByChunksSteps(RelevanceTestSteps context) {
        this.context = context;
    }

    @When("I fetch URLs from the database by {int} items in a chunk")
    public void fetch(int chunkSize) {
        this.chunkSize = chunkSize;
        query = new FetchURLsByChunks(TestDbConnectionFactory.INSTANCE);
        consumer = urls -> {};
    }

    @Then("I should consume {int} URLs at a time")
    public void ensureChunkSizeIsCorrect(int expectedChunkSize) {
        var expectedURLs = context.getUrls();
        var actualURLs = new ArrayList<>();

        query.accept(chunkSize, consumer.andThen(urls -> {
            assertTrue(urls.size() <= expectedChunkSize);
            actualURLs.addAll(urls);
        }));

        assertTrue(actualURLs.containsAll(expectedURLs));
    }
}
