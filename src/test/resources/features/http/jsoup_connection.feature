Feature: Fetching HTML documents using Jsoup HTTP connection
  In order to process apartment information
  As a Developer
  I should be able to fetch HTML documents using Jsoup HTTP connection

  Background:
    Given I configured JsoupConnection

  Scenario: A successful fetch
    Given I requested "/success" using JsoupConnection
    When the response is OK
    Then I must successfully get an HTML document

  Scenario: An unexpected HTTP status
    Given I requested "/not_ok" using JsoupConnection
    When the response is not OK
    Then I should not get any document
    And I should see a log message that the response was not OK

  Scenario: Connection timed out
    Given I requested "/time_out" using JsoupConnection
    When the connection times out
    Then I should not get any document
    And I should see a log message that the connection timed out

  Scenario: Connection error
    Given I requested "/error" using JsoupConnection
    When the connection ends up with an error
    Then I should not get any document
    And I should see a log message about the connection error