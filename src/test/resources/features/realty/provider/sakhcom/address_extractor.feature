Feature: Extraction of the apartment address from the offer page
  In order to gather meaningful property details
  As a Developer
  I should be able to get property address

  Scenario: Extraction from a complex DOM node
    Given the following components of a property address on Sakh.com
      | Южно-Сахалинск, 1 мкр., |
      | улица Хабаровская, 42   |
    When I pass the document to the Sakh.com address extractor
    Then I must get that very Sakh.com property address

  Scenario: Extraction from a simple DOM node
    Given just "Южно-Сахалинск, пр. Мира, 300" as a Sakh.com property address
    When I pass the document to the Sakh.com address extractor
    Then I must get that very Sakh.com property address
