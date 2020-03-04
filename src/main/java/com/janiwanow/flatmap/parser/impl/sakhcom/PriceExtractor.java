package com.janiwanow.flatmap.parser.impl.sakhcom;

import com.janiwanow.flatmap.data.Price;
import com.janiwanow.flatmap.util.Numbers;
import org.jsoup.nodes.Document;

import java.util.Objects;

/**
 * Apartment price extractor.
 */
public final class PriceExtractor {
    /**
     * Extracts rent or sale price of the apartment.
     *
     * @param document offer page like https://dom.sakh.com/flat/sell/546472
     * @return extracted price
     */
    public static Price extract(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        return Price.inRubles(Numbers.parseDouble(document.selectFirst("#offer .price .value").text()));
    }
}
