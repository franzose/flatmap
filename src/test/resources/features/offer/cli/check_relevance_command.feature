Feature: Command to check relevance of the property offers

  Scenario:
    Given the database contains property details from the following URLs
      | https://example.com/view/1 |
      | https://example.com/view/2 |
      | https://example.com/view/3 |
      | https://example.com/view/4 |
    And the following property offers have become obsolete
      | https://example.com/view/1 |
      | https://example.com/view/2 |
    When I run the CheckRelevanceCommand command
    Then the following property offers must be marked obsolete
      | https://example.com/view/1 |
      | https://example.com/view/2 |
    And the following property offers must be left unchanged
      | https://example.com/view/3 |
      | https://example.com/view/4 |