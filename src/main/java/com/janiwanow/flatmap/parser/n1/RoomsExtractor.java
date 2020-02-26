package com.janiwanow.flatmap.parser.n1;

import com.janiwanow.flatmap.parser.Numbers;
import org.jsoup.nodes.Document;

final class RoomsExtractor {
    static int extract(Document document) {
        return Numbers.parseInt(document.selectFirst(".offer-card-header .deal-title").text());
    }
}
