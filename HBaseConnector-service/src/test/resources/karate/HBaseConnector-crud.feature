# Generated with Water Generator
# The goal of this feature test is ensuring JSON response format.
Feature: Check HBaseConnector Rest Api Response

  Scenario: HBaseConnector CRUD Operations

    Given header Content-Type = 'application/json'
    And header Accept = 'application/json'
    Given url serviceBaseUrl + '/water/hbaseconnectors'
    And request
    """
    {
      "name": "hbaseConnectorFeature",
      "masterHostname": "localhost",
      "masterPort": 16000,
      "zookeeperQuorum": "localhost",
      "rootdir": "hdfs://localhost:8020/hbase",
      "clusterDistributed": false,
      "description": "feature description"
    }
    """
    When method POST
    Then status 200
    And match response contains
    """
    {
      "id": #number,
      "entityVersion": 1,
      "entityCreateDate": '#number',
      "entityModifyDate": '#number',
      "name": "hbaseConnectorFeature",
      "masterHostname": "localhost",
      "masterPort": 16000,
      "zookeeperQuorum": "localhost",
      "rootdir": "hdfs://localhost:8020/hbase",
      "clusterDistributed": false,
      "description": "feature description"
    }
    """
    * def entityId = response.id

    Given header Content-Type = 'application/json'
    And header Accept = 'application/json'
    Given url serviceBaseUrl + '/water/hbaseconnectors'
    And request
    """
    {
      "id": "#(entityId)",
      "entityVersion": 1,
      "name": "hbaseConnectorFeatureUpdated",
      "masterHostname": "localhost",
      "masterPort": 16000,
      "zookeeperQuorum": "localhost",
      "rootdir": "hdfs://localhost:8020/hbase",
      "clusterDistributed": true,
      "description": "feature description updated"
    }
    """
    When method PUT
    Then status 200
    And match response contains
    """
    {
      "id": #number,
      "entityVersion": 2,
      "entityCreateDate": '#number',
      "entityModifyDate": '#number',
      "name": "hbaseConnectorFeatureUpdated",
      "masterHostname": "localhost",
      "masterPort": 16000,
      "zookeeperQuorum": "localhost",
      "rootdir": "hdfs://localhost:8020/hbase",
      "clusterDistributed": true,
      "description": "feature description updated"
    }
    """

    Given header Content-Type = 'application/json'
    And header Accept = 'application/json'
    Given url serviceBaseUrl + '/water/hbaseconnectors/' + entityId
    When method GET
    Then status 200
    And match response contains
    """
    {
      "id": #number,
      "entityVersion": 2,
      "entityCreateDate": '#number',
      "entityModifyDate": '#number',
      "name": "hbaseConnectorFeatureUpdated",
      "masterHostname": "localhost",
      "masterPort": 16000,
      "zookeeperQuorum": "localhost",
      "rootdir": "hdfs://localhost:8020/hbase",
      "clusterDistributed": true,
      "description": "feature description updated"
    }
    """

    Given header Content-Type = 'application/json'
    And header Accept = 'application/json'
    Given url serviceBaseUrl + '/water/hbaseconnectors'
    When method GET
    Then status 200
    And match response.results contains deep
    """
    {
      "id": #number,
      "entityVersion": 2,
      "entityCreateDate": '#number',
      "entityModifyDate": '#number',
      "name": "hbaseConnectorFeatureUpdated",
      "masterHostname": "localhost",
      "masterPort": 16000,
      "zookeeperQuorum": "localhost",
      "rootdir": "hdfs://localhost:8020/hbase",
      "clusterDistributed": true,
      "description": "feature description updated"
    }
    """

    Given header Content-Type = 'application/json'
    And header Accept = 'application/json'
    Given url serviceBaseUrl + '/water/hbaseconnectors/' + entityId
    When method DELETE
    Then status 204
