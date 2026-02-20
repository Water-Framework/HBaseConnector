# Generated with Water Generator
# The goal of this feature test is ensuring REST endpoint availability without requiring a live HBase cluster.
Feature: Check HBaseConnector Rest API module status response

  Scenario: HBaseConnector module status endpoint

    Given header Content-Type = 'application/json'
    And header Accept = 'application/json'
    Given url serviceBaseUrl + '/water/hbase/module/status'
    When method GET
    Then status 200
    And match response contains 'HBaseConnector Module works!'
