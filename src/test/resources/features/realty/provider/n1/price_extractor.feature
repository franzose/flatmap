Feature: Extraction of the apartment price from the offer page
  In order to gather meaningful property details
  As a Developer
  I should be able to get apartment price

  Scenario Outline: Extraction
    Given there is an N1 offer page where property price is "<given>"
    When I pass the document to the N1 price extractor
    Then I must get <expected> as the property price
    Examples:
      | given            | expected |
      | 3 000 000 ₽      | 3000000  |
      | 15 000 ₽ в месяц | 15000    |