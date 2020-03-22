package com.janiwanow.flatmap.realty.provider.sakhcom;

import com.janiwanow.flatmap.internal.http.HttpConnection;
import com.janiwanow.flatmap.realty.Parser;
import com.janiwanow.flatmap.realty.ParserOptions;
import com.janiwanow.flatmap.realty.property.PropertyDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toSet;

/**
 * Parser of the offers listed on https://dom.sakh.com.
 */
public final class SakhcomParser implements Parser {
    private static final Logger LOG = LoggerFactory.getLogger(SakhcomParser.class);
    private final Set<String> cities;

    public SakhcomParser(Set<String> cities) {
        this.cities = cities;
    }

    /**
     * Retrieves HTML from a bunch of URLs and parses it to a set of property details.
     *
     * @param connection an HTTP connection to use for parsing
     * @param options parser options
     * @return a set of property details
     */
    @Override
    public Set<PropertyDetails> parse(HttpConnection connection, ParserOptions options) {
        Objects.requireNonNull(connection, "HTTP connection must not be null.");
        LOG.info("Starting to fetch apartment information from sakh.com...");

        var fetcher = new SakhcomCityFetcher(
            new SakhcomURLs().getURLs(options.pagination),
            connection.newBuilder(),
            options.delay
        );

        var details = cities
            .stream()
            .map(fetcher::fetchFromCity)
            .map(CompletableFuture::join)
            .flatMap(Collection::stream)
            .collect(toSet());

        LOG.info("Finished fetching apartment information.");

        return details;
    }

    @Override
    public boolean supports(String websiteId) {
        return "sakhcom".equals(websiteId);
    }
}
