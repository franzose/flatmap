package com.janiwanow.flatmap.realty;

import java.net.URI;
import java.util.Objects;

/**
 * A data class representing a result of an offer relevance check.
 */
public final class RelevanceCheckResult {
    public final URI url;
    public final boolean isRelevant;
    public final boolean isObsolete;

    public static RelevanceCheckResult relevant(URI url) {
        Objects.requireNonNull(url, "URI must not be null.");
        return new RelevanceCheckResult(url, true);
    }

    public static RelevanceCheckResult obsolete(URI url) {
        Objects.requireNonNull(url, "URI must not be null.");
        return new RelevanceCheckResult(url, false);
    }

    private RelevanceCheckResult(URI url, boolean isRelevant) {
        this.url = url;
        this.isRelevant = isRelevant;
        this.isObsolete = !isRelevant;
    }
}
