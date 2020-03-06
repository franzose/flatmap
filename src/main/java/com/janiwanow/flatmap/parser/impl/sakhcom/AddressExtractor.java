package com.janiwanow.flatmap.parser.impl.sakhcom;

import org.jsoup.nodes.Document;

import java.util.Objects;

/**
 * Apartment address extractor.
 */
public final class AddressExtractor {
    /**
     * Extracts apartment address from the Sakh.com offer page.
     *
     * @param document offer page like https://dom.sakh.com/flat/sell/546472
     * @return extracted address
     */
    public static String extract(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        var heading = document.selectFirst("#offer > h4");
        var street = heading.selectFirst(".text");

        if (street == null) {
            return document.selectFirst("#offer > h4 + h4").text();
        }

        var link = heading.selectFirst("a");

        if (link != null) {
            link.remove();
        }

        return String.format("%s %s", heading.text(), street.text());
    }
}
