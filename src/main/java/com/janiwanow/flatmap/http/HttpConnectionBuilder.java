package com.janiwanow.flatmap.http;

import java.util.Map;

/**
 * Interface for all HTTP connection builder implementations.
 */
public interface HttpConnectionBuilder {
    /**
     * Sets a number of retries.
     *
     * @param retries
     * @return this builder
     */
    HttpConnectionBuilder retries(int retries);

    /**
     * Sets a timeout.
     *
     * @param timeout in milliseconds
     * @return this builder
     */
    HttpConnectionBuilder timeout(int timeout);

    /**
     * Sets cookies.
     *
     * @param cookies
     * @return this builder
     */
    HttpConnectionBuilder cookies(Map<String, String> cookies);

    /**
     * Builds a new {@link HttpConnection} from the given options.
     *
     * @return a new connection
     */
    HttpConnection build();
}
