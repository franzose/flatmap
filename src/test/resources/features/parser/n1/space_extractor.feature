Feature: Extraction of apartment area details from the offer page
  In order to gather meaningful apartment information
  As a Developer
  I should be able to get apartment area details

  Scenario: Extraction
    Given the following apartment area information
      | 146 м² |
      | 78 м²  |
      | 18 м²  |
    When I pass the document to the space extractor
    Then I must get 146 square meters of the total area
    And I must get 78 square meters of the living space
    And I must get 18 square meters of the kitchen area