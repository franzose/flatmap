package com.janiwanow.flatmap.parser;

/**
 * Parser pagination options.
 */
public final class Pagination {
    public final int startFrom;
    public final int traverse;
    public final int end;

    /**
     * @param startFrom the initial page number to start parsing from
     * @param traverse number of offer list pages to go through
     */
    public Pagination(int startFrom, int traverse) {
        this.startFrom = Math.max(1, startFrom);
        this.traverse = Math.max(1, traverse);
        this.end = this.startFrom + this.traverse - 1;
    }
}
