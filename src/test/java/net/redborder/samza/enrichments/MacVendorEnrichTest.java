package net.redborder.samza.enrichments;

import junit.framework.TestCase;
import org.apache.samza.config.Config;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.redborder.samza.util.constants.Dimension.CLIENT_MAC;
import static net.redborder.samza.util.constants.Dimension.CLIENT_MAC_VENDOR;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MacVendorEnrichTest extends TestCase {
    @Mock
    static Config config;

    @BeforeClass
    public static void initTest() throws IOException {
        config = mock(Config.class);
    }
    @Test
    public void enrichesWithMacVendor() {
        // Enriches when the MAC is found
        MacVendorEnrich.ouiFilePath = ClassLoader.getSystemResource("mac_vendors").getFile();
        MacVendorEnrich macVendorEnrich = new MacVendorEnrich();
        macVendorEnrich.init(config);

        Map<String, Object> messageApple = new HashMap<>();
        messageApple.put(CLIENT_MAC, "00:1C:B3:09:85:15");

        Map<String, Object> enriched = macVendorEnrich.enrich(messageApple);
        assertEquals("Apple", enriched.get(CLIENT_MAC_VENDOR));

        // It doesn't define CLIENT_MAC_VENDOR field when the MAC is not found
        Map<String, Object> messageWithoutVendor = new HashMap<>();
        messageWithoutVendor.put(CLIENT_MAC, "AA:AA:AA:AA:AA:AA");

        Map<String, Object> enrichedWithoutVendor = macVendorEnrich.enrich(messageWithoutVendor);
        assertNull(enrichedWithoutVendor.get(CLIENT_MAC_VENDOR));
    }

    @Test
    public void logsWhenVendorFileNotFound() {
        MacVendorEnrich.ouiFilePath = "/this_path_doesnt_exist";
        MacVendorEnrich macVendorEnrich = new MacVendorEnrich();
        macVendorEnrich.init(config);
        assertTrue(macVendorEnrich.ouiMap.isEmpty());
    }
}

