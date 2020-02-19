package com.janiwanow.flatmap.http;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Optional;

public class JsoupConnection implements Connection {
    public static final class Options {
        public final int retries;
        public final int timeout;

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
     * @param url a URL to fetch the document from
     * @return
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
