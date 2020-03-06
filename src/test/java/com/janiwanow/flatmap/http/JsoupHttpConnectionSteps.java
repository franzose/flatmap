package com.janiwanow.flatmap.http;

import com.github.tomakehurst.wiremock.http.Fault;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.URI;
import java.net.URISyntaxException;

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

    @Given("I configured JsoupHttpConnection")
    public void configureConnection() {
        connection = JsoupHttpConnection.builder().retries(RETRIES).timeout(TIMEOUT).build();
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
}
