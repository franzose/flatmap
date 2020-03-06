package com.janiwanow.flatmap.parser.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.janiwanow.flatmap.cli.Command;
import com.janiwanow.flatmap.data.PropertyDetails;
import com.janiwanow.flatmap.event.EventDispatcher;
import com.janiwanow.flatmap.http.HttpConnectionBuilder;
import com.janiwanow.flatmap.parser.WebsiteParser;

import java.util.Collection;
import java.util.Set;

import static com.janiwanow.flatmap.util.Env.ENV;

/**
 * Console command to parse websites and get property details from them.
 */
@Parameters(commandNames = "parse", commandDescription = "Parses websites to get property details")
public final class ParseWebsitesCommand implements Command {
    private final EventDispatcher dispatcher;
    private final HttpConnectionBuilder http;
    private final Set<WebsiteParser> parsers;

    /**
     * websiteId is used to allow users to parse specific websites and not everything.
     *
     * @see WebsiteParser#supports(String)
     */
    @Parameter()
    private String websiteId = "";

    @Parameter(names = {"--attempts", "--retries"})
    private int retries = Integer.parseInt(ENV.get("HTTP_CONNECTION_RETRIES", "3"));

    @Parameter(names = {"--pages"})
    private int pages = Integer.parseInt(ENV.get("PAGES", "20"));

    public ParseWebsitesCommand(
        EventDispatcher dispatcher,
        HttpConnectionBuilder http,
        Set<WebsiteParser> parsers
    ) {
        this.dispatcher = dispatcher;
        this.http = http;
        this.parsers = parsers;
    }

    /**
     * Parses websites to gather property details.
     *
     * <p>Under the hood, the process is relatively simple:
     * <ol>
     *     <li>Take all available parsers
     *     <li>Filter out parsers by the websiteId if the user wants to parse a specific website
     *     <li>Parse websites using the rest of the parsers
     *     <li>Just in case, ensure fetched data is unique
     *     <li>Dispatch an event to let subscribers decide what to do next
     * </ol>
     */
    @Override
    public void execute() {
        var conn = http.retries(retries).build();

        var info = parsers
            .stream()
            .filter(this::filterByWebsiteId)
            .map(parser -> parser.parse(conn, pages))
            .flatMap(Collection::stream)
            .distinct()
            .toArray(PropertyDetails[]::new);

        if (info.length > 0) {
            dispatcher.dispatch(new PropertyDetailsParsed(info));
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
