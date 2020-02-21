Feature: Fetching HTML documents using DocumentFetcher
  In order to process apartment information
  As a Developer
  I should be able to fetch HTML documents using DocumentFetcher

  Scenario: Fetching a document asynchronously
    Given I scheduled a request to "https://example.com"
    When the document was fetched successfully
    Then I must get that HTML document

  Scenario: Fetching multiple documents asynchronously
    Given I scheduled multiple fetching requests
      | https://example.com |
      | https://foobarqux.com |
      | https://parampampam.ru |
    When the documents are fetched
    Then I must get those HTML documents

  Scenario: Skipping empty responses
    Given There are 2 URLs not responding
    And I scheduled 4 fetching requests
    When the documents are fetched
    Then I must get only 2 HTML documents