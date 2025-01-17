package com.janiwanow.flatmap.internal.http;

/**
 * Range of delays in milliseconds between HTTP requests.
 */
public final class DelayRange {
    public static final int MIN_DEFAULT = 200;
    public static final int MAX_DEFAULT = 1500;
    public final int min;
    public final int max;

    /**
     * @param min minimum delay
     * @param max maximum delay
     */
    public DelayRange(int min, int max) {
        this.min = Math.max(1, min);
        this.max = Math.max(1, max);
    }
}
