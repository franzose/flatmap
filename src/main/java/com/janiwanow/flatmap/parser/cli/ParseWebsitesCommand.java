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

    private boolean filterByWebsiteId(WebsiteParser parser) {
        return (
            websiteId.isEmpty() ||
            websiteId.isBlank() ||
            parser.supports(websiteId)
        );
    }
}
