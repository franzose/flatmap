package com.janiwanow.flatmap.parser.n1;

import com.janiwanow.flatmap.parser.Numbers;
import org.jsoup.nodes.Document;

import java.util.Objects;

/**
 * Apartment rooms extractor.
 */
final class RoomsExtractor {
    /**
     * Extracts the number of rooms of the apartment.
     *
     * @param document N1 offer page like https://novosibirsk.n1.ru/view/33016674/
     * @return extracted number of rooms
     */
    static int extract(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        return Numbers.parseInt(document.selectFirst(".offer-card-header .deal-title").text());
    }
}
