package com.janiwanow.flatmap.http;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.MalformedURLException;
import java.net.URL;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.janiwanow.flatmap.WireMockPathToURL.toAbsoluteURL;
import static org.junit.jupiter.api.Assertions.*;

public class JsoupConnectionSteps {
    private JsoupConnection connection;
    private URL url;

    @Given("I configured JsoupConnection")
    public void configureConnection() {
        connection = new JsoupConnection(new JsoupConnection.Options(3, 1500));
    }

    @When("I requested {string} using JsoupConnection")
    public void requestUrl(String path) throws MalformedURLException {
        this.url = toAbsoluteURL(path);
        prepareStub(path);
    }

    private static void prepareStub(String url) {
        stubFor(get(urlEqualTo(url))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/html")
                .withBody("JsoupConnection!")));
    }

    @Then("I must successfully get an HTML document")
    public void ensureThereAreNoErrors() {
        assertDoesNotThrow(() -> connection.fetch(url));
    }
}
