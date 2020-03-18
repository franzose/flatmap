Feature: Marking property details obsolete
  In order to keep the database up-to-date
  As a Developer
  I should be able to mark property details obsolete

  @db
  Scenario:
    Given the database contains property details from the following URLs
      | https://example.com |
      | https://foobar.dev  |
    When I mark those property details obsolete
    Then 2 properties must be marked obsolete
