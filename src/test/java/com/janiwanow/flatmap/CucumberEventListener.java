package com.janiwanow.flatmap;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

public class CucumberEventListener implements ConcurrentEventListener {
    private WireMockServer server = new WireMockServer();

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        // Run before ALL tests
        publisher.registerHandlerFor(TestRunStarted.class, event -> server.start());

        // Run after EACH feature
        publisher.registerHandlerFor(TestCaseFinished.class, event -> WireMock.reset());

        // Run after ALL tests
        publisher.registerHandlerFor(TestRunFinished.class, event -> server.stop());
    }
}
