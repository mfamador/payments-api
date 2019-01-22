Feature: Payments

  Scenario: Creation of payment resources
    Given a file with payment resources in json
    When use them to create payment resources
    Then the resources are created