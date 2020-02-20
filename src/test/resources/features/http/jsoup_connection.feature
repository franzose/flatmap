Feature: Fetching HTML documents using Jsoup HTTP connection
  In order to process apartment information
  As a Developer
  I should be able to fetch HTML documents using Jsoup HTTP connection

  Background:
    Given I configured JsoupConnection

  Scenario: A successful fetch
    Given I requested "/foo" using JsoupConnection
    Then I must successfully get an HTML document
