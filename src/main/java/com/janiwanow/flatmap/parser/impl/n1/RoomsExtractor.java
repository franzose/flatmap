package com.janiwanow.flatmap.parser.impl.n1;

import com.janiwanow.flatmap.util.Numbers;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

/**
 * Apartment rooms extractor.
 */
public final class RoomsExtractor {
    /**
     * Extracts the number of rooms of the apartment.
     *
     * @param document N1 offer page like https://novosibirsk.n1.ru/view/33016674/
     * @return extracted number of rooms
     */
    public static int extract(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        try {
            // if it's an apartment, its rooms number is in the offer title
            return Numbers.parseInt(document.selectFirst(".offer-card-header .deal-title").text());
        } catch (NullPointerException | NumberFormatException e) {
            // in other cases the number should be within a list of parameters
            var value = getRoomsValue(document.select(".card-living-content-params-list__name"));

            // it's likely we've found a room offer if the rooms number is empty
            return value.isEmpty() ? 1 : Numbers.parseInt(value.get().text());
        }
    }

    /**
     * Tries to find rooms number in the given list of elements.
     *
     * @param parameters of a property
     * @return element if a number is found, empty otherwise
     */
    private static Optional<Element> getRoomsValue(Elements parameters) {
        for (var name : parameters) {
            if (name.text().toLowerCase().startsWith("комнат")) {
                return Optional.of(name.nextElementSibling());
            }
        }

        return Optional.empty();
    }
}
