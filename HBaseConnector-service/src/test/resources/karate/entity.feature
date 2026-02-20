# Generated with Water Generator
# The goal of this feature test is keeping a lightweight REST smoke-check.
Feature: Check HBaseConnector module status endpoint

  Scenario: HBaseConnector module status endpoint is reachable

    Given header Content-Type = 'application/json'
    And header Accept = 'application/json'
    Given url serviceBaseUrl + '/water/hbase/module/status'
    When method GET
    Then status 200
    And match response contains 'HBaseConnector Module works!'
