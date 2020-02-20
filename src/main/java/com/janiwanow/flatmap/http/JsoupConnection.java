package com.janiwanow.flatmap.http;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

/**
 * The default implementation of a connection fetching HTML documents.
 *
 * As stated in the class name, the current implementation relies on {@link org.jsoup.Connection}
 * to fetch the HTML documents and has built-in support for retries in case of failures.
 */
public class JsoupConnection implements Connection {
    public static final class Options {
        public static final int MINIMUM_RETRIES = 1;
        public static final int MINIMUM_TIMEOUT = 1500;
        public final int retries;
        public final int timeout;

        /**
         * Constructs connection options.
         *
         * @param retries number of retries before giving up
         * @param timeout connection timeout
         */
        public Options(int retries, int timeout) {
            this.retries = Math.max(retries, MINIMUM_RETRIES);
            this.timeout = Math.max(timeout, MINIMUM_TIMEOUT);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(JsoupConnection.class);
    private Options options;

    public JsoupConnection(Options options) {
        Objects.requireNonNull(options, "Connection options must not be null.");
        this.options = options;
    }

    /**
     * Fetches an HTML document from the given URL.
     *
     * In case of failures, does as much retries as defined by the connection {@link Options}.
     * Exceptions are swallowed to avoid breaking the normal program flow.
     *
     * @param url The URL to fetch the document from
     * @return optional containing the fetched document or
     *         empty optional in case of a failure
     */
    @Override
    public Optional<Document> fetch(URL url) {
        Objects.requireNonNull(url, "URL must not be null.");

        Document document = null;

        int attempts = 1;

        LOG.info("Started fetching from {}", url);
        LOG.info("Connection options: timeout {}ms, {} retries", options.timeout, options.retries);

        do {
            try {
                document = Jsoup.connect(url.toString())
                    .ignoreHttpErrors(false)
                    .timeout(options.timeout)
                    .get();

                LOG.info("Finished fetching {}. Attempts: {}", url, attempts);
                break;
            } catch (HttpStatusException e) {
                LOG.warn("Failed to fetch {}\nStatus code: {}. Attempts: {}", url, e.getStatusCode(), attempts, e);
            } catch (SocketTimeoutException e) {
                LOG.warn("Connection to {} timed out. Attempts: {}", url, attempts, e);
            } catch (IOException e) {
                LOG.warn("Connection error while fetching {}. Attempts: {}", url, attempts, e);
            } catch (Throwable e) {
                LOG.error("Unexpected exception while fetching {}. Attempts: {}", url, attempts, e);
            }

            attempts++;
        } while (attempts <= options.retries);

        return Optional.ofNullable(document);
    }
}
