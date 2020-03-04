package com.janiwanow.flatmap.parser;

import com.janiwanow.flatmap.data.ApartmentInfo;
import com.janiwanow.flatmap.http.DocumentFetcher;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * A service to gather apartment information details from the offer pages.
 *
 * <p>Gathering is done in the following steps:
 * <ol>
 *     <li>Fetch HTML of the offer list page</li>
 *     <li>Retrieve all URLs pointing to the offer pages</li>
 *     <li>Fetch those offer pages</li>
 *     <li>Grab apartment information from each offer page</li>
 * </ol>
 */
public final class ApartmentInfoFetcher {
    private final DocumentFetcher fetcher;
    private final ApartmentInfoExtractor extractor;
    private final String offerPageLinkSelector;

    /**
     * Constructs ApartmentInfoFetcher.
     *
     * @param fetcher Fetcher instance to download HTML documents for processing
     * @param extractor Extractor instance to grab necessary apartment details
     * @param offerPageLinkSelector Selector to search offer page links by
     */
    public ApartmentInfoFetcher(DocumentFetcher fetcher, ApartmentInfoExtractor extractor, String offerPageLinkSelector) {
        Objects.requireNonNull(fetcher, "Fetcher must not be null.");
        Objects.requireNonNull(extractor, "Extractor must not be null.");
        Objects.requireNonNull(offerPageLinkSelector, "Selector must not be null.");

        this.fetcher = fetcher;
        this.extractor = extractor;
        this.offerPageLinkSelector = offerPageLinkSelector;
    }

    /**
     * Fetches information about the apartments from the given offer list.
     *
     * @param offerListURL URL of the offer list to process the offers from
     * @return apartment information details extracted from the dedicated offer pages
     */
    public Set<ApartmentInfo> fetch(URI offerListURL) {
        Objects.requireNonNull(offerListURL, "URL must not be null.");

        return process(fetcher.fetchAsync(offerListURL).thenApply(extractURLsOptionally()));
    }

    /**
     * Fetches information about the apartments from the given offer lists.
     *
     * @param offerListURLs URLs of the offer lists to process the offers from
     * @return apartment information details extracted from the dedicated offer pages
     */
    public Set<ApartmentInfo> fetchAll(Set<URI> offerListURLs) {
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
     * Fetches the HTML documents from the given URLs and then extracts apartment information from them.
     *
     * @param future incomplete future containing previously fetched offer page URLs
     * @return apartment information details extracted from the dedicated offer pages
     */
    private Set<ApartmentInfo> process(CompletableFuture<Set<URI>> future) {
        return future.thenApply(fetcher::fetchAll).thenApply(extractor::extract).join();
    }
}
