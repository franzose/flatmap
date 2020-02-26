package com.janiwanow.flatmap.parser.n1;

import org.jsoup.nodes.Document;

import java.util.Objects;

final class PriceExtractor {
    static double extract(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        var text = document.selectFirst(".offer-card-header .price")
            .text()
            .replaceAll("[^0-9]", "");

        return Double.parseDouble(text);
    }
}
