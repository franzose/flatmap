package com.janiwanow.flatmap.internal.http;

import com.github.tomakehurst.wiremock.http.Fault;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.janiwanow.flatmap.WireMockPathToURL.toAbsoluteURL;
import static org.junit.jupiter.api.Assertions.*;

public class JsoupHttpConnectionSteps {
    private static final String SUCCESS_MESSAGE = "JsoupHttpConnection!";
    private static final int RETRIES = 5;
    private static final int TIMEOUT = 3000;
    private HttpConnection connection;
    private URI url;
    private String path;
    private HttpConnectionBuilder actualBuilder;
    private int expectedRetries;
    private int expectedTimeout;
    private Map<String, String> expectedCookies;

    @Given("I configured JsoupHttpConnection")
    public void configureConnection() {
        connection = JsoupHttpConnection.builder().retries(RETRIES).timeout(TIMEOUT).build();
    }

    @Given("I set the following options for JsoupHttpConnection")
    public void configureConnection(DataTable data) {
        var rows = data.asMaps(String.class, String.class);
        var options = rows.get(0);

        expectedRetries = Integer.parseInt(options.get("retries").toString());
        expectedTimeout = Integer.parseInt(options.get("timeout").toString());

        var cookies = options.get("cookies").toString().split("=");
        expectedCookies = Map.of(cookies[0], cookies[1]);

        connection = JsoupHttpConnection
            .builder()
            .retries(expectedRetries)
            .timeout(expectedTimeout)
            .cookies(expectedCookies)
            .build();
    }

    @When("I requested {string} using JsoupHttpConnection")
    public void requestUrl(String path) throws URISyntaxException {
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

    @When("I create a new HTTP connection builder")
    public void createNewBuilder() {
        actualBuilder = connection.newBuilder();
    }

    @Then("I must successfully get an HTML document")
    public void ensureThereAreNoErrors() {
        assertDoesNotThrow(() -> {
            var document = connection.fetch(url);
            assertTrue(document.isPresent());

            var body = document.get().body();
            assertTrue(body.hasText());
            assertEquals(SUCCESS_MESSAGE, body.html());
        });
    }

    @Then("I should not get any document")
    public void ensureThereIsNoDocument() {
        assertDoesNotThrow(() -> {
            var document = connection.fetch(url);
            assertTrue(document.isEmpty());
        });
    }

    @Then("it must inherit those HTTP connection options")
    public void ensureConnectionOptionsWereInherited() {
        var expectedResult = String.format(
            "{retries:%d, timeout:%d, cookies:%s}",
            expectedRetries,
            expectedTimeout,
            expectedCookies
        );

        assertEquals(expectedResult, actualBuilder.toString());
    }
}
