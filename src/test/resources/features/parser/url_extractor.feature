Feature: URL extraction from the HTML documents

  Scenario Outline: Extraction from a single document
    Given we fetched an HTML document from "<URL>"
    And it contains a list of links
      | /foo |
      | /bar |
      | /bar |
      | /qux |
      | /qux |
      | /doo |
      | javascript:void(0) |
    When the extractor takes the document
    Then it should return a list of absolute URLs of the following paths
      | /foo |
      | /bar |
      | /qux |
      | /doo |
    Examples:
      | URL |
      | https://example.com |
      | https://example.com?page=999 |

  Scenario: Extraction from multiple documents
    Given the document fetched from "https://example.com" contains the following URLs
      | /foo |
      | /bar |
    And the document fetched from "https://foobarqux.com" contains the following URLs
      | /qux |
      | /doo |
    When the extractor takes these documents
    Then it should return a combined URL list
