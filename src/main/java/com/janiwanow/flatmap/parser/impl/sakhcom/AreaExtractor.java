package com.janiwanow.flatmap.parser.impl.sakhcom;

import com.janiwanow.flatmap.data.Area;
import com.janiwanow.flatmap.internal.util.Numbers;
import org.jsoup.nodes.Document;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Property area details extractor.
 */
public final class AreaExtractor {
    private static final Pattern PATTERN = Pattern.compile("((?:П|п)лощадь:\\s*\\d+)|(жилая:\\s*\\d+)|(кухня:\\s*\\d+)");

    /**
     * Extracts property area details and uses the default rooms extractor.
     *
     * @param document offer page like https://dom.sakh.com/flat/sell/546472
     * @return extracted area details under the title "Area"
     */
    public static Optional<Area> extract(Document document) {
        return extract(document, RoomsExtractor::extract);
    }

    /**
     * Extracts property area details.
     *
     * @param document offer page like https://dom.sakh.com/flat/sell/546472
     * @param roomsExtractor An implementation of the extractor
     * @return extracted area details under the title "Area"
     */
    public static Optional<Area> extract(Document document, Function<Document, Integer> roomsExtractor) {
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

        if (living == 0.0) {
            living = total;
        }

        // It is likely a single bed offer, we don't need that
        if (total == 0.0 && living == 0.0 && kitchen == 0.0) {
            return Optional.empty();
        }

        return Optional.of(new Area(total, living, kitchen, roomsExtractor.apply(document)));
    }
}

