package com.janiwanow.flatmap.parser.impl.sakhcom;

import com.janiwanow.flatmap.internal.util.Numbers;
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

        var heading = document.selectFirst("#offer > h1 + h3");

        if (heading == null) {
            return 1;
        }

        var text = heading.text();

        if (text.isEmpty() || text.isBlank()) {
            return 1;
        }

        try {
            return Numbers.parseInt(text);
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
