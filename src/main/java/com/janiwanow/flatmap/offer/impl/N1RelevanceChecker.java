package com.janiwanow.flatmap.offer.impl;

import com.janiwanow.flatmap.http.HttpConnection;
import com.janiwanow.flatmap.offer.RelevanceCheckResult;
import com.janiwanow.flatmap.offer.RelevanceChecker;
import com.janiwanow.flatmap.parser.PropertyDetailsExtractor;

import java.net.URI;
import java.util.Objects;
import java.util.regex.Pattern;

public final class N1RelevanceChecker implements RelevanceChecker {
    private static final Pattern PATTERN = Pattern.compile("https://.*\\.n1\\.ru");
    private final HttpConnection connection;
    private final PropertyDetailsExtractor extractor;

    public N1RelevanceChecker(
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
            extractor.extract(document.get()).isEmpty()
        );

        return isObsolete
            ? RelevanceCheckResult.obsolete(url)
            : RelevanceCheckResult.relevant(url);
    }

    @Override
    public boolean supports(URI url) {
        Objects.requireNonNull(url, "URL must not be null.");

        return PATTERN.matcher(url.toString()).lookingAt();
    }
}
