package com.janiwanow.flatmap.parser;

import com.janiwanow.flatmap.http.Delay;

import java.util.Objects;

/**
 * Website parser options.
 */
public final class ParserOptions {
    public final Delay delay;
    public final int pages;

    public ParserOptions(Delay delay, int pages) {
        Objects.requireNonNull(delay, "Delay must not be null.");
        this.delay = delay;
        this.pages = Math.max(1, pages);
    }
}
