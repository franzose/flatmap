Feature: N1 offer relevance checker

  Scenario: Relevant property offer
    Given I got an active N1 property offer
    When I check it using N1 offer relevance checker
    Then the N1 property offer must be relevant

  Scenario: Non-existent property offer
    Given there were no N1 property offer
    When I check it using N1 offer relevance checker
    Then the N1 property offer must be obsolete
