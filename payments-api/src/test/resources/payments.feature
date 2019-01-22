Feature: Payments

  Scenario: Creation of payment resources
    Given a file with payment resources in json
    When use them to create payment resources
    Then the resources are created

  Scenario: Update payment resources
    Given a list of resources
    When the version is set to 2
    Then the resources are are updated with version 2


