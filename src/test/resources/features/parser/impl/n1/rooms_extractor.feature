Feature: Extraction of rooms number from the offer page
  In order to gather meaningful property details
  As a Developer
  I should be able to get the number of rooms

  Scenario: Extraction from the offer title
    Given there is an N1 offer page entitled "продам 2-к"
    When I pass the document to the N1 rooms extractor
    Then I must get 2 rooms for N1 property details

  Scenario: Extraction from the parameter list
    Given there is an N1 offer page
    And that offer has 2 rooms listed among other parameters
    When I pass the document to the N1 rooms extractor
    Then I must get 2 rooms for N1 property details