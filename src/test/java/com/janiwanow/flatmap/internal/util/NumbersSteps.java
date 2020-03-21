package com.janiwanow.flatmap.internal.util;

import com.janiwanow.flatmap.internal.util.Numbers;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumbersSteps {
    private String str;
    private int actualInteger;
    private double actualDouble;

    @Given("I have a string {string} containing a number")
    public void setUpString(String str) {
        this.str = str;
    }

    @When("I parse integer using the numbers utility")
    public void parseInt() {
        actualInteger = Numbers.parseInt(str);
    }

    @When("I parse double using the numbers utility")
    public void parseDouble() {
        actualDouble = Numbers.parseDouble(str);
    }

    @Then("I must get {int} integer back")
    public void ensureIntegerIsParsed(int expectedInteger) {
        assertEquals(expectedInteger, actualInteger);
    }

    @Then("I must get {double} double back")
    public void ensureDoubleIsParsed(double expectedDouble) {
        assertEquals(expectedDouble, actualDouble);
    }
}
