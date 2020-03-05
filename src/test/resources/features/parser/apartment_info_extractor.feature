Feature: Extraction of the apartment information
  In order to process apartment information
  As a Developer
  I should be able to get an apartment information from offer pages

  Scenario: Extraction
    Given there is an HTML document of the "https://example.com/apartment/1234" offer page
    When I pass the HTML document to the apartment information extractor
    Then I must get a valid apartment information

  Scenario: No info if document is empty or incomplete
    Given there is an incomplete HTML document of an offer page
    When I pass the HTML document to the apartment information extractor
    Then I must get an empty apartment information