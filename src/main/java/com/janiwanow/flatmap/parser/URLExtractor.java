package com.janiwanow.flatmap.parser;

import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * URL extractor.
 *
 * <p>It's sole purpose is to extract URLs from the HTML documents. Extractor accepts
 * instances of Jsoup's {@link Document} and returns sets of {@link URL} instances back.
 * Note that the extractor <i>always</i> returns absolute form of the URLs using
 * Document's built-in {@code abs:href} notation. This is by design since
 * extracted URLs are then used to download and process other HTML documents.
 *
 * <p>The current implementation silently ignores any invalid URL.
 */
public class URLExtractor {

    /**
     * Extracts URLs from an HTML document.
     *
     * @param document The document to extract URLs from
     * @param selector The look-up CSS selector to query against
     * @return a set of URLs extracted from the document
     */
    public static Set<URL> extract(Document document, String selector) {
        Objects.requireNonNull(document, "Document must not be null.");
        Objects.requireNonNull(selector, "Selector must not be null.");

        return document
            .select(selector)
            .eachAttr("abs:href")
            .stream()
            .map(URLExtractor::toUrl)
            .filter(Objects::nonNull)
            .collect(toSet());
    }

    /**
     * Extracts URLs from multiple HTML documents into a single set.
     *
     * @param documents A set of documents to extract URLs from
     * @param selector The look-up CSS selector to query against
     * @return a set of URLs extracted from the documents
     */
    public static Set<URL> extract(Set<Document> documents, String selector) {
        Objects.requireNonNull(documents, "Document must not be null.");
        Objects.requireNonNull(selector, "Selector must not be null.");

        return documents.stream()
            .flatMap(document -> extract(document, selector).stream())
            .collect(toSet());
    }

    /**
     * Converts a URL from {@link String} to {@link URL}.
     *
     * <p>It is safe to return NULL from this method since all NULLs
     * are filtered out in {@link #extract(Document, String)} method.
     *
     * @param url A full URL or a relative path
     * @return a valid URL, null otherwise
     */
    private static URL toUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
