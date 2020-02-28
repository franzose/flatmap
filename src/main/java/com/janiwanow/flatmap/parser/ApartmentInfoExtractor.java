package com.janiwanow.flatmap.parser;

import com.janiwanow.flatmap.data.ApartmentInfo;
import com.janiwanow.flatmap.data.Price;
import com.janiwanow.flatmap.data.Space;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

/**
 * Apartment information details extractor.
 *
 * <p>This extractor gathers data by delegating extraction to several
 * dedicated extractors, one for each peace of information. Thus,
 * it's not tied to particular implementations or websites.
 *
 * <p>Thanks to using {@link Function} interfaces here, one can
 * easily pass method references to the constructor of this class like this:
 *
 * <pre>
 * <code>
 *     var extractor = new ApartmentInfoExtractor(
 *         AddressExtractor::extract,
 *         SpaceExtractor::extract,
 *         PriceExtractor::extract
 *     );
 * </code>
 * </pre>
 */
public final class ApartmentInfoExtractor {
    private final Function<Document, String> addressExtractor;
    private final Function<Document, Space> spaceExtractor;
    private final Function<Document, Price> priceExtractor;

    public ApartmentInfoExtractor(
        Function<Document, String> addressExtractor,
        Function<Document, Space> spaceExtractor,
        Function<Document, Price> priceExtractor
    ) {
        this.addressExtractor = addressExtractor;
        this.spaceExtractor = spaceExtractor;
        this.priceExtractor = priceExtractor;
    }

    /**
     * Extracts apartment information details from the HTML document.
     *
     * @param document HTML document to parse
     * @return extracted information details
     */
    public ApartmentInfo extract(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        try {
            return new ApartmentInfo(
                new URL(document.baseUri()),
                addressExtractor.apply(document),
                spaceExtractor.apply(document),
                priceExtractor.apply(document)
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extracts apartment information details from the given HTML documents.
     *
     * @param documents HTML documents to parse
     * @return extracted information details
     */
    public Set<ApartmentInfo> extract(Set<Document> documents) {
        Objects.requireNonNull(documents, "Documents must not be null.");

        return documents.stream().map(this::extract).collect(toSet());
    }
}
