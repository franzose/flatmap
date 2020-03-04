Feature: Extraction of the apartment price from the offer page
  In order to gather meaningful apartment information
  As a Developer
  I should be able to get apartment price

  Scenario: Extraction
    Given there is a Sakh.com offer page where apartment price is "6 150 000"
    When I pass the document to the Sakh.com price extractor
    Then I must get 6150000 as the Sakh.com apartment price
