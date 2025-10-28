Feature: Online Library Search

  Background:
    Given the user is on the online bookstore

  Scenario: Search for a specific book
    When the user searches for "Harry Potter"
    Then a book titled "Harry Potter and the Sorcerer's Stone" by "J.K. Rowling" should appear in the results

  Scenario: Search for an unknown book
    When the user searches for "None"
    Then a message "Looks like we don't have what you are looking for" should be displayed
