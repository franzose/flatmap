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
    Given database already contains property details from "https://example.dev"
    When I put property details from the same URL
    Then the database must still contain 1 property details record

  @db @outline
  Scenario Outline: Bypassing invalid property details
    Given I obtained some property details
      # offer_url
      | https://example.com |
      # address
      | Южно-Сахалинск, проспект Мира, 121 |
      # total_area
      | <total_area> |
      # living_space
      | <living_space> |
      # kitchen_area
      | 9 |
      # rooms
      | <rooms> |
      # price_amount
      | <price_amount> |
      # price_currency
      | RUB |
    When I put the property details into the database
    Then the database must still contain 0 property details records
    Examples:
      | total_area | living_space | rooms | price_amount |
      | 0          | 50           | 2     | 8000000      |
      | 80         | 0            | 2     | 8000000      |
      | 80         | 50           | 0     | 8000000      |
      | 80         | 50           | 2     | 0            |