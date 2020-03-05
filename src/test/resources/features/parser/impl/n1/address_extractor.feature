Feature: Extraction of the apartment address from the offer page
  In order to gather meaningful property details
  As a Developer
  I should be able to get apartment address

  Scenario: Extraction
    Given there is an N1 offer page with the following property address components
      | ул. Чкалова          |
      | мкр-н Авиастроителей |
      | Дзержинский район    |
      | Новосибирск          |
    When I pass the document to the N1 address extractor
    Then I must get "Новосибирск, Дзержинский район, мкр-н Авиастроителей, ул. Чкалова" as the N1 property address