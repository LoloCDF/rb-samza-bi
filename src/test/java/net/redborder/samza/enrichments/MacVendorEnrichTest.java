/*
 * Copyright (c) 2015 ENEO Tecnologia S.L.
 * This file is part of redBorder.
 * redBorder is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * redBorder is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with redBorder. If not, see <http://www.gnu.org/licenses/>.
 */

package net.redborder.samza.enrichments;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static net.redborder.samza.util.constants.Dimension.CLIENT_MAC;
import static net.redborder.samza.util.constants.Dimension.CLIENT_MAC_VENDOR;

@RunWith(MockitoJUnitRunner.class)
public class MacVendorEnrichTest extends TestCase {
    @Test
    public void enrichesWithMacVendor() {
        MacVendorEnrich.ouiFilePath = ClassLoader.getSystemResource("mac_vendors").getFile();
        MacVendorEnrich macVendorEnrich = new MacVendorEnrich();

        Map<String, Object> messageApple = new HashMap<>();
        messageApple.put(CLIENT_MAC, "00:1C:B3:09:85:15");

        Map<String, Object> enriched = macVendorEnrich.enrich(messageApple);
        assertEquals("Apple", enriched.get(CLIENT_MAC_VENDOR));

        Map<String, Object> messageWithoutVendor = new HashMap<>();
        messageWithoutVendor.put(CLIENT_MAC, "AA:AA:AA:AA:AA:AA");

        Map<String, Object> enrichedWithoutVendor = macVendorEnrich.enrich(messageWithoutVendor);
        assertNull(enrichedWithoutVendor.get(CLIENT_MAC_VENDOR));
    }

    @Test
    public void logsWhenVendorFileNotFound() {
        MacVendorEnrich.ouiFilePath = "/this_path_doesnt_exist";
        MacVendorEnrich macVendorEnrich = new MacVendorEnrich();
    }
}

