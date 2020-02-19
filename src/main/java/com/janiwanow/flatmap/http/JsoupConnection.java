package com.janiwanow.flatmap.http;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
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

    private Options options;

    public JsoupConnection(Options options) {
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
        if (url == null) {
            throw new IllegalArgumentException("URL must not be null.");
        }

        Document document = null;

        int attempts = 1;

        do {
            try {
                document = Jsoup.connect(url.toString()).timeout(options.timeout).get();
                break;
            } catch (SocketTimeoutException e) {
                // TODO: log timeout
            } catch (IOException e) {
                // TODO: log IOException
            } catch (Throwable e) {
                // TODO: log throwable
            }

            attempts++;
        } while (attempts <= options.retries);

        return Optional.ofNullable(document);
    }
}
