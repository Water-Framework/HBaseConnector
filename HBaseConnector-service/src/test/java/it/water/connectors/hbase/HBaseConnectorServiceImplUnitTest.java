package it.water.connectors.hbase;

import it.water.connectors.hbase.api.HBaseConnectorSystemApi;
import it.water.connectors.hbase.service.HBaseConnectorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HBaseConnectorServiceImplUnitTest {

    @Mock
    private HBaseConnectorSystemApi systemService;

    @InjectMocks
    private HBaseConnectorServiceImpl service;

    @Test
    void delegatesSimpleOperationsToSystemService() throws IOException {
        List<String> families = Arrays.asList("cf1", "cf2");

        service.checkConnection();
        service.createTable("table1", families);
        service.deleteData("table1", "row1");
        service.disableTable("table1");
        service.dropTable("table1");
        service.enableTable("table1");
        service.insertData("table1", "row1", "cf1", "col1", "value1");

        verify(systemService).checkConnection();
        verify(systemService).createTable("table1", families);
        verify(systemService).deleteData("table1", "row1");
        verify(systemService).disableTable("table1");
        verify(systemService).dropTable("table1");
        verify(systemService).enableTable("table1");
        verify(systemService).insertData("table1", "row1", "cf1", "col1", "value1");
    }

    @Test
    void scanDelegatesAndReturnsResult() throws IOException {
        Map<byte[], List<byte[]>> columns = new HashMap<>();
        byte[] family = "cf1".getBytes();
        byte[] column = "col1".getBytes();
        columns.put(family, Arrays.asList(column));

        Map<byte[], Map<byte[], Map<byte[], byte[]>>> expected = new HashMap<>();
        when(systemService.scan("table1", columns, null, null, 10)).thenReturn(expected);

        Map<byte[], Map<byte[], Map<byte[], byte[]>>> result = service.scan("table1", columns, null, null, 10);

        assertNotNull(result);
        assertSame(expected, result);
        verify(systemService).scan("table1", columns, null, null, 10);
    }

    @Test
    void setterAndGetterForSystemServiceWork() {
        service.setSystemService(systemService);
        assertSame(systemService, service.getSystemService());
    }
}
