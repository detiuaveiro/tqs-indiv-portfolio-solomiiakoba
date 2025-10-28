Feature: Library book search

  Background:
    Given a library exists

  Scenario: Find books by author
    Given a book with the title "Clean Code", written by "Robert C. Martin", published in 2008-08-01
    And a book with the title "The Pragmatic Programmer", written by "Andrew Hunt", published in 1999-10-30
    When the customer searches for books by author "Robert C. Martin"
    Then the search results should contain 1 book
    And the book title should be "Clean Code"

  Scenario: No books found for unknown author
    When the customer searches for books by author "Unknown Author"
    Then the search results should contain 0 books

  Scenario: Find books published between two dates
    Given a book with the title "One good book", written by "Anonymous", published in 2013-03-12
    And a book with the title "Some other book", written by "Tim Tomson", published in 2020-08-23
    When the customer searches for books published between 2010-01-01 and 2015-12-31
    Then the search results should contain 1 book
    And the book title should be "One good book"

  Scenario: Find books by author with data table
    Given the following books exist:
      | title                   | author             | published   |
      | Clean Code              | Robert C. Martin   | 2008-08-01 |
      | The Pragmatic Programmer| Andrew Hunt        | 1999-10-30 |
    When the customer searches for books by author "Robert C. Martin"
    Then the search results should contain 1 book
    And the book title should be "Clean Code"