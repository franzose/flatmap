package com.janiwanow.flatmap.parser.n1;

import com.janiwanow.flatmap.data.Space;
import com.janiwanow.flatmap.parser.Numbers;
import org.jsoup.nodes.Document;

final class SpaceExtractor {
    static Space extract(Document document) {
        var texts = document.select(".offer-card-factoids .text").eachText();

        return new Space(
            Numbers.parseDouble(texts.get(0)),
            Numbers.parseDouble(texts.get(1)),
            Numbers.parseDouble(texts.get(2)),
            RoomsExtractor.extract(document)
        );
    }
}
