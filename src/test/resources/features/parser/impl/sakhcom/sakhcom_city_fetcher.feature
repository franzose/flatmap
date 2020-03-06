Feature: Fetching property details from a particular city
  In order to parse sakh.com properly
  As a Developer
  I should be able to choose a particular city

  Scenario: Parsing a city
    Given I want to get property details from "korsakov"
    When I pass the city to the Sakh.com city fetcher
    Then I must get a set of property details from that city