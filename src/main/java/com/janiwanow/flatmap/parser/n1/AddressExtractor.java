package com.janiwanow.flatmap.parser.n1;

import org.jsoup.nodes.Document;

import java.util.Collections;
import java.util.Objects;

final class AddressExtractor {
    static String extract(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        var components = document.select(".offer-card-geo-tags .ui-kit-link__inner").eachText();
        Collections.reverse(components);

        return String.join(", ", components);
    }
}
