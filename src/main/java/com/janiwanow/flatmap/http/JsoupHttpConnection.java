package com.janiwanow.flatmap.http;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The default implementation of a connection fetching HTML documents.
 *
 * As stated in the class name, the current implementation relies on {@link org.jsoup.Connection}
 * to fetch the HTML documents and has built-in support for retries in case of failures.
 */
public final class JsoupHttpConnection implements HttpConnection {
    private static final Logger LOG = LoggerFactory.getLogger(JsoupHttpConnection.class);
    public static final int MINIMUM_RETRIES = 1;
    public static final int MINIMUM_TIMEOUT = 1500;
    private int retries;
    private int timeout;
    private Map<String, String> cookies = new HashMap<>();

    public static class Builder implements HttpConnectionBuilder {
        private JsoupHttpConnection connection;

        public Builder() {
            connection = new JsoupHttpConnection();
        }

        @Override
        public HttpConnectionBuilder retries(int retries) {
            connection.retries = Math.max(retries, MINIMUM_RETRIES);
            return this;
        }

        @Override
        public HttpConnectionBuilder timeout(int timeout) {
            connection.timeout = Math.max(timeout, MINIMUM_TIMEOUT);
            return this;
        }

        @Override
        public HttpConnectionBuilder cookies(Map<String, String> cookies) {
            connection.cookies.putAll(cookies);
            return this;
        }

        @Override
        public HttpConnection build() {
            var result = connection;
            connection = null;

            return result;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public JsoupHttpConnection() {
        this.retries = MINIMUM_RETRIES;
        this.timeout = MINIMUM_TIMEOUT;
    }

    /**
     * Fetches an HTML document from the given URL.
     *
     * In case of failures, does as much retries as defined by the connection options.
     * Exceptions are swallowed to avoid breaking the normal program flow.
     *
     * @param url The URL to fetch the document from
     * @return optional containing the fetched document or
     *         empty optional in case of a failure
     */
    @Override
    public Optional<Document> fetch(URI url) {
        Objects.requireNonNull(url, "URL must not be null.");

        Document document = null;

        int attempts = 1;

        LOG.info("Started fetching from {}", url);
        LOG.info("HttpConnection options: timeout {}ms, {} retries", timeout, retries);

        do {
            try {
                document = Jsoup.connect(url.toString())
                    .ignoreHttpErrors(false)
                    .timeout(timeout)
                    .cookies(cookies)
                    .get();

                LOG.info("Finished fetching {}. Attempts: {}", url, attempts);
                break;
            } catch (HttpStatusException e) {
                LOG.warn("Failed to fetch {}\nStatus code: {}. Attempts: {}", url, e.getStatusCode(), attempts, e);
            } catch (SocketTimeoutException e) {
                LOG.warn("HttpConnection to {} timed out. Attempts: {}", url, attempts, e);
            } catch (IOException e) {
                LOG.warn("HttpConnection error while fetching {}. Attempts: {}", url, attempts, e);
            } catch (Throwable e) {
                LOG.error("Unexpected exception while fetching {}. Attempts: {}", url, attempts, e);
            }

            attempts++;
        } while (attempts <= retries);

        return Optional.ofNullable(document);
    }
}
