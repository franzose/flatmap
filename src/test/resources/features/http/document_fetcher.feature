Feature: Fetching HTML documents using DocumentFetcher
  In order to process apartment information
  As a Developer
  I should be able to fetch HTML documents using DocumentFetcher

  Scenario: Fetching a document asynchronously
    Given I scheduled a request to "https://example.com"
    When fetching completed successfully
    Then I must get that HTML document
