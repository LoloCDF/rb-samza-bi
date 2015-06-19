package net.redborder.samza.processors;

import junit.framework.TestCase;
import net.redborder.samza.enrichments.EnrichManager;
import net.redborder.samza.store.StoreManager;
import net.redborder.samza.util.MockKeyValueStore;
import net.redborder.samza.util.MockMessageCollector;
import net.redborder.samza.util.MockTaskContext;
import org.apache.samza.config.Config;
import org.apache.samza.task.TaskContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static net.redborder.samza.util.constants.Dimension.*;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LocationLogicProcessorTest extends TestCase {
    static MockKeyValueStore storeLogic;

    static LocationLogicProcessor locationLogicProcessor;
    static EnrichManager enrichManager;

    @Mock
    static StoreManager storeManager;

    @Mock
    static Config config;

    static TaskContext taskContext;

    @BeforeClass
    public static void initTest() {
        // This store uses an in-memory map instead of samza K/V RockDB
        storeLogic = new MockKeyValueStore();
        taskContext = new MockTaskContext();
        config = mock(Config.class);

        // Mock the storeManager in order to return the mock store
        // that we just instantiated
        storeManager = mock(StoreManager.class);
        when(storeManager.getStore(LocationLogicProcessor.LOCATION_STORE_LOGIC)).thenReturn(storeLogic);
        when(storeManager.enrich(anyMap())).thenAnswer(new Answer<Map<String, Object>>() {
            @Override
            public Map<String, Object> answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (Map<String, Object>) args[0];
            }
        });

        enrichManager = new EnrichManager();
        locationLogicProcessor = new LocationLogicProcessor(storeManager, enrichManager, config, taskContext);
    }


    @Test
    public void getNameTest() {
        assertEquals("location-logic", locationLogicProcessor.getName());
    }

    @Test
    public void processEmptyMsg() {
        MockMessageCollector collector = new MockMessageCollector();
        Map<String, Object> message = new HashMap<>();
        message.put(CLIENT_MAC, "00:00:00:00:00:00");
        message.put(CAMPUS, "CAMPUS-A");
        message.put(BUILDING, "BUILDING-A");
        message.put(FLOOR, "FLOOR-A");
        message.put(ZONE, "ZONE-A");
        message.put(TYPE, "nmsp-measure");
        message.put(WIRELESS_STATION, "33:33:33:33:33:33");
        message.put(TIMESTAMP, 1388609700L);


        locationLogicProcessor.process(message, collector);
        Map<String, Object> expected = new HashMap<>();
        expected.put(TIMESTAMP, 1388609700L);
        expected.put(CAMPUS_NEW, "CAMPUS-A");
        expected.put(CLIENT_MAC, "00:00:00:00:00:00");
        expected.put(BUILDING_NEW, "BUILDING-A");
        expected.put(FLOOR_NEW, "FLOOR-A");
        expected.put(ZONE_NEW, "ZONE-A");
        expected.put(WIRELESS_STATION_NEW, "33:33:33:33:33:33");
        expected.put(TYPE, "nmsp-measure");

        assertEquals(expected, collector.getResult().get(0));

        Map<String, Object> message1 = new HashMap<>();
        message1.put(CLIENT_MAC, "00:00:00:00:00:00");
        message1.put(TIMESTAMP, 1388609710L);
        message1.put(CAMPUS, "CAMPUS-B");
        message1.put(BUILDING, "BUILDING-B");
        message1.put(TYPE, "nmsp-measure");
        message1.put(ZONE, "ZONE-B");
        message1.put(FLOOR, "FLOOR-B");
        message1.put(WIRELESS_STATION, "33:33:33:33:33:31");
        locationLogicProcessor.process(message1, collector);

        Map<String, Object> expected1 = new HashMap<>();
        expected1.put(TIMESTAMP, 1388609710L);
        expected1.put(CAMPUS_OLD, "CAMPUS-A");
        expected1.put(CLIENT_MAC, "00:00:00:00:00:00");
        expected1.put(BUILDING_OLD, "BUILDING-A");
        expected1.put(FLOOR_OLD, "FLOOR-A");
        expected1.put(ZONE_OLD, "ZONE-A");
        expected1.put(WIRELESS_STATION_OLD, "33:33:33:33:33:33");
        expected1.put(CAMPUS_NEW, "CAMPUS-B");
        expected1.put(BUILDING_NEW, "BUILDING-B");
        expected1.put(ZONE_NEW, "ZONE-B");
        expected1.put(FLOOR_NEW, "FLOOR-B");
        expected1.put(WIRELESS_STATION_NEW, "33:33:33:33:33:31");
        expected1.put(TYPE, "nmsp-measure");

        assertEquals(expected1, collector.getResult().get(0));

        locationLogicProcessor.process(message1, collector);
        assertEquals(message1, collector.getResult().get(0));

    }


}
