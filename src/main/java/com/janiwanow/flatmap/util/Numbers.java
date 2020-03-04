package com.janiwanow.flatmap.util;

import java.util.Objects;

/**
 * A simple utility class to retrieve numbers from strings.
 */
public final class Numbers {
    /**
     * Tries to parse an integer from the given string.
     *
     * @param str String containing an integer somewhere
     * @return parsed integer
     */
    public static int parseInt(String str) {
        Objects.requireNonNull(str, "String must not be null.");

        return Integer.parseInt(str.replaceAll("[^0-9]", ""));
    }

    /**
     * Tries to parse a double from the given string.
     *
     * @param str String containing an double somewhere
     * @return parsed double
     */
    public static double parseDouble(String str) {
        Objects.requireNonNull(str, "String must not be null.");

        return Double.parseDouble(str
            .replaceAll(",+", ".")
            .replaceAll("[^0-9.]", "")
        );
    }
}
