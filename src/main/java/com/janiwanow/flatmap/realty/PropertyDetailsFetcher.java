package com.janiwanow.flatmap.realty;

import com.janiwanow.flatmap.internal.http.DocumentFetcher;
import com.janiwanow.flatmap.realty.data.PropertyDetails;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * A service to gather property details from the offer pages.
 *
 * <p>Gathering is done in the following steps:
 * <ol>
 *     <li>Fetch HTML of the offer list page</li>
 *     <li>Retrieve all URLs pointing to the offer pages</li>
 *     <li>Fetch those offer pages</li>
 *     <li>Grab the details from each offer page</li>
 * </ol>
 */
public final class PropertyDetailsFetcher {
    private final DocumentFetcher fetcher;
    private final PropertyDetailsExtractor extractor;
    private final String offerPageLinkSelector;

    /**
     * Constructs PropertyDetailsFetcher.
     *
     * @param fetcher Fetcher instance to download HTML documents for processing
     * @param extractor Extractor instance to grab necessary apartment details
     * @param offerPageLinkSelector Selector to search offer page links by
     */
    public PropertyDetailsFetcher(DocumentFetcher fetcher, PropertyDetailsExtractor extractor, String offerPageLinkSelector) {
        Objects.requireNonNull(fetcher, "Fetcher must not be null.");
        Objects.requireNonNull(extractor, "Extractor must not be null.");
        Objects.requireNonNull(offerPageLinkSelector, "Selector must not be null.");

        this.fetcher = fetcher;
        this.extractor = extractor;
        this.offerPageLinkSelector = offerPageLinkSelector;
    }

    /**
     * Fetches property details from the given offer list.
     *
     * @param offerListURL URL of the offer list to process the offers from
     * @return property details extracted from the dedicated offer pages
     */
    public Set<PropertyDetails> fetch(URI offerListURL) {
        Objects.requireNonNull(offerListURL, "URL must not be null.");

        return process(fetcher.fetchAsync(offerListURL).thenApply(extractURLsOptionally()));
    }

    /**
     * Fetches property details from the given offer lists.
     *
     * @param offerListURLs URLs of the offer lists to process the offers from
     * @return property details extracted from the dedicated offer pages
     */
    public Set<PropertyDetails> fetchAll(Set<URI> offerListURLs) {
        Objects.requireNonNull(offerListURLs, "URLs must not be null.");

        return process(fetcher.fetchAsync(offerListURLs).thenApply(extractURLs()));
    }

    /**
     * Extracts offer URLs from the document if it's present, returns an empty set otherwise.
     *
     * @return offer URLs for further processing
     */
    private Function<Optional<Document>, Set<URI>> extractURLsOptionally() {
        return document -> document.map(d -> URLExtractor.extract(d, offerPageLinkSelector)).orElseGet(HashSet::new);
    }

    /**
     * Extracts offer URLs from the given documents.
     *
     * @return offer URLs for further processing
     */
    private Function<Set<Document>, Set<URI>> extractURLs() {
        return documents -> URLExtractor.extract(documents, offerPageLinkSelector);
    }

    /**
     * Fetches the HTML documents from the given URLs and then extracts property details from them.
     *
     * @param future incomplete future containing previously fetched offer page URLs
     * @return property details extracted from the dedicated offer pages
     */
    private Set<PropertyDetails> process(CompletableFuture<Set<URI>> future) {
        return future.thenApply(fetcher::fetchAll).thenApply(extractor::extract).join();
    }
}
