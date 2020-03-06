package com.janiwanow.flatmap.parser.impl.sakhcom;

import com.janiwanow.flatmap.data.PropertyDetails;
import com.janiwanow.flatmap.http.HttpConnection;
import com.janiwanow.flatmap.http.HttpConnectionBuilder;
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
public class SakhcomCityFetcher {
    private final HttpConnectionBuilder http;
    private final Set<URI> urls;
    private final Function<HttpConnection, PropertyDetailsFetcher> function;

    /**
     * @param urls this fetcher should process
     * @param http connection builder needed to setup a new {@link HttpConnection}
     * @param function which takes a new HTTP connection built
     *                 by the given builder and returns a new {@link PropertyDetailsFetcher}
     *                 ready to fetch all the given urls
     */
    public SakhcomCityFetcher(
        Set<URI> urls,
        HttpConnectionBuilder http,
        Function<HttpConnection, PropertyDetailsFetcher> function
    ) {
        Objects.requireNonNull(urls, "URLs must not be null.");
        Objects.requireNonNull(http, "HttpConnectionBuilder must not be null.");
        Objects.requireNonNull(function, "Fetcher function must not be null.");
        this.urls = urls;
        this.http = http;
        this.function = function;
    }

    /**
     * Schedules fetching property details from the given city.
     *
     * @param city to fetch property details from
     * @return a {@link CompletableFuture} which is going to be run later
     */
    public CompletableFuture<Set<PropertyDetails>> fetchFromCity(String city) {
        var conn = http.cookies(Map.of("city", city)).build();
        return CompletableFuture.supplyAsync(() -> function.apply(conn).fetchAll(urls));
    }
}
