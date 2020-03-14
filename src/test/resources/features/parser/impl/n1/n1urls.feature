Feature: Building valid N1 URLs
  In order to parse the N1 website
  As a Developer
  I should be able to get a set of valid URLs

  Scenario: Building URLs
    Given I wanted to get some N1 URLs for the following cities
      | novosibirsk |
      | barnaul     |
    And to parse 2 pages of N1
    When I use N1URLs for that purpose
    Then I must get a set of valid N1 URLs

  Scenario: Starting from an arbitrary page
    Given I wanted to get some N1 URLs
    And to start parsing N1 from 5 page
    And to parse 5 pages of N1
    When I use N1URLs for that purpose
    Then I must get N1 URLs from 5 to 9 page