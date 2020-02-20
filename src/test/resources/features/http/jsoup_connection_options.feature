Feature: Configuring JsoupConnection options
  In order to use JsoupConnection
  As a Developer
  I must have it properly configured

  Scenario Outline: Passing negative or zero retries
    Given I defined an invalid <number> of retries
    When I pass the defined retries to the JsoupConnection options
    Then I must get the default retries value
    Examples:
      | number |
      | -1     |
      | 0      |

  Scenario: Passing timeout which is too low
    Given I defined a too low timeout value
    When I pass the defined timeout to the JsoupConnection options
    Then I must get the default timeout value