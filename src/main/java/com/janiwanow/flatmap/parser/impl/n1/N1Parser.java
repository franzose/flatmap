package com.janiwanow.flatmap.parser.impl.n1;

import com.janiwanow.flatmap.data.ApartmentInfo;
import com.janiwanow.flatmap.http.DocumentFetcher;
import com.janiwanow.flatmap.http.HttpConnection;
import com.janiwanow.flatmap.parser.ApartmentInfoExtractor;
import com.janiwanow.flatmap.parser.ApartmentInfoFetcher;
import com.janiwanow.flatmap.parser.WebsiteParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;

/**
 * Parser of the offers listed on https://n1.ru.
 */
public final class N1Parser implements WebsiteParser {
    private static final Logger LOG = LoggerFactory.getLogger(N1Parser.class);
    private final Set<String> cities;
    private final int pages;

    public N1Parser(Set<String> cities, int pages) {
        Objects.requireNonNull(cities, "Cities must not be null.");
        this.cities = cities;
        this.pages = Math.max(1, pages);
    }

    /**
     * Retrieves HTML from a bunch of URLs and parses it to a set of apartment information.
     *
     * @param connection an HTTP connection to use for parsing
     * @return a set of apartment information
     */
    @Override
    public Set<ApartmentInfo> parse(HttpConnection connection) {
        Objects.requireNonNull(connection, "HTTP connection must not be null.");
        LOG.info("Starting to fetch apartment information from N1...");

        var urls = new N1URLs(cities).getURLs(pages);
        var apartments = getFetcher(connection).fetchAll(urls);

        LOG.info("Finished fetching apartment information.");

        return apartments;
    }

    /**
     * Builds a fetcher from the common and the custom components.
     *
     * @param connection an http connection instance
     * @return fetcher which will do the job
     */
    private ApartmentInfoFetcher getFetcher(HttpConnection connection) {
        return new ApartmentInfoFetcher(
            new DocumentFetcher(connection),
            new ApartmentInfoExtractor(
                AddressExtractor::extract,
                SpaceExtractor::extract,
                PriceExtractor::extract
            ),
            ".offers-search .card-title > a"
        );
    }

    @Override
    public boolean supports(String websiteId) {
        return "n1".equals(websiteId);
    }
}
