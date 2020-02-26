package com.janiwanow.flatmap.parser.n1;

import com.janiwanow.flatmap.parser.Numbers;
import org.jsoup.nodes.Document;

import java.util.Objects;

final class PriceExtractor {
    static double extract(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        return Numbers.parseDouble(document.selectFirst(".offer-card-header .price").text());
    }
}
