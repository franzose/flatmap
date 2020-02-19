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
        public final int retries;
        public final int timeout;

        /**
         * Constructs connection options.
         *
         * @param retries number of retries before giving up
         * @param timeout connection timeout
         */
        public Options(int retries, int timeout) {
            this.retries = retries;
            this.timeout = timeout;
        }
    }

    private Options options;

    public JsoupConnection(Options options) {
        this.options = options;
    }

    /**
     * Fetches an HTML document from the given URL.
     *
     * Does as much retries as defined by the connection {@link Options}.
     * Exceptions are swallowed to avoid breaking the normal program flow.
     *
     * @param url The URL to fetch the document from
     * @return optional containing the fetched document or
     *         empty optional in case of a failure
     */
    @Override
    public Optional<Document> fetch(URL url) {
        Document document = null;

        for (int tries = 1; tries <= options.retries; tries++) {
            try {
                document = Jsoup.connect(url.toString()).timeout(options.timeout).get();
                break;
            } catch (SocketTimeoutException e) {
                // TODO: log timeout
            } catch (IOException e) {
                // TODO: log IOException
            }
        }

        return Optional.ofNullable(document);
    }
}
