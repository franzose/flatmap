package com.janiwanow.flatmap.offer;

import java.net.URI;

/**
 * Interface for all checkers.
 *
 * <p>The sole purpose of the service is to check whether an offer is still relevant or not.
 * This check allows to keep an interactive map up-to-date by simply removing
 * those property details which offers has become obsolete.
 */
public interface RelevanceChecker {
    /**
     * Checks an offer for relevance/obsolescence.
     *
     * @param url the URL that's going to be checked
     * @return a result of the check
     */
    RelevanceCheckResult check(URI url);

    /**
     * Determines whether this checker supports the given URI.
     *
     * @param url the URL that's going to be checked
     * @return "true" if supports, "false" otherwise
     */
    boolean supports(URI url);
}
