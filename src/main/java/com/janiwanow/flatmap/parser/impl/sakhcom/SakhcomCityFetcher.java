package com.janiwanow.flatmap.parser.impl.sakhcom;

import com.janiwanow.flatmap.data.PropertyDetails;
import com.janiwanow.flatmap.http.Delay;
import com.janiwanow.flatmap.http.DocumentFetcher;
import com.janiwanow.flatmap.http.HttpConnection;
import com.janiwanow.flatmap.http.HttpConnectionBuilder;
import com.janiwanow.flatmap.parser.ParserOptions;
import com.janiwanow.flatmap.parser.PropertyDetailsExtractor;
import com.janiwanow.flatmap.parser.PropertyDetailsFetcher;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * A service to fetch property details from different cities on sakh.com.
 *
 * <p>This kind of a wrapper is needed just because one cannot just change URL
 * to switch to another city on sakh.com. This website detects the chosen city
 * by the session cookie thus we need to send it along with the request.
 */
final class SakhcomCityFetcher {
    private final HttpConnectionBuilder http;
    private final Set<URI> urls;
    private final Delay delay;

    /**
     * @param urls this fetcher should process
     * @param http connection builder needed to setup a new {@link HttpConnection}
     * @param delay a range of the delay between HTTP requests
     */
    SakhcomCityFetcher(Set<URI> urls, HttpConnectionBuilder http, Delay delay) {
        Objects.requireNonNull(urls, "URLs must not be null.");
        Objects.requireNonNull(http, "HttpConnectionBuilder must not be null.");
        Objects.requireNonNull(delay, "Delay must not be null.");
        this.urls = urls;
        this.http = http;
        this.delay = delay;
    }

    /**
     * Schedules fetching property details from the given city.
     *
     * @param city to fetch property details from
     * @return a {@link CompletableFuture} which is going to be run later
     */
    public CompletableFuture<Set<PropertyDetails>> fetchFromCity(String city) {
        var conn = http.cookies(Map.of("city", city)).build();
        return CompletableFuture.supplyAsync(() -> getFetcher(conn).fetchAll(urls));
    }

    /**
     * Builds a fetcher from the common and the custom components.
     *
     * @param connection an http connection instance
     * @return fetcher which will do the job
     */
    private PropertyDetailsFetcher getFetcher(HttpConnection connection) {
        return new PropertyDetailsFetcher(
            new DocumentFetcher(connection, delay),
            new PropertyDetailsExtractor(
                AddressExtractor::extract,
                AreaExtractor::extract,
                PriceExtractor::extract
            ),
            ".offers > .item > .content > a"
        );
    }
}
