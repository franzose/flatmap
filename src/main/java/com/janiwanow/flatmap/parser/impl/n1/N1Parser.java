package com.janiwanow.flatmap.parser.impl.n1;

import com.janiwanow.flatmap.data.ApartmentInfo;
import com.janiwanow.flatmap.http.DocumentFetcher;
import com.janiwanow.flatmap.http.HttpConnection;
import com.janiwanow.flatmap.parser.ApartmentInfoExtractor;
import com.janiwanow.flatmap.parser.ApartmentInfoFetcher;
import com.janiwanow.flatmap.parser.WebsiteParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Parser of the offers listed on https://n1.ru.
 */
public final class N1Parser implements WebsiteParser {
    private static final Logger LOG = LoggerFactory.getLogger(N1Parser.class);
    private static final String WEBSITE_ID = "n1";

    @Override
    public Set<ApartmentInfo> parse(HttpConnection connection) {
        Objects.requireNonNull(connection, "HTTP connection must not be null.");
        LOG.info("Starting to fetch apartment information from N1...");

        var apartments = new ApartmentInfoFetcher(
            new DocumentFetcher(connection),
            new ApartmentInfoExtractor(
                AddressExtractor::extract,
                SpaceExtractor::extract,
                PriceExtractor::extract
            ),
            ".offers-search .card-title > a"
        ).fetchAll(new HashSet<>()); // TODO: real URLs

        LOG.info("Finished fetching apartment information.");

        return apartments;
    }

    @Override
    public boolean supports(String websiteId) {
        return WEBSITE_ID.equals(websiteId);
    }
}
