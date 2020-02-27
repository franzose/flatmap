package com.janiwanow.flatmap.parser.n1;

import com.janiwanow.flatmap.data.Space;
import com.janiwanow.flatmap.parser.Numbers;
import org.jsoup.nodes.Document;

import java.util.Objects;

/**
 * Apartment area details extractor.
 */
final class SpaceExtractor {
    /**
     * Extracts apartment area details.
     *
     * @param document N1 offer page like https://novosibirsk.n1.ru/view/33016674/
     * @return extracted area details under the title "Space"
     */
    static Space extract(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        var texts = document.select(".offer-card-factoids .text").eachText();

        return new Space(
            Numbers.parseDouble(texts.get(0)),
            Numbers.parseDouble(texts.get(1)),
            Numbers.parseDouble(texts.get(2)),
            RoomsExtractor.extract(document)
        );
    }
}
