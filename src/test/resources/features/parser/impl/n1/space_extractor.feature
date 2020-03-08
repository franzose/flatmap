Feature: Extraction of apartment area details from the offer page
  In order to gather meaningful property details
  As a Developer
  I should be able to get property are details

  Scenario Outline: Extraction
    Given the following N1 property area information
      | <total> | <living> | <kitchen> |
    When I pass the document to the space extractor
    Then I must get <expected_total> square meters of the total area
    And I must get <expected_living> square meters of the living space
    And I must get <expected_kitchen> square meters of the kitchen area
    Examples:
      | total  | living | kitchen | expected_total | expected_living | expected_kitchen |
      | 146 м² | 78 м²  | 18 м²   | 146            | 78              | 18               |
      | 150 м² |        |         | 150            | 150             | 0                |
