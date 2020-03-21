Feature: Purging database
  In order to clear the database
  As a Developer
  I should be able to drop all its tables at once

  @db_purger
  Scenario: Purging database
    Given the database had the following tables
      | foo |
      | bar |
      | qux |
    And I registered the "db:purge" command in the console application
    When I run the "db:purge" command
    Then the database must be empty