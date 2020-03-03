package com.janiwanow.flatmap.parser;

import com.janiwanow.flatmap.data.ApartmentInfo;
import com.janiwanow.flatmap.http.HttpConnection;

import java.util.Set;

/**
 * Website parser.
 *
 * <p>Instances of this interface are assumed to act as {@link ApartmentInfoFetcher} wrappers,
 * however they are not forced to do that and thus can implement parsing logic on their own.
 *
 * <p>The idea behind WebsiteParser and its derivatives is to encapsulate any setup required
 * to actually fetch apartment information. Setup may include determining which URLs
 * to request, which CSS selectors to use etc.
 */
public interface WebsiteParser {
    /**
     * Utilizes the given HTTP connection to retrieve information about the apartments.
     *
     * @param connection an HTTP connection to use for parsing
     * @return a set of apartment information details
     */
    Set<ApartmentInfo> parse(HttpConnection connection);

    /**
     * Checks whether this parser is able to parse the website with given ID.
     *
     * <p>Identifiers are used to pick a particular parser
     * if the user wants to parse a particular website.
     *
     * <p>While any valid string can act as an identifier, it is recommended to use
     * the name of the sub-package inside {@link com.janiwanow.flatmap.parser}.
     *
     * @param websiteId a string representing a particular website
     * @return "true" if the parser supports the website, "false" otherwise
     */
    boolean supports(String websiteId);
}
