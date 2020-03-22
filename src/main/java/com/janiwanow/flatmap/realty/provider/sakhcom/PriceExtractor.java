package com.janiwanow.flatmap.realty.provider.sakhcom;

import com.janiwanow.flatmap.internal.util.Numbers;
import com.janiwanow.flatmap.realty.property.Price;
import org.jsoup.nodes.Document;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Apartment price extractor.
 */
public final class PriceExtractor implements Function<Document, Optional<Price>> {
    /**
     * Extracts rent or sale price of the apartment.
     *
     * @param document offer page like https://dom.sakh.com/flat/sell/546472
     * @return extracted price
     */
    @Override
    public Optional<Price> apply(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        var element = document.selectFirst("#offer .price .value");

        if (element == null) {
            return Optional.empty();
        }

        var text = element.text();

        if (text == null || text.isEmpty() || text.isBlank()) {
            return Optional.empty();
        }

        try {
            return Optional.of(Price.inRubles(Numbers.parseDouble(text)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
