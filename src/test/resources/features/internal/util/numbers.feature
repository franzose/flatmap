Feature: Parsing numbers from strings
  In order to work with numbers
  As a Developer
  I should be able to retrieve numbers from arbitrary strings

  Scenario Outline: Parsing integers
    Given I have a string "<str>" containing a number
    When I parse integer using the numbers utility
    Then I must get <expected> integer back
    Examples:
      | str               | expected |
      | foo 1234 bar      | 1234     |
      | foo 1 234 567 bar | 1234567  |
      | foo 1 234.567 bar | 1234567  |
      | foo 1,234.567 bar | 1234567  |

  Scenario Outline: Parsing doubles
    Given I have a string "<str>" containing a number
    When I parse double using the numbers utility
    Then I must get <expected> double back
    Examples:
      | str               | expected  |
      | foo 1 234 567 bar | 1234567   |
      | foo 1 234.56 bar  | 1234.56   |
      | foo 1 234,56 bar  | 1234.56   |