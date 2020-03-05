Feature: Extraction of the apartment address from the offer page
  In order to gather meaningful property details
  As a Developer
  I should be able to get property address

  Scenario: Extraction
    Given there is a Sakh.com offer page with the following property address components
      | Южно-Сахалинск, 1 мкр., |
      | улица Хабаровская, 42   |
    When I pass the document to the Sakh.com address extractor
    Then I must get "Южно-Сахалинск, 1 мкр., улица Хабаровская, 42" as the Sakh.com property address