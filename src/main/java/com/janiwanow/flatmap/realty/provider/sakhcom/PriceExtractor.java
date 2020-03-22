package com.janiwanow.flatmap.realty.provider.sakhcom;

import com.janiwanow.flatmap.internal.util.Numbers;
import com.janiwanow.flatmap.realty.data.Price;
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
