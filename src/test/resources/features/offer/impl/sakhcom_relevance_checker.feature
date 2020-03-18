Feature: Sakh.com offer relevance checker

  Scenario: Relevant property offer
    Given I got an active Sakh.com property offer
    When I check it using Sakh.com offer relevance checker
    Then the Sakh.com property offer must be relevant

  Scenario: Non-existent property offer
    Given there were no Sakh.com property offer
    When I check it using Sakh.com offer relevance checker
    Then the Sakh.com property offer must be obsolete

  Scenario: Obsolete property offer
    Given I got "Объявление из архива" within a Sakh.com property offer
    When I check it using Sakh.com offer relevance checker
    Then the Sakh.com property offer must be obsolete