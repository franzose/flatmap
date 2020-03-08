package com.janiwanow.flatmap.parser.impl.sakhcom;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * Builder aimed at producing valid sakh.com URLs.
 */
public final class SakhcomURLs {
    /**
     * Pattern used to build the final URL.
     *
     * <p>Parameters as they appear in the string:
     * <ol>
     *     <li>Property type
     *     <li>Offer type
     *     <li>Page number
     * </ol>
     */
    public static final String PATTERN = "https://dom.sakh.com/%s/%s/list%d";

    public static final Set<String> OFFER_TYPES = Set.of(
        "sell", // purchase
        "lease" // rent
    );

    /**
     * In case of Sakh.com, rent period is a query string parameter.
     */
    public static final Set<String> RENT_PERIODS = Set.of(
        "s[period]=ежемесячно", // monthly rent
        "s[period]=посуточно"   // daily rent
    );

    public static final Set<String> PROPERTY_TYPES = Set.of(
        "flat",  // apartments
        "room",  // rooms
        "house" // houses, villas, cottages
    );

    public Set<URI> getURLs(int pages) {
        pages = Math.max(1, pages);

        Set<URI> urls = new HashSet<>();

        for (var property : PROPERTY_TYPES) {
            for (var offer : OFFER_TYPES) {
                for (var page = 1; page <= pages; page++) {
                    try {
                        var path = String.format(PATTERN, property, offer, page);

                        if (offer.equals("lease")) {
                            for (var period : RENT_PERIODS) {
                                urls.add(new URI(path + '?' + period));
                            }
                        } else {
                            urls.add(new URI(path));
                        }
                    } catch (URISyntaxException e) {
                        // it's not going to happen
                    }
                }
            }
        }

        return urls;
    }
}
