package com.janiwanow.flatmap.console;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.janiwanow.flatmap.console.event.PropertyDetailsParsed;
import com.janiwanow.flatmap.data.PropertyDetails;
import com.janiwanow.flatmap.http.Delay;
import com.janiwanow.flatmap.http.HttpConnectionBuilder;
import com.janiwanow.flatmap.internal.console.Command;
import com.janiwanow.flatmap.internal.eventbus.EventDispatcher;
import com.janiwanow.flatmap.parser.Pagination;
import com.janiwanow.flatmap.parser.ParserOptions;
import com.janiwanow.flatmap.parser.WebsiteParser;

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
    @Parameter(description = "ID of the website to parse. Study the com.janiwanow.flatmap.parser.WebsiteParser interface for more information")
    private String websiteId = "";

    @Parameter(
        names = {"--attempts", "--retries"},
        description = "How many times parser should try to reconnect in case of a failure"
    )
    private int retries = Integer.parseInt(ENV.get("HTTP_CONNECTION_RETRIES", "3"));

    @Parameter(names = "--timeout", description = "HTTP connection timeout")
    private int timeout = Integer.parseInt(ENV.get("HTTP_CONNECTION_TIMEOUT", "5000"));

    @Parameter(names = "--pages", description = "Number of the offer list pages to traverse")
    private int pages = Integer.parseInt(ENV.get("PAGES", "20"));

    @Parameter(names = "--start-from", description = "Initial page number to start parsing from")
    private int startFrom = Integer.parseInt(ENV.get("START_FROM", "1"));

    @Parameter(names = "--delay-min", description = "Minimum delay between HTTP connections")
    private int minDelay = Integer.parseInt(ENV.get("DELAY_MIN", String.valueOf(Delay.MIN_DEFAULT)));

    @Parameter(names = "--delay-max", description = "Maximum delay between HTTP connections")
    private int maxDelay = Integer.parseInt(ENV.get("DELAY_MAX", String.valueOf(Delay.MAX_DEFAULT)));

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
     *     <li>Dispatch an event to let subscribers decide what to do next
     * </ol>
     */
    @Override
    public void execute() {
        var conn = http.retries(retries).timeout(timeout).build();
        var options = new ParserOptions(
            new Delay(minDelay, maxDelay),
            new Pagination(startFrom, pages)
        );

        parsers
            .stream()
            .filter(this::filterByWebsiteId)
            .map(parser -> parser.parse(conn, options))
            .forEach(this::dispatchEvent);
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

    /**
     * Dispatches an event once a parser finished its job.
     *
     * @param details fetched property details
     */
    private void dispatchEvent(Set<PropertyDetails> details) {
        if (details.size() > 0) {
            var arg = details.toArray(new PropertyDetails[0]);
            dispatcher.dispatch(new PropertyDetailsParsed(arg));
        }
    }
}
