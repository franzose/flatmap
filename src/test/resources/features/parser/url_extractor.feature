Feature: URL extraction from the HTML documents
  In order to process apartment information
  As a Developer
  I should be able to grab URLs from the HTML documents

  Scenario Outline: Extraction from a single document
    Given I fetched an HTML document from "<URL>"
    And it contains the following links
      | /foo |
      | /bar |
      | /bar |
      | /qux |
      | /qux |
      | /doo |
      | javascript:void(0) |
    When I pass the document to the extractor
    Then I must get absolute URLs of these paths
      | /foo |
      | /bar |
      | /qux |
      | /doo |
    Examples:
      | URL |
      | https://example.com |
      | https://example.com?page=999 |

  Scenario: Extraction from multiple documents
    Given I fetched an HTML document from "https://example.com" containing the following URLs
      | /foo |
      | /bar |
    And I fetched an HTML document from "https://foobarqux.com" containing the following URLs
      | /qux |
      | /doo |
    When I pass the documents to the extractor
    Then I must get a combined URL list
