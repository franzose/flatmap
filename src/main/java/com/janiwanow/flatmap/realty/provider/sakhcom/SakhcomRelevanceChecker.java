package com.janiwanow.flatmap.realty.provider.sakhcom;

import com.janiwanow.flatmap.internal.http.HttpConnection;
import com.janiwanow.flatmap.realty.PropertyDetailsExtractor;
import com.janiwanow.flatmap.realty.RelevanceCheckResult;
import com.janiwanow.flatmap.realty.RelevanceChecker;

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

    public static SakhcomRelevanceChecker getDefault(HttpConnection connection) {
        Objects.requireNonNull(connection, "Connection must not be null.");
        return new SakhcomRelevanceChecker(connection, new PropertyDetailsExtractor(
            AddressExtractor::extract,
            AreaExtractor::extract,
            PriceExtractor::extract
        ));
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
