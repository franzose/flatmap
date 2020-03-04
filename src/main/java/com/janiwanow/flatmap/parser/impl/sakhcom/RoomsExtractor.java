package com.janiwanow.flatmap.parser.impl.sakhcom;

import com.janiwanow.flatmap.util.Numbers;
import org.jsoup.nodes.Document;

import java.util.Objects;

/**
 * Apartment rooms extractor.
 */
public final class RoomsExtractor {
    /**
     * Extracts the number of rooms of the apartment.
     *
     * @param document offer page like https://dom.sakh.com/flat/sell/546472
     * @return extracted number of rooms
     */
    public static int extract(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        return Numbers.parseInt(document.selectFirst("#offer > h3").text());
    }
}
