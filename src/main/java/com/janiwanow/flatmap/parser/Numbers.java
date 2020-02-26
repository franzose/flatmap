package com.janiwanow.flatmap.parser;

/**
 * A simple utility class to retrieve numbers from strings.
 */
public class Numbers {
    /**
     * Tries to parse an integer from the given string.
     *
     * @param str String containing an integer somewhere
     * @return parsed integer
     */
    public static int parseInt(String str) {
        return Integer.parseInt(str.replaceAll("[^0-9]", ""));
    }

    /**
     * Tries to parse a double from the given string.
     *
     * @param str String containing an double somewhere
     * @return parsed double
     */
    public static double parseDouble(String str) {
        return Double.parseDouble(str
            .replaceAll(",+", ".")
            .replaceAll("[^0-9.]", "")
        );
    }
}
