package com.janiwanow.flatmap.http;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.*;

/**
 * The default implementation of a connection fetching HTML documents.
 *
 * As stated in the class name, the current implementation relies on {@link org.jsoup.Connection}
 * to fetch the HTML documents and has built-in support for retries in case of failures.
 */
public final class JsoupHttpConnection implements HttpConnection {
    private static final Logger LOG = LoggerFactory.getLogger(JsoupHttpConnection.class);
    private static final List<String> USER_AGENTS = List.of(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:73.0) Gecko/20100101 Firefox/73.0",
        "Mozilla/5.0 (X11; Linux x86_64; rv:73.0) Gecko/20100101 Firefox/73.0",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.1 Safari/605.1.15",
        "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/18.17763",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.8) Gecko/20100101 Firefox/60.8"
    );

    public static final int MINIMUM_RETRIES = 3;
    public static final int MINIMUM_TIMEOUT = 5000;
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
            connection = new JsoupHttpConnection();

            return result;
        }

        @Override
        public String toString() {
            return String.format(
                "{retries:%d, timeout:%d, cookies:%s}",
                connection.retries,
                connection.timeout,
                connection.cookies
            );
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
        var userAgent = getRandomUserAgent();

        LOG.info("Started fetching from {}", url);
        LOG.info("Retries: {}, timeout: {}ms, cookies: {}", retries, timeout, cookies);
        LOG.info("User agent: {}", userAgent);

        do {
            try {
                document = Jsoup.connect(url.toString())
                    .ignoreHttpErrors(false)
                    .timeout(timeout)
                    .cookies(cookies)
                    .userAgent(userAgent)
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

    private String getRandomUserAgent() {
        return USER_AGENTS.get(new Random().nextInt(USER_AGENTS.size()));
    }

    @Override
    public HttpConnectionBuilder newBuilder() {
        return builder()
            .timeout(this.timeout)
            .retries(this.retries)
            .cookies(this.cookies);
    }
}
