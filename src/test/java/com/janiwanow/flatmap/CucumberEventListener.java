package com.janiwanow.flatmap;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

public class CucumberEventListener implements ConcurrentEventListener {
    private WireMockServer server = new WireMockServer();

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, event -> server.start());
        publisher.registerHandlerFor(TestCaseFinished.class, event -> WireMock.reset());
        publisher.registerHandlerFor(TestRunFinished.class, event -> server.stop());
    }
}
