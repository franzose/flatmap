Feature: Extraction of apartment area details from the offer page
  In order to gather meaningful property details
  As a Developer
  I should be able to get property area details

  Scenario Outline: Extraction
    Given the "<area>" as property area on Sakh.com
    When I pass the document to the Sakh.com area extractor
    Then I must get <total> square meters of the total area from Sakh.com
    And I must get <living> square meters of the living space from Sakh.com
    And I must get <kitchen> square meters of the kitchen area from Sakh.com
    Examples:
      | total | living | kitchen | area                                       |
      | 48    | 30     | 6       | Площадь: 48 м² (жилая: 30 м², кухня: 6 м²) |
      | 48    | 30     | 0       | Площадь: 48 м² (жилая: 30 м²)              |
      | 48    | 48     | 6       | Площадь: 48 м² (кухня: 6 м²)               |
      | 48    | 48     | 0       | Площадь: 48 м²                             |