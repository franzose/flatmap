package com.janiwanow.flatmap.realty.provider.n1;

import com.janiwanow.flatmap.internal.http.HttpConnection;
import com.janiwanow.flatmap.realty.PropertyDetailsExtractor;
import com.janiwanow.flatmap.realty.RelevanceCheckResult;
import com.janiwanow.flatmap.realty.RelevanceChecker;

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

    public static N1RelevanceChecker getDefault(HttpConnection connection) {
        Objects.requireNonNull(connection, "Connection must not be null.");
        return new N1RelevanceChecker(connection, new PropertyDetailsExtractor(
            new AddressExtractor(),
            new AreaExtractor(),
            new PriceExtractor()
        ));
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
