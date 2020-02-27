package com.janiwanow.flatmap.parser;

import com.janiwanow.flatmap.data.ApartmentInfo;
import com.janiwanow.flatmap.data.Price;
import com.janiwanow.flatmap.data.Space;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.function.Function;

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
     * @throws MalformedURLException if the {@link Document#baseUri()} returns an invalid value for {@link URL}
     */
    public ApartmentInfo extract(Document document) throws MalformedURLException {
        Objects.requireNonNull(document, "Document must not be null.");

        return new ApartmentInfo(
            new URL(document.baseUri()),
            addressExtractor.apply(document),
            spaceExtractor.apply(document),
            priceExtractor.apply(document)
        );
    }
}
