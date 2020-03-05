Feature: Extraction of rooms number from the offer page
  In order to gather meaningful apartment information
  As a Developer
  I should be able to get the number of rooms in the apartment

  Scenario: Extraction
    Given there is an Sakh.com offer page where rooms are "2 комнаты, брежневка"
    When I pass the document to the Sakh.com rooms extractor
    Then I must get 2 as the number of rooms of the Sakh.com offer