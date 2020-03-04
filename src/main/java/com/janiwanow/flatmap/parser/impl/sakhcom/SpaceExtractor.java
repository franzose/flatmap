package com.janiwanow.flatmap.parser.impl.sakhcom;

import com.janiwanow.flatmap.data.Space;
import com.janiwanow.flatmap.util.Numbers;
import org.jsoup.nodes.Document;

import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Apartment area details extractor.
 */
public final class SpaceExtractor {
    private static final Pattern PATTERN = Pattern.compile("((?:П|п)лощадь:\\s*\\d+)|(жилая:\\s*\\d+)|(кухня:\\s*\\d+)");

    /**
     * Extracts apartment area details and uses the default rooms extractor.
     *
     * @param document offer page like https://dom.sakh.com/flat/sell/546472
     * @return extracted area details under the title "Space"
     */
    public static Space extract(Document document) {
        return extract(document, RoomsExtractor::extract);
    }

    /**
     * Extracts apartment area details.
     *
     * @param document offer page like https://dom.sakh.com/flat/sell/546472
     * @param roomsExtractor An implementation of the extractor
     * @return extracted area details under the title "Space"
     */
    public static Space extract(Document document, Function<Document, Integer> roomsExtractor) {
        Objects.requireNonNull(document, "Document must not be null.");
        Objects.requireNonNull(roomsExtractor, "Rooms extractor must not be null.");

        var matcher = PATTERN.matcher(document.selectFirst("#offer .area").text());

        double total = 0;
        double living = 0;
        double kitchen = 0;

        while (matcher.find()) {
            var lowered = matcher.group().toLowerCase();
            var number = Numbers.parseDouble(lowered);

            if (lowered.startsWith("площадь")) {
                total = number;
            } else if (lowered.startsWith("жилая")) {
                living = number;
            } else if (lowered.startsWith("кухня")) {
                kitchen = number;
            }
        }

        return new Space(total, living, kitchen, roomsExtractor.apply(document));
    }
}

