package it.water.connectors.hbase;

import it.water.connectors.hbase.api.HBaseConnectorApi;
import it.water.connectors.hbase.model.HBaseConnector;
import it.water.connectors.hbase.service.rest.HBaseConnectorRestControllerImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HBaseConnectorRestControllerImplUnitTest {

    @Mock
    private HBaseConnectorApi hbaseConnectorApi;

    @InjectMocks
    private HBaseConnectorRestControllerImpl controller;

    @Test
    void checkConnectionReturnsNotFoundWhenConnectorMissing() throws IOException {
        when(hbaseConnectorApi.find(10L)).thenReturn(null);

        String result = controller.checkConnection(10L);

        assertEquals("Connector not found", result);
    }

    @Test
    void checkConnectionReturnsOkWhenServiceWorks() throws IOException {
        when(hbaseConnectorApi.find(11L)).thenReturn(sampleConnector());

        String result = controller.checkConnection(11L);

        verify(hbaseConnectorApi).checkConnection();
        assertEquals("HBase connection OK", result);
    }

    @Test
    void checkConnectionReturnsErrorMessageWhenServiceFails() throws IOException {
        when(hbaseConnectorApi.find(12L)).thenReturn(sampleConnector());
        doThrow(new IOException("connection down")).when(hbaseConnectorApi).checkConnection();

        String result = controller.checkConnection(12L);

        assertEquals("HBase connection failed: connection down", result);
    }

    @Test
    void createTableAndMutationsRequireConnectorAndDelegate() throws IOException {
        when(hbaseConnectorApi.find(13L)).thenReturn(sampleConnector());

        controller.createTable(13L, "tbl", "cf1,cf2");
        controller.insertData(13L, "tbl", "row1", "cf1", "col1", "v1");
        controller.deleteData(13L, "tbl", "row1");
        controller.enableTable(13L, "tbl");
        controller.disableTable(13L, "tbl");
        controller.dropTable(13L, "tbl");

        verify(hbaseConnectorApi).createTable("tbl", java.util.Arrays.asList("cf1", "cf2"));
        verify(hbaseConnectorApi).insertData("tbl", "row1", "cf1", "col1", "v1");
        verify(hbaseConnectorApi).deleteData("tbl", "row1");
        verify(hbaseConnectorApi).enableTable("tbl");
        verify(hbaseConnectorApi).disableTable("tbl");
        verify(hbaseConnectorApi).dropTable("tbl");
    }

    @Test
    void operationsThrowWhenConnectorMissing() {
        when(hbaseConnectorApi.find(14L)).thenReturn(null);

        assertThrows(IOException.class, () -> controller.createTable(14L, "tbl", "cf1"));
        assertThrows(IOException.class, () -> controller.insertData(14L, "tbl", "row1", "cf1", "col1", "v1"));
        assertThrows(IOException.class, () -> controller.deleteData(14L, "tbl", "row1"));
        assertThrows(IOException.class, () -> controller.enableTable(14L, "tbl"));
        assertThrows(IOException.class, () -> controller.disableTable(14L, "tbl"));
        assertThrows(IOException.class, () -> controller.dropTable(14L, "tbl"));
    }

    @Test
    void getEntityServiceReturnsInjectedApi() throws Exception {
        Method method = HBaseConnectorRestControllerImpl.class.getDeclaredMethod("getEntityService");
        method.setAccessible(true);

        Object result = method.invoke(controller);

        assertSame(hbaseConnectorApi, result);
    }

    @Test
    void requireConnectorAllowsExecutionWhenConnectorExists() throws IOException {
        when(hbaseConnectorApi.find(15L)).thenReturn(sampleConnector());
        doNothing().when(hbaseConnectorApi).enableTable("tbl");

        controller.enableTable(15L, "tbl");

        verify(hbaseConnectorApi).enableTable("tbl");
    }

    private HBaseConnector sampleConnector() {
        return new HBaseConnector(
            "hbase-1",
            "localhost",
            16000,
            "localhost",
            "hdfs://localhost:8020/hbase",
            1L
        );
    }
}
