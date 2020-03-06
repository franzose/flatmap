package com.janiwanow.flatmap.parser.impl.n1;

import com.janiwanow.flatmap.data.PropertyDetails;
import com.janiwanow.flatmap.http.DocumentFetcher;
import com.janiwanow.flatmap.http.HttpConnection;
import com.janiwanow.flatmap.parser.PropertyDetailsExtractor;
import com.janiwanow.flatmap.parser.PropertyDetailsFetcher;
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

    public N1Parser(Set<String> cities) {
        Objects.requireNonNull(cities, "Cities must not be null.");
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
        LOG.info("Starting to fetch properties from N1...");

        var urls = new N1URLs(cities).getURLs(pages);
        var details = getFetcher(connection).fetchAll(urls);

        LOG.info("Finished fetching properties.");

        return details;
    }

    /**
     * Builds a fetcher from the common and the custom components.
     *
     * @param connection an http connection instance
     * @return fetcher which will do the job
     */
    private PropertyDetailsFetcher getFetcher(HttpConnection connection) {
        return new PropertyDetailsFetcher(
            new DocumentFetcher(connection),
            new PropertyDetailsExtractor(
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
