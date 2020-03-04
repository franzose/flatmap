Feature: Building valid N1 URLs
  In order to parse the N1 website
  As a Developer
  I should be able to get a set of valid URLs

  Scenario: Building URLs
    Given I want to get URLs of the first 2 pages in the following cities
      | novosibirsk |
      | barnaul     |
    When I use N1URLs for that purpose
    Then I must get a set of valid N1 URLs