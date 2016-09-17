Feature: Calculator

  Scenario: Add two numbers
    Given a Calculator
    When I enter 5
    And I press PLUS
    And I enter 10
    Then I should see 15

  Scenario: Repeating an operation
    Given a Calculator
    When I enter 5
    And I press MULTIPLY
    And I enter 2
    Then I should see 10
    And I should see 20, 40 and 80 if I repeat the operation three times
  