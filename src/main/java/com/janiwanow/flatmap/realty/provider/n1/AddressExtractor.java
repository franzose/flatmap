package com.janiwanow.flatmap.realty.provider.n1;

import org.jsoup.nodes.Document;

import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;

/**
 * Apartment address extractor.
 */
public final class AddressExtractor implements Function<Document, String> {
    /**
     * Extracts apartment address from the N1 offer page.
     *
     * @param document N1 offer page like https://novosibirsk.n1.ru/view/33016674/
     * @return extracted address
     */
    @Override
    public String apply(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        var components = document.select(".offer-card-geo-tags .ui-kit-link__inner").eachText();
        Collections.reverse(components);

        return String.join(", ", components);
    }
}
