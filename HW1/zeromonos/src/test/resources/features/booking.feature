Feature: Waste Collection Booking System
  As a citizen
  I want to book waste collection services
  So that I can dispose of bulky items

  Background:
    Given the booking system is available
    And the web application is running

  Scenario: Successfully create a waste collection booking
    Given I am on the booking page
    When I select municipality "Aveiro"
    And I enter description "Old sofa and two chairs for collection"
    And I select a valid future date
    And I select time slot "MORNING"
    And I submit the booking form
    Then I should see a success message
    And I should receive an access token

  Scenario: Attempt to book with insufficient description
    Given I am on the booking page
    When I select municipality "Porto"
    And I enter description "Short"
    And I select a valid future date
    And I select time slot "AFTERNOON"
    And I submit the booking form
    Then I should see a validation error

  Scenario: Check booking status with valid token
    Given I have created a booking with token
    When I navigate to the check booking page
    And I enter my booking token
    And I click check booking
    Then I should see my booking details
    And the status should be "RECEIVED"

  Scenario: Staff updates booking status
    Given there is a booking with status "RECEIVED"
    When I navigate to the staff portal
    And I find the booking in the list
    And I click assign button
    Then the booking status should change to "ASSIGNED"

  Scenario: Cancel an active booking
    Given I have an active booking
    When I navigate to check booking page
    And I enter my booking token
    And I click check booking
    And I click cancel booking
    And I confirm the cancellation
    Then the booking status should be "CANCELED"

  Scenario: Browse bookings by municipality in staff portal
    Given there are bookings for "Aveiro" and "Porto"
    When I navigate to the staff portal
    And I filter by municipality "Aveiro"
    Then I should only see bookings from "Aveiro"