package com.janiwanow.flatmap.parser;

import com.janiwanow.flatmap.http.Delay;

import java.util.Objects;

/**
 * Website parser options.
 */
public final class ParserOptions {
    public final Delay delay;
    public final Pagination pagination;

    public ParserOptions(Delay delay, Pagination pagination) {
        Objects.requireNonNull(delay, "Delay must not be null.");
        Objects.requireNonNull(pagination, "Pagination must not be null.");
        this.delay = delay;
        this.pagination = pagination;
    }
}
