Feature: Extraction of the property details
  In order to process property details
  As a Developer
  I should be able to get an property details from offer pages

  Scenario: Extraction
    Given there is an HTML document of the "https://example.com/apartment/1234" offer page
    When I pass the HTML document to the property details extractor
    Then I must get valid property details

  Scenario: No info if document is empty or incomplete
    Given there is an incomplete HTML document of an offer page
    When I pass the HTML document to the property details extractor
    Then I must get empty property details