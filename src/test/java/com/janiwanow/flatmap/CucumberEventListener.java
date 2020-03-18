package com.janiwanow.flatmap;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.janiwanow.flatmap.db.TestConnectionFactory;
import com.janiwanow.flatmap.db.cli.PurgeDatabaseCommand;
import com.janiwanow.flatmap.db.cli.SetupDatabaseCommand;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

public class CucumberEventListener implements ConcurrentEventListener {
    private WireMockServer server = new WireMockServer();

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        // Run before ALL tests
        publisher.registerHandlerFor(TestRunStarted.class, this::setUp);

        // Run after EACH feature
        publisher.registerHandlerFor(TestCaseFinished.class, event -> WireMock.reset());

        // Run after ALL tests
        publisher.registerHandlerFor(TestRunFinished.class, this::tearDown);
    }

    private void setUp(TestRunStarted event) {
        server.start();

        new SetupDatabaseCommand(TestConnectionFactory.INSTANCE).execute();
    }

    private void tearDown(TestRunFinished event) {
        server.stop();

        new PurgeDatabaseCommand(TestConnectionFactory.INSTANCE).execute();
    }
}
