package com.janiwanow.flatmap.http;

import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.Optional;

/**
 * Common interface for connections aimed at fetching HTML documents by the given URLs.
 */
public interface HttpConnection {
    /**
     * Tries to fetch an HTML document from the given URL.
     *
     * {@link Optional} is used intentionally so that the clients to be explicitly
     * aware of the value absence possibility and for them to avoid dealing with
     * possible NULLs or exceptions related to connection errors.
     *
     * @param url The URL to fetch the document from
     * @return optional containing the fetched document or
     *         empty optional in case of connection errors
     */
    Optional<Document> fetch(URI url);

    /**
     * Creates a new HTTP connection builder providing the current connection options to it.
     *
     * @return a new builder inheriting the current connection options
     */
    HttpConnectionBuilder newBuilder();
}
