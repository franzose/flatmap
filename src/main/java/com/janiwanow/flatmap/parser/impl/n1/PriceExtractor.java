package com.janiwanow.flatmap.parser.impl.n1;

import com.janiwanow.flatmap.data.Price;
import com.janiwanow.flatmap.internal.util.Numbers;
import org.jsoup.nodes.Document;

import java.util.Objects;

/**
 * Apartment price extractor.
 */
public final class PriceExtractor {
    /**
     * Extracts rent or sale price of the apartment.
     *
     * @param document N1 offer page like https://novosibirsk.n1.ru/view/33016674/
     * @return extracted price
     */
    public static Price extract(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        return Price.inRubles(Numbers.parseDouble(document.selectFirst(".offer-card-header .price").text()));
    }
}
