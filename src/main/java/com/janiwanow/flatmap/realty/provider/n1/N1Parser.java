package com.janiwanow.flatmap.realty.provider.n1;

import com.janiwanow.flatmap.internal.http.DocumentFetcher;
import com.janiwanow.flatmap.internal.http.HttpConnection;
import com.janiwanow.flatmap.realty.Parser;
import com.janiwanow.flatmap.realty.ParserOptions;
import com.janiwanow.flatmap.realty.PropertyDetailsExtractor;
import com.janiwanow.flatmap.realty.PropertyDetailsFetcher;
import com.janiwanow.flatmap.realty.property.PropertyDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;

/**
 * Parser of the offers listed on https://n1.ru.
 */
public final class N1Parser implements Parser {
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
     * @param options parser options
     * @return a set of property details
     */
    @Override
    public Set<PropertyDetails> parse(HttpConnection connection, ParserOptions options) {
        Objects.requireNonNull(connection, "HTTP connection must not be null.");
        LOG.info("Starting to fetch properties from N1...");

        var fetcher = new PropertyDetailsFetcher(
            new DocumentFetcher(connection, options.delay),
            new PropertyDetailsExtractor(
                new AddressExtractor(),
                new AreaExtractor(),
                new PriceExtractor()
            ),
            ".offers-search .card-title > a"
        );

        var urls = new N1URLs(cities).getURLs(options.pagination);
        var details = fetcher.fetchAll(urls);

        LOG.info("Finished fetching properties.");

        return details;
    }

    @Override
    public boolean supports(String websiteId) {
        return "n1".equals(websiteId);
    }
}
