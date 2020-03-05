package com.janiwanow.flatmap.parser.impl.n1;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Builder aimed at producing valid N1 URLs.
 */
public final class N1URLs {
    /**
     * Pattern used to build the final URL.
     *
     * <p>Parameters as they appear in the string:
     * <ol>
     *     <li>City
     *     <li>Offer type (purchase or rent)
     *     <li>Property type (apartment, room, cottage, dacha etc)
     *     <li>Page number of the offer list
     * </ol>
     */
    public static final String PATTERN = "https://%s.n1.ru/%s/%s/?page=%d";

    public static final Set<String> OFFER_TYPES = Set.of(
        "kupit",              // purchase
        "snyat/dolgosrochno", // long-term rental
        "snyat/posutochno"    // daily rental
    );

    public static final Set<String> PROPERTY_TYPES = Set.of(
        "kvartiry", // apartment
        "komnaty",  // rooms
        "doma",     // house, cottage, villa etc
        "dachi"     // dacha
    );

    /**
     * A set of cities from which we want to get the property details.
     *
     * <p>Unlike the offer and property types, cities are made dynamic intentionally.
     * The formers are hardly to change in the future, while cities might change
     * if we want to show more or less cities on the map.
     */
    private final Set<String> cities;

    public N1URLs(Set<String> cities) {
        Objects.requireNonNull(cities, "Cities must not be null.");
        this.cities = cities;
    }

    /**
     * Builds a bunch of N1 URLs.
     *
     * @param pages number of pages for the parser to go through
     * @return a set of valid N1 URLs ready to be parsed
     */
    public Set<URI> getURLs(int pages) {
        pages = Math.max(1, pages);

        Set<URI> urls = new HashSet<>();

        for (var city : cities) {
            for (var offer : OFFER_TYPES) {
                for (var property : PROPERTY_TYPES) {
                    for (var page = 1; page <= pages; page++) {
                        try {
                            urls.add(new URI(String.format(PATTERN, city, offer, property, page)));
                        } catch (URISyntaxException e) {
                            // it's not going to happen
                        }
                    }
                }
            }
        }

        return urls;
    }
}
