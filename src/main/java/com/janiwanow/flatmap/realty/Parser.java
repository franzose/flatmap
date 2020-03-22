package com.janiwanow.flatmap.realty;

import com.janiwanow.flatmap.internal.http.HttpConnection;
import com.janiwanow.flatmap.realty.data.PropertyDetails;

import java.util.Set;

/**
 * Website parser.
 *
 * <p>Instances of this interface are assumed to act as {@link PropertyDetailsFetcher} wrappers,
 * however they are not forced to do that and thus can implement parsing logic on their own.
 *
 * <p>The idea behind Parser and its derivatives is to encapsulate any setup required
 * to actually fetch property details. Setup may include determining which URLs
 * to request, which CSS selectors to use etc.
 */
public interface Parser {
    /**
     * Utilizes the given HTTP connection to retrieve information about the properties.
     *
     * @param connection an HTTP connection to use for parsing
     * @param options parser options
     * @return a set of property details
     */
    Set<PropertyDetails> parse(HttpConnection connection, ParserOptions options);

    /**
     * Checks whether this parser is able to parse the website with given ID.
     *
     * <p>Identifiers are used to pick a particular parser
     * if the user wants to parse a particular website.
     *
     * <p>While any valid string can act as an identifier, it is recommended to use
     * the name of the sub-package inside {@link com.janiwanow.flatmap.realty.provider}.
     *
     * @param websiteId a string representing a particular website
     * @return "true" if the parser supports the website, "false" otherwise
     */
    boolean supports(String websiteId);
}
