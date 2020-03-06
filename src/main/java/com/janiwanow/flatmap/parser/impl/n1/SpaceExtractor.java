package com.janiwanow.flatmap.parser.impl.n1;

import com.janiwanow.flatmap.data.Space;
import com.janiwanow.flatmap.util.Numbers;
import org.jsoup.nodes.Document;

import java.util.Objects;
import java.util.function.Function;

/**
 * Apartment area details extractor.
 */
public final class SpaceExtractor {
    /**
     * Extracts apartment area details and uses the default rooms extractor.
     *
     * @param document N1 offer page like https://novosibirsk.n1.ru/view/33016674/
     * @return extracted area details under the title "Space"
     */
    public static Space extract(Document document) {
        return extract(document, RoomsExtractor::extract);
    }

    /**
     * Extracts apartment area details.
     *
     * @param document N1 offer page like https://novosibirsk.n1.ru/view/33016674/
     * @param roomsExtractor An implementation of the extractor
     * @return extracted area details under the title "Space"
     */
    public static Space extract(Document document, Function<Document, Integer> roomsExtractor) {
        Objects.requireNonNull(document, "Document must not be null.");
        Objects.requireNonNull(roomsExtractor, "Rooms extractor must not be null.");

        var texts = document.select(".offer-card-factoids .text").eachText();
        var size = texts.size();

        return new Space(
            size >= 1 ? Numbers.parseDouble(texts.get(0)) : 0.0,
            size >= 2 ? Numbers.parseDouble(texts.get(1)) : 0.0,
            size >= 3 ? Numbers.parseDouble(texts.get(2)) : 0.0,
            roomsExtractor.apply(document)
        );
    }
}
