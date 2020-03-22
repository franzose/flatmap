Feature: Fetching URLs from the database by chunks
  In order to process property offer URLs
  As a Developer
  I should be able to fetch them from the database by chunks

  @db
  Scenario:
    Given the database contains property details from the following URLs
      | https://a.com |
      | https://b.com |
      | https://c.com |
      | https://d.com |
      | https://e.com |
      | https://f.com |
      | https://g.com |
    When I fetch URLs from the database by 2 items in a chunk
    Then I should consume 2 URLs at a time