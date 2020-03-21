Feature: Console application
  In order to run multiple tasks from the same program
  As a Developer
  I should be able to pass arguments to it and run commands

  Scenario: Command execution
    Given I registered an "app" command in the console application
    When I run the "app" command
    Then the command should finish successfully

  Scenario: Execution of a command on an empty application
    Given I did not register any command in the console application
    When I run a "foo" command
    Then the console application should warn that the command was not found

  Scenario: Execution of an unknown command
    Given I registered an "app" command in the console application
    When I run a "foo" command
    Then the console application should warn that the command was not found