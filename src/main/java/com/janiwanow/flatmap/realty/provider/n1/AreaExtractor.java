package com.janiwanow.flatmap.realty.provider.n1;

import com.janiwanow.flatmap.internal.util.Numbers;
import com.janiwanow.flatmap.realty.property.Area;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Property area details extractor.
 */
public final class AreaExtractor implements Function<Document, Optional<Area>> {
    /**
     * Extracts property area details and uses the default rooms extractor.
     *
     * @param document N1 offer page like https://novosibirsk.n1.ru/view/33016674/
     * @return extracted area details under the title "Area"
     */
    @Override
    public Optional<Area> apply(Document document) {
        return apply(document, new RoomsExtractor());
    }

    /**
     * Extracts property area details.
     *
     * @param document N1 offer page like https://novosibirsk.n1.ru/view/33016674/
     * @param roomsExtractor An implementation of the extractor
     * @return extracted area details under the title "Area"
     */
    public Optional<Area> apply(Document document, Function<Document, Integer> roomsExtractor) {
        Objects.requireNonNull(document, "Document must not be null.");
        Objects.requireNonNull(roomsExtractor, "Rooms extractor must not be null.");

        var paramNames = document.select(".card-living-content-params-list__name");

        if (paramNames.isEmpty()) {
            return Optional.empty();
        }

        var total = getParameterValueByName(paramNames, "Общая площадь");

        if (total.isEmpty()) {
            return Optional.empty();
        }

        var living = getParameterValueByName(paramNames, "Жилая площадь");
        var kitchen = getParameterValueByName(paramNames, "Кухня");

        if (living.isEmpty()) {
            living = total;
        }

        return Optional.of(new Area(
            Numbers.parseDouble(total.get().text()),
            Numbers.parseDouble(living.get().text()),
            kitchen.isEmpty() ? 0.0 : Numbers.parseDouble(kitchen.get().text()),
            roomsExtractor.apply(document)
        ));
    }

    private static Optional<Element> getParameterValueByName(Elements paramNames, String name) {
        for (var paramName : paramNames) {
            if (paramName.text().toLowerCase().startsWith(name.toLowerCase())) {
                var next = paramName.nextElementSibling();

                if (next == null) {
                    return Optional.empty();
                }

                var text = next.text();

                if (text.isEmpty() || text.isBlank()) {
                    return Optional.empty();
                }

                return Optional.of(next);
            }
        }

        return Optional.empty();
    }
}
