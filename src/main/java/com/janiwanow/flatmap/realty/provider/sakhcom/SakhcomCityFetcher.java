package com.janiwanow.flatmap.realty.provider.sakhcom;

import com.janiwanow.flatmap.internal.http.DelayRange;
import com.janiwanow.flatmap.internal.http.DocumentFetcher;
import com.janiwanow.flatmap.internal.http.HttpConnection;
import com.janiwanow.flatmap.internal.http.HttpConnectionBuilder;
import com.janiwanow.flatmap.realty.PropertyDetailsExtractor;
import com.janiwanow.flatmap.realty.PropertyDetailsFetcher;
import com.janiwanow.flatmap.realty.data.PropertyDetails;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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
    private final DelayRange delay;

    /**
     * @param urls this fetcher should process
     * @param http connection builder needed to setup a new {@link HttpConnection}
     * @param delay a range of the delay between HTTP requests
     */
    SakhcomCityFetcher(Set<URI> urls, HttpConnectionBuilder http, DelayRange delay) {
        Objects.requireNonNull(urls, "URLs must not be null.");
        Objects.requireNonNull(http, "HttpConnectionBuilder must not be null.");
        Objects.requireNonNull(delay, "DelayRange must not be null.");
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
