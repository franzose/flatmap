package com.janiwanow.flatmap.parser.n1;

import org.jsoup.nodes.Document;

final class RoomsExtractor {
    static int extract(Document document) {
        return Integer.parseInt(document.selectFirst(".offer-card-header .deal-title").text().replaceAll("[^0-9]", ""));
    }
}
