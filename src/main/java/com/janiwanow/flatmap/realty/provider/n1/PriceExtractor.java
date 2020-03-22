package com.janiwanow.flatmap.realty.provider.n1;

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
     * @param document N1 offer page like https://novosibirsk.n1.ru/view/33016674/
     * @return extracted price or empty
     */
    @Override
    public Optional<Price> apply(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        var element = document.selectFirst(".offer-card-header .price");

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
