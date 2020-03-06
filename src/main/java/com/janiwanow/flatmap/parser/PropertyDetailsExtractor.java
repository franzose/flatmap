package com.janiwanow.flatmap.parser;

import com.janiwanow.flatmap.data.PropertyDetails;
import com.janiwanow.flatmap.data.Price;
import com.janiwanow.flatmap.data.Space;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

/**
 * Property details extractor.
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
 *     var extractor = new PropertyDetailsExtractor(
 *         AddressExtractor::extract,
 *         SpaceExtractor::extract,
 *         PriceExtractor::extract
 *     );
 * </code>
 * </pre>
 */
public final class PropertyDetailsExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyDetailsExtractor.class);
    private final Function<Document, String> addressExtractor;
    private final Function<Document, Space> spaceExtractor;
    private final Function<Document, Price> priceExtractor;

    public PropertyDetailsExtractor(
        Function<Document, String> addressExtractor,
        Function<Document, Space> spaceExtractor,
        Function<Document, Price> priceExtractor
    ) {
        this.addressExtractor = addressExtractor;
        this.spaceExtractor = spaceExtractor;
        this.priceExtractor = priceExtractor;
    }

    /**
     * Extracts property details from the HTML document.
     *
     * <p>There may be a case when the HTML document is incomplete or empty.
     * This may lead to NPE since extractors, for the sake of simplicity,
     * are not forced to do any checks for {@link NullPointerException}.
     *
     * <p>Instead, we catch it here and return an empty {@link Optional}.
     * At the moment, this is just enough.
     *
     * @param document HTML document to parse
     * @return extracted information details
     */
    public Optional<PropertyDetails> extract(Document document) {
        Objects.requireNonNull(document, "Document must not be null.");

        try {
            return Optional.of(new PropertyDetails(
                new URI(document.baseUri()),
                addressExtractor.apply(document),
                spaceExtractor.apply(document),
                priceExtractor.apply(document)
            ));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            LOG.info("Could not extract property details from {}.", document.baseUri(), e);
            return Optional.empty();
        } catch (NumberFormatException e) {
            LOG.info("Could not extract property details from {}.", document.baseUri(), e);
            LOG.debug(document.html());
            return Optional.empty();
        }
    }

    /**
     * Extracts property details from the given HTML documents.
     *
     * @param documents HTML documents to parse
     * @return extracted property details
     */
    public Set<PropertyDetails> extract(Set<Document> documents) {
        Objects.requireNonNull(documents, "Documents must not be null.");

        return documents
            .stream()
            .map(this::extract)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toSet());
    }
}
