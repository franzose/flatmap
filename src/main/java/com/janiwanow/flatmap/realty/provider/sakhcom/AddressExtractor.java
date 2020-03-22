package com.janiwanow.flatmap.realty.provider.sakhcom;

import org.jsoup.nodes.Document;

import java.util.Objects;
import java.util.function.Function;

/**
 * Apartment address extractor.
 */
public final class AddressExtractor implements Function<Document, String> {
    /**
     * Extracts apartment address from the Sakh.com offer page.
     *
     * @param document offer page like https://dom.sakh.com/flat/sell/546472
     * @return extracted address
     */
    @Override
    public String apply(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        var headings = document.select("#offer > h4");

        if (headings == null) {
            return "";
        }

        // This is for houses and dachas
        if (headings.size() > 1) {
            var heading = headings.last();

            return heading == null ? "" : heading.text();
        }

        var heading = headings.first();

        if (heading == null) {
            return "";
        }

        var link = heading.selectFirst("a");

        if (link == null) {
            return heading.text();
        }

        // ".text" element might be wrapped in a link,
        // so we grab the element first and only then remove the link
        var street = heading.selectFirst(".text");

        link.remove();

        if (street == null) {
            return heading.text();
        }

        return String.format("%s %s", heading.text(), street.text());
    }
}
