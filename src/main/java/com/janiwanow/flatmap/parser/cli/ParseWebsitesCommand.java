package com.janiwanow.flatmap.parser.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.janiwanow.flatmap.cli.Command;
import com.janiwanow.flatmap.data.ApartmentInfo;
import com.janiwanow.flatmap.event.EventDispatcher;
import com.janiwanow.flatmap.http.HttpConnection;
import com.janiwanow.flatmap.parser.WebsiteParser;

import java.util.Collection;
import java.util.Set;

/**
 * Console command to parse websites and get apartment information from them.
 */
@Parameters(commandNames = "parse", commandDescription = "Parses websites to get apartment information")
public final class ParseWebsitesCommand implements Command {
    private final EventDispatcher dispatcher;
    private final HttpConnection httpConnection;
    private final Set<WebsiteParser> parsers;

    /**
     * websiteId is used to allow users to parse specific websites and not everything.
     *
     * @see WebsiteParser#supports(String)
     */
    @Parameter()
    private String websiteId = "";

    public ParseWebsitesCommand(
        EventDispatcher dispatcher,
        HttpConnection httpConnection,
        Set<WebsiteParser> parsers
    ) {
        this.dispatcher = dispatcher;
        this.httpConnection = httpConnection;
        this.parsers = parsers;
    }

    /**
     * Parses websites to gather apartment information.
     *
     * <p>Under the hood, the process is relatively simple:
     * <ol>
     *     <li>Take all available parsers
     *     <li>Filter out parsers by the websiteId if the user wants to parse a specific website
     *     <li>Parse websites using the rest of the parsers
     *     <li>Just in case, ensure fetched apartment data is unique
     *     <li>Dispatch an event to let subscribers decide what to do next
     * </ol>
     */
    @Override
    public void execute() {
        var info = parsers
            .stream()
            .filter(this::filterByWebsiteId)
            .map(parser -> parser.parse(httpConnection))
            .flatMap(Collection::stream)
            .distinct()
            .toArray(ApartmentInfo[]::new);

        if (info.length > 0) {
            dispatcher.dispatch(new ApartmentInfoParsed(info));
        }
    }

    /**
     * Decides whether the given parser can be used.
     *
     * <p>By default, websiteId is empty which means that the command
     * should use all available {@link WebsiteParser} implementations.
     *
     * <p>If websiteId is present, i.e. the user wants to parse a particular website,
     * we should make sure the given parser supports this website.
     *
     * @param parser a parser in the list
     * @return "true" if the parser is supported, "false" otherwise
     */
    private boolean filterByWebsiteId(WebsiteParser parser) {
        return (
            websiteId.isEmpty() ||
            websiteId.isBlank() ||
            parser.supports(websiteId)
        );
    }
}
