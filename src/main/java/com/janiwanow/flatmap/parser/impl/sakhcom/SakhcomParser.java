package com.janiwanow.flatmap.parser.impl.sakhcom;

import com.janiwanow.flatmap.data.PropertyDetails;
import com.janiwanow.flatmap.http.DocumentFetcher;
import com.janiwanow.flatmap.http.HttpConnection;
import com.janiwanow.flatmap.http.HttpConnectionBuilder;
import com.janiwanow.flatmap.parser.PropertyDetailsExtractor;
import com.janiwanow.flatmap.parser.PropertyDetailsFetcher;
import com.janiwanow.flatmap.parser.WebsiteParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toSet;

/**
 * Parser of the offers listed on https://dom.sakh.com.
 */
public final class SakhcomParser implements WebsiteParser {
    private static final Logger LOG = LoggerFactory.getLogger(SakhcomParser.class);
    private final HttpConnectionBuilder http;
    private final Set<String> cities;

    public SakhcomParser(HttpConnectionBuilder http, Set<String> cities) {
        this.http = http;
        this.cities = cities;
    }

    /**
     * Retrieves HTML from a bunch of URLs and parses it to a set of property details.
     *
     * @param connection an HTTP connection to use for parsing
     * @param pages number of pages to go through
     * @return a set of property details
     */
    @Override
    public Set<PropertyDetails> parse(HttpConnection connection, int pages) {
        Objects.requireNonNull(connection, "HTTP connection must not be null.");
        LOG.info("Starting to fetch apartment information from sakh.com...");

        var fetcher = new SakhcomCityFetcher(new SakhcomURLs().getURLs(pages), http, SakhcomParser::getFetcher);
        var details = cities
            .stream()
            .map(fetcher::fetchFromCity)
            .map(CompletableFuture::join)
            .flatMap(Collection::stream)
            .collect(toSet());

        LOG.info("Finished fetching apartment information.");

        return details;
    }

    /**
     * Builds a fetcher from the common and the custom components.
     *
     * @param connection an http connection instance
     * @return fetcher which will do the job
     */
    private static PropertyDetailsFetcher getFetcher(HttpConnection connection) {
        return new PropertyDetailsFetcher(
            new DocumentFetcher(connection),
            new PropertyDetailsExtractor(
                AddressExtractor::extract,
                AreaExtractor::extract,
                PriceExtractor::extract
            ),
            ".offers > .item > .content > a"
        );
    }

    @Override
    public boolean supports(String websiteId) {
        return "sakhcom".equals(websiteId);
    }
}
