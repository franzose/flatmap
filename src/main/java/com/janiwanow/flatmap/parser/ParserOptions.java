package com.janiwanow.flatmap.parser;

import com.janiwanow.flatmap.http.DelayRange;

import java.util.Objects;

/**
 * Website parser options.
 */
public final class ParserOptions {
    public final DelayRange delay;
    public final Pagination pagination;

    public ParserOptions(DelayRange delay, Pagination pagination) {
        Objects.requireNonNull(delay, "DelayRange must not be null.");
        Objects.requireNonNull(pagination, "Pagination must not be null.");
        this.delay = delay;
        this.pagination = pagination;
    }
}
