package com.janiwanow.flatmap.http;

import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toSet;

/**
 * HTML document fetcher.
 */
public class DocumentFetcher {
    private final Connection connection;

    public DocumentFetcher(Connection connection) {
        Objects.requireNonNull(connection, "Connection must not be null.");

        this.connection = connection;
    }

    /**
     * Schedules fetching of an HTML document from the given URL.
     *
     * @param url The URL to fetch the document from
     * @return future ready to fetch the document
     */
    public CompletableFuture<Optional<Document>> fetchAsync(URL url) {
        Objects.requireNonNull(url, "URL must not be null.");

        return CompletableFuture.supplyAsync(() -> connection.fetch(url));
    }

    /**
     * Schedules fetching of HTML documents from the given URLs.
     *
     * @param urls a set of URLs to fetch the documents from
     * @return future ready to fetch the documents
     */
    public CompletableFuture<Set<Document>> fetchAsync(Set<URL> urls) {
        Objects.requireNonNull(urls, "URLs must not be null.");

        return CompletableFuture.supplyAsync(() -> fetch(urls));
    }

    /**
     * Fetches the documents from the given URLs.
     *
     * @param urls a set of URLs to fetch the documents from
     * @return a set of fetched HTML documents
     */
    public Set<Document> fetch(Set<URL> urls) {
        Objects.requireNonNull(urls, "URLs must not be null.");

        return urls.stream()
            .map(this::fetchAsync)
            .map(CompletableFuture::join)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toSet());
    }
}
