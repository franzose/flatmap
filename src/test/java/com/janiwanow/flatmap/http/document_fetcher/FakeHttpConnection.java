package com.janiwanow.flatmap.http.document_fetcher;

import com.janiwanow.flatmap.http.HttpConnection;
import com.janiwanow.flatmap.http.HttpConnectionBuilder;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class FakeHttpConnection implements HttpConnection {

    private final Function<URI, Optional<Document>> function;

    public FakeHttpConnection(Function<URI, Optional<Document>> function) {
        this.function = function;
    }

    @Override
    public Optional<Document> fetch(URI url) {
        return function.apply(url);
    }

    @Override
    public HttpConnectionBuilder newBuilder() {
        return new HttpConnectionBuilder() {
            @Override
            public HttpConnectionBuilder retries(int retries) {
                return this;
            }

            @Override
            public HttpConnectionBuilder timeout(int timeout) {
                return this;
            }

            @Override
            public HttpConnectionBuilder cookies(Map<String, String> cookies) {
                return this;
            }

            @Override
            public HttpConnection build() {
                return new FakeHttpConnection(url -> Optional.empty());
            }
        };
    }
}
