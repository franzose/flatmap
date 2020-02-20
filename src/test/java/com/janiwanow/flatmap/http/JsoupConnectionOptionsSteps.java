package com.janiwanow.flatmap.http;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;

public class JsoupConnectionOptionsSteps {
    private int retries;
    private int timeout;
    private JsoupConnection.Options options;

    @Given("I defined an invalid {int} of retries")
    public void setNumberOfRetries(int retries) {
        this.retries = retries;
    }

    @Given("I defined a too low timeout value")
    public void setTimeout() {
        timeout = JsoupConnection.Options.MINIMUM_TIMEOUT - 500;
    }

    @When("I pass the defined retries to the JsoupConnection options")
    public void newOptionsWithRetries() {
        options = new JsoupConnection.Options(retries, JsoupConnection.Options.MINIMUM_TIMEOUT);
    }

    @When("I pass the defined timeout to the JsoupConnection options")
    public void newOptionsWithTimeout() {
        options = new JsoupConnection.Options(JsoupConnection.Options.MINIMUM_RETRIES, timeout);
    }

    @Then("I must get the default retries value")
    public void ensureRetriesIsValid() {
        assertEquals(JsoupConnection.Options.MINIMUM_RETRIES, options.retries);
    }

    @Then("I must get the default timeout value")
    public void ensureTimeoutIsValid() {
        assertEquals(JsoupConnection.Options.MINIMUM_TIMEOUT, options.timeout);
    }
}
