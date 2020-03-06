Feature: Building valid sakh.com URLs
  In order to parse sakh.com
  As a Developer
  I should be able to get a set of valid URLs

  Scenario: Building URLs
    Given I want to get sakh.com URLs of the first 2 pages
    When I use SakhcomURLs for that purpose
    Then I must get a set of valid sakh.com URLs