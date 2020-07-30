Feature: Title of your feature

  Scenario: Title of your scenario
    Given I will connect to "qa" environment
    And I will use the below data
      | name     | manzer   |
      | deptname | computer |
    When I will make "post" call to "emp/create"
    Then will get below output
      | statusCode | 201 |
