Feature: Building valid sakh.com URLs
  In order to parse sakh.com
  As a Developer
  I should be able to get a set of valid URLs

  Scenario: Building URLs
    Given I wanted to get some URLs from sakh.com
    And to parse 2 pages of sakh.com
    When I use SakhcomURLs for that purpose
    Then I must get a set of valid sakh.com URLs

  Scenario: Starting from an arbitrary page
    Given I wanted to get some URLs from sakh.com
    And to start parsing sakh.com from 5 page
    And to parse 5 pages of sakh.com
    When I use SakhcomURLs for that purpose
    Then I must get sakh.com URLs from 5 to 9 page