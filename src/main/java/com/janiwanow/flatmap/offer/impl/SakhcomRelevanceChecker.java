package com.janiwanow.flatmap.offer.impl;

import com.janiwanow.flatmap.http.HttpConnection;
import com.janiwanow.flatmap.offer.RelevanceCheckResult;
import com.janiwanow.flatmap.offer.RelevanceChecker;
import com.janiwanow.flatmap.parser.PropertyDetailsExtractor;

import java.net.URI;
import java.util.Objects;

public final class SakhcomRelevanceChecker implements RelevanceChecker {
    private final HttpConnection connection;
    private final PropertyDetailsExtractor extractor;

    public SakhcomRelevanceChecker(
        HttpConnection connection,
        PropertyDetailsExtractor extractor
    ) {
        Objects.requireNonNull(connection, "Connection must not be null.");
        Objects.requireNonNull(extractor, "Extractor must not be null.");
        this.connection = connection;
        this.extractor = extractor;
    }

    @Override
    public RelevanceCheckResult check(URI url) {
        Objects.requireNonNull(url, "URL must not be null.");

        var document = connection.fetch(url);

        var isObsolete = (
            document.isEmpty() ||
            extractor.extract(document.get()).isEmpty() ||
            document.get().html().contains("Объявление из архива")
        );

        return isObsolete
            ? RelevanceCheckResult.obsolete(url)
            : RelevanceCheckResult.relevant(url);
    }

    @Override
    public boolean supports(URI url) {
        Objects.requireNonNull(url, "URL must not be null.");

        return url.toString().startsWith("https://dom.sakh.com");
    }
}
