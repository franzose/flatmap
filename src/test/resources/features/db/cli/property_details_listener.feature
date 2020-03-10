Feature: Inserting property details into the database
  In order to keep information about the properties
  As a Developer
  I should be able to save their details into the database

  @db
  Scenario: Regular insertion
    Given I obtained some property details
      # offer_url
      | https://example.com |
      # address
      | Южно-Сахалинск, проспект Мира, 121 |
      # total_area
      | 36 |
      # living_space
      | 20 |
      # kitchen_area
      | 9 |
      # rooms
      | 1 |
      # price_amount
      | 5000000 |
      # price_currency
      | RUB |
    When I put the property details into the database
    And I query for the property details from "https://example.com"
    Then I must get those property details

  @db
  Scenario: Duplicate insertion
    Given I have already put the property details obtained from "https://example.dev"
    When I put property details from the same URL
    Then the database must ignore these property details