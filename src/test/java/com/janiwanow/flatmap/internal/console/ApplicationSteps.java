package com.janiwanow.flatmap.internal.console;

import com.beust.jcommander.Parameters;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ApplicationSteps {
    private final ApplicationContext context;
    private Throwable thrownException = null;

    public ApplicationSteps(ApplicationContext context) {
        this.context = context;
    }

    @Given("I registered an \"app\" command in the console application")
    public void setUpApplication() {
        context.setUpApplication(new AppCommand());
    }

    @Given("I did not register any command in the console application")
    public void setUpEmptyApplication() {
        context.setUpApplication();
    }

    @When("^I run (?:the|a) \"(.*)\" command$")
    public void runCommand(String commandName) {
        try {
            context.application.run(commandName);
        } catch (Throwable e) {
            thrownException = e;
        }
    }

    @Then("the command should finish successfully")
    public void ensureFinishedSuccessfully() {
        assertNull(thrownException, "The command finished unexpectedly.");
    }

    @Then("the console application should warn that the command was not found")
    public void ensureCommandWasNotFound() {
        assertTrue(thrownException instanceof CommandNotFoundException);
    }

    @Parameters(commandNames = "app")
    private static class AppCommand implements Command {
        @Override
        public void execute() {
        }
    }
}
