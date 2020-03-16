package com.janiwanow.flatmap.offer;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A data class representing a result of an offer relevance check.
 */
public final class RelevanceCheckResult {
    public final URI url;
    public final LocalDateTime checkedAt;
    public final boolean isRelevant;
    public final boolean isObsolete;

    public static RelevanceCheckResult relevant(URI url) {
        Objects.requireNonNull(url, "URI must not be null.");
        return new RelevanceCheckResult(url, LocalDateTime.now(), true);
    }

    public static RelevanceCheckResult obsolete(URI url) {
        Objects.requireNonNull(url, "URI must not be null.");
        return new RelevanceCheckResult(url, LocalDateTime.now(), false);
    }

    private RelevanceCheckResult(URI url, LocalDateTime checkedAt, boolean isRelevant) {
        this.url = url;
        this.checkedAt = checkedAt;
        this.isRelevant = isRelevant;
        this.isObsolete = !isRelevant;
    }
}
