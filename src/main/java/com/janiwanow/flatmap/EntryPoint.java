package com.janiwanow.flatmap;

import com.janiwanow.flatmap.console.CheckRelevanceCommand;
import com.janiwanow.flatmap.console.ParseWebsitesCommand;
import com.janiwanow.flatmap.console.PurgeDatabaseCommand;
import com.janiwanow.flatmap.console.SetupDatabaseCommand;
import com.janiwanow.flatmap.http.JsoupHttpConnection;
import com.janiwanow.flatmap.internal.console.Application;
import com.janiwanow.flatmap.internal.console.CommandNotFoundException;
import com.janiwanow.flatmap.internal.eventbus.EventDispatcher;
import com.janiwanow.flatmap.internal.eventbus.GreenRobotEventDispatcher;
import com.janiwanow.flatmap.internal.sql.DbConnectionFactory;
import com.janiwanow.flatmap.internal.sql.HikariDbConnectionFactory;
import com.janiwanow.flatmap.offer.db.FetchURLsByChunks;
import com.janiwanow.flatmap.offer.db.MarkObsolete;
import com.janiwanow.flatmap.offer.db.PropertyDetailsListener;
import com.janiwanow.flatmap.offer.impl.N1RelevanceChecker;
import com.janiwanow.flatmap.offer.impl.SakhcomRelevanceChecker;
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
        var db = HikariDbConnectionFactory.INSTANCE;
        var app = new Application(Set.of(
            new SetupDatabaseCommand(db),
            new PurgeDatabaseCommand(db),
            setUpParsingCommand(),
            setUpCheckRelevanceCommand(db)
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
                new SakhcomParser(Set.of(ENV.get("SAKHCOM_CITIES", "ys").split(separator)))
            )
        );
    }

    private static CheckRelevanceCommand setUpCheckRelevanceCommand(DbConnectionFactory db) {
        return new CheckRelevanceCommand(
            new FetchURLsByChunks(db),
            new MarkObsolete(db),
            Set.of(
                N1RelevanceChecker.getDefault(JsoupHttpConnection.builder().build()),
                SakhcomRelevanceChecker.getDefault(JsoupHttpConnection.builder().build())
            )
        );
    }

    private static EventDispatcher setUpEventDispatcher() {
        var eventBus = EventBus.getDefault();

        eventBus.register(new PropertyDetailsListener(HikariDbConnectionFactory.INSTANCE));

        return new GreenRobotEventDispatcher(eventBus);
    }
}
