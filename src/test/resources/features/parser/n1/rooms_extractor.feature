Feature: Extraction of rooms number from the offer page
  In order to gather meaningful apartment information
  As a Developer
  I should be able to get the number of room in the apartment

  Scenario: Extraction
    Given there is an N1 offer page entitled "продам 2-к"
    When I pass the document to the N1 rooms extractor
    Then I must get 2 as the number of rooms