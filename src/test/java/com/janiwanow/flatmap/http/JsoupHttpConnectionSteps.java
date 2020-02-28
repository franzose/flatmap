package com.janiwanow.flatmap.http;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.tomakehurst.wiremock.http.Fault;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Supplier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.janiwanow.flatmap.WireMockPathToURL.toAbsoluteURL;
import static org.junit.jupiter.api.Assertions.*;

public class JsoupHttpConnectionSteps {
    private static final String SUCCESS_MESSAGE = "JsoupHttpConnection!";
    private static final int RETRIES = 5;
    private static final int TIMEOUT = 3000;
    private ListAppender<ILoggingEvent> appender;
    private JsoupHttpConnection connection;
    private URL url;
    private String path;

    @Given("I configured JsoupHttpConnection")
    public void configureConnection() {
        connection = JsoupHttpConnection.builder().retries(RETRIES).timeout(TIMEOUT).build();
    }

    @When("I requested {string} using JsoupHttpConnection")
    public void requestUrl(String path) throws MalformedURLException {
        this.url = toAbsoluteURL(path);
        this.path = path;
    }

    @When("the response is OK")
    public void prepareOkResponseStub() {
        stubFor(get(urlEqualTo(path))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/html")
                .withBody(SUCCESS_MESSAGE)));
    }

    @When("the response is not OK")
    public void prepareNotOkResponseStub() {
        stubFor(get(urlEqualTo(path))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/html")
                .withStatus(403)
                .withBody("Forbidden!")));
    }

    @When("the connection times out")
    public void prepareTimeoutConnectionStub() {
        stubFor(get(urlEqualTo(path))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/html")
                .withFixedDelay(TIMEOUT + 1000)));
    }

    @When("the connection ends up with an error")
    public void prepareConnectionErrorStub() {
        stubFor(get(urlEqualTo(path))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/html")
                .withFault(Fault.CONNECTION_RESET_BY_PEER)));
    }

    @Then("I must successfully get an HTML document")
    public void ensureThereAreNoErrors() {
        appender = getAppender();

        assertDoesNotThrow(() -> {
            var document = connection.fetch(url);
            assertTrue(document.isPresent());

            var body = document.get().body();
            assertTrue(body.hasText());
            assertEquals(SUCCESS_MESSAGE, body.html());

            assertEquals(
                String.format("Finished fetching %s. Attempts: 1", url),
                appender.list.get(2).getFormattedMessage()
            );
        });
    }

    @Then("I should not get any document")
    public void ensureThereIsNoDocument() {
        appender = getAppender();

        assertDoesNotThrow(() -> {
            var document = connection.fetch(url);
            assertTrue(document.isEmpty());
        });
    }

    @And("I should see a log message that the response was not OK")
    public void ensureThereIsHttpStatusExceptionLogMessage() {
        assertLogMessages(() -> String.format("Failed to fetch %s\nStatus code: %d.", url, 403));
    }

    @And("I should see a log message that the connection timed out")
    public void ensureThereIsConnectionTimedOutLogMessage() {
        assertLogMessages(() -> String.format("HttpConnection to %s timed out.", url));
    }

    @And("I should see a log message about the connection error")
    public void ensureThereIsConnectionErrorLogMessage() {
        assertLogMessages(() -> String.format("HttpConnection error while fetching %s.", url));
    }

    private static ListAppender<ILoggingEvent> getAppender() {
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();

        var logger = (Logger) LoggerFactory.getLogger(JsoupHttpConnection.class);
        logger.addAppender(appender);

        return appender;
    }

    private void assertLogMessages(Supplier<String> supplier) {
        for (int attempt = 1; attempt <= RETRIES; attempt++) {
            assertEquals(
                String.format("%s Attempts: %d", supplier.get(), attempt),
                appender.list.get(attempt + 1).getFormattedMessage()
            );
        }
    }
}
