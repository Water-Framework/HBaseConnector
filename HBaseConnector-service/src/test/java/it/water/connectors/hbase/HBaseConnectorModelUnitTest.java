package it.water.connectors.hbase;

import it.water.connectors.hbase.model.HBaseConnector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HBaseConnectorModelUnitTest {

    @Test
    void constructorAndGettersWork() {
        HBaseConnector connector = new HBaseConnector(
            "hbase-connector",
            "master.local",
            16000,
            "zk.local",
            "hdfs://cluster/hbase",
            10L
        );

        assertEquals("hbase-connector", connector.getName());
        assertEquals("master.local", connector.getMasterHostname());
        assertEquals(16000, connector.getMasterPort());
        assertEquals("zk.local", connector.getZookeeperQuorum());
        assertEquals("hdfs://cluster/hbase", connector.getRootdir());
        assertEquals(10L, connector.getOwnerUserId());
        assertFalse(connector.getClusterDistributed());
        assertNotNull(connector.toString());
    }

    @Test
    void settersAndRoleConstantsWork() {
        HBaseConnector connector = new HBaseConnector(
            "name-1",
            "master-1",
            16000,
            "zk-1",
            "hdfs://cluster/hbase",
            1L
        );

        connector.setName("name-2");
        connector.setMasterHostname("master-2");
        connector.setMasterPort(17000);
        connector.setZookeeperQuorum("zk-2");
        connector.setRootdir("hdfs://cluster/new");
        connector.setClusterDistributed(true);
        connector.setDescription("description");
        connector.setOwnerUserId(20L);

        assertEquals("name-2", connector.getName());
        assertEquals("master-2", connector.getMasterHostname());
        assertEquals(17000, connector.getMasterPort());
        assertEquals("zk-2", connector.getZookeeperQuorum());
        assertEquals("hdfs://cluster/new", connector.getRootdir());
        assertTrue(connector.getClusterDistributed());
        assertEquals("description", connector.getDescription());
        assertEquals(20L, connector.getOwnerUserId());

        assertEquals("HBaseConnectorManager", HBaseConnector.DEFAULT_MANAGER_ROLE);
        assertEquals("HBaseConnectorViewer", HBaseConnector.DEFAULT_VIEWER_ROLE);
        assertEquals("HBaseConnectorEditor", HBaseConnector.DEFAULT_EDITOR_ROLE);
    }

    @Test
    void equalsAndHashCodeAreBasedOnName() {
        HBaseConnector connectorOne = new HBaseConnector(
            "same-name",
            "master-1",
            16000,
            "zk-1",
            "hdfs://cluster/one",
            1L
        );
        HBaseConnector connectorTwo = new HBaseConnector(
            "same-name",
            "master-2",
            17000,
            "zk-2",
            "hdfs://cluster/two",
            2L
        );

        assertEquals(connectorOne, connectorTwo);
        assertEquals(connectorOne.hashCode(), connectorTwo.hashCode());
    }
}
