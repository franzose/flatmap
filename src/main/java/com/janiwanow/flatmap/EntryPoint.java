package com.janiwanow.flatmap;

import com.janiwanow.flatmap.cli.Application;
import com.janiwanow.flatmap.cli.CommandNotFoundException;
import com.janiwanow.flatmap.db.HikariConnectionFactory;
import com.janiwanow.flatmap.db.cli.PropertyDetailsListener;
import com.janiwanow.flatmap.db.cli.PurgeDatabaseCommand;
import com.janiwanow.flatmap.db.cli.SetupDatabaseCommand;
import com.janiwanow.flatmap.event.EventDispatcher;
import com.janiwanow.flatmap.event.GreenRobotEventDispatcher;
import com.janiwanow.flatmap.http.JsoupHttpConnection;
import com.janiwanow.flatmap.parser.cli.ParseWebsitesCommand;
import com.janiwanow.flatmap.parser.impl.n1.N1Parser;
import com.janiwanow.flatmap.parser.impl.sakhcom.SakhcomParser;
import org.greenrobot.eventbus.EventBus;

import java.util.Set;

import static com.janiwanow.flatmap.util.Env.ENV;

/**
 * The entry point of the console application.
 */
public final class EntryPoint {
    public static void main(String[] args) throws CommandNotFoundException {
        var dbConnectionFactory = HikariConnectionFactory.getInstance();
        var app = new Application(Set.of(
            new SetupDatabaseCommand(dbConnectionFactory),
            new PurgeDatabaseCommand(dbConnectionFactory),
            setUpParsingCommand()
        ));

        app.run(args);
    }

    private static ParseWebsitesCommand setUpParsingCommand() {
        var separator = ENV.get("SEPARATOR", ";");

        return new ParseWebsitesCommand(
            setUpEventDispatcher(),
            JsoupHttpConnection.builder(),
            Set.of(
                new N1Parser(Set.of(ENV.get("N1_CITIES", "novosibirsk").split(separator))),
                new SakhcomParser(
                    JsoupHttpConnection.builder(),
                    Set.of(ENV.get("SAKHCOM_CITIES", "ys").split(separator))
                )
            )
        );
    }

    private static EventDispatcher setUpEventDispatcher() {
        var eventBus = EventBus.getDefault();

        eventBus.register(new PropertyDetailsListener(HikariConnectionFactory.getInstance()));

        return new GreenRobotEventDispatcher(eventBus);
    }
}
