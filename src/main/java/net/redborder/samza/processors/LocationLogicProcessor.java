package net.redborder.samza.processors;

import net.redborder.samza.enrichments.EnrichManager;
import net.redborder.samza.store.StoreManager;
import net.redborder.samza.util.constants.Constants;
import net.redborder.samza.util.constants.Dimension;
import org.apache.samza.config.Config;
import org.apache.samza.storage.kv.KeyValueStore;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemStream;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.TaskContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.redborder.samza.util.constants.Dimension.*;

public class LocationLogicProcessor extends Processor<Map<String, Object>> {
    private static final SystemStream OUTPUT_STREAM = new SystemStream("kafka", Constants.ENRICHMENT_LOC_OUTPUT_TOPIC);
    private KeyValueStore<String, Map<String, Object>> storeLogic;
    public final static String LOCATION_STORE_LOGIC = "location-logic";
    private static final String DATASOURCE = "rb_loc_post";
    private KeyValueStore<String, Long> countersStore;
    private KeyValueStore<String, Long> flowsNumber;

    private final List<String> dimToDruid = Arrays.asList(MARKET, MARKET_UUID, ORGANIZATION, ORGANIZATION_UUID,
            DEPLOYMENT, DEPLOYMENT_UUID, SENSOR_NAME, SENSOR_UUID, NAMESPACE, TYPE, TIMESTAMP);

    public LocationLogicProcessor(StoreManager storeManager, EnrichManager enrichManager, Config config, TaskContext context) {
        super(storeManager, enrichManager, config, context);
        this.storeManager = storeManager;
        this.enrichManager = enrichManager;
        storeLogic = storeManager.getStore(LOCATION_STORE_LOGIC);
        countersStore = (KeyValueStore<String, Long>) context.getStore("counter");
        flowsNumber = (KeyValueStore<String, Long>) context.getStore("flows-number");
    }

    @Override
    public void process(String stream, Map<String, Object> message, MessageCollector collector) {
        String type = (String) message.get(TYPE);
        if (type != null && (type.equals("mse10") || type.equals("mse") || type.equals("nmsp-info") || type.equals("nmsp-measure") || type.equals("radius"))) {
            Map<String, Object> toDruid = new HashMap<>();
            Map<String, Object> toCache = new HashMap<>();

            String client_mac = (String) message.get(CLIENT_MAC);
            String newFloor = (String) message.get(FLOOR_UUID);
            String newBuilding = (String) message.get(BUILDING_UUID);
            String newCampus = (String) message.get(CAMPUS_UUID);
            String newZone = (String) message.get(ZONE_UUID);
            String wirelessStation = (String) message.get(WIRELESS_STATION);
            Object namespace_id = message.get(NAMESPACE_UUID) == null ? "" : message.get(NAMESPACE_UUID);

            Map<String, Object> locationCache = storeLogic.get(client_mac + namespace_id.toString());

            if (newFloor == null)
                newFloor = "outside";

            if (newBuilding == null)
                newBuilding = "outside";

            if (newCampus == null)
                newCampus = "outside";

            if (newZone == null)
                newZone = "outside";

            if (wirelessStation == null)
                wirelessStation = "outside";

            if (locationCache != null) {
                String oldFloor = (String) locationCache.get(FLOOR_UUID);
                String oldBuilding = (String) locationCache.get(BUILDING_UUID);
                String oldCampus = (String) locationCache.get(CAMPUS_UUID);
                String oldwirelessStation = (String) locationCache.get(WIRELESS_STATION);
                String oldZone = (String) locationCache.get(ZONE_UUID);

                if (oldFloor != null) {
                    if (!oldFloor.equals(newFloor)) {
                        toDruid.put(FLOOR_OLD, oldFloor);
                    } else {
                        toDruid.put(FLOOR_OLD, newFloor);
                    }

                    toDruid.put(FLOOR_NEW, newFloor);
                }

                if (oldZone != null) {
                    if (!oldZone.equals(newZone)) {
                        toDruid.put(ZONE_OLD, oldZone);
                    } else {
                        toDruid.put(ZONE_OLD, newZone);
                    }
                    toDruid.put(ZONE_NEW, newZone);
                }

                if (oldwirelessStation != null) {
                    if (!oldwirelessStation.equals(wirelessStation)) {
                        toDruid.put(WIRELESS_STATION_OLD, oldwirelessStation);
                    } else {
                        toDruid.put(WIRELESS_STATION_OLD, wirelessStation);
                    }
                    toDruid.put(WIRELESS_STATION_NEW, wirelessStation);
                }

                if (oldBuilding != null) {
                    if (!oldBuilding.equals(newBuilding)) {
                        toDruid.put(BUILDING_OLD, oldBuilding);
                    } else {
                        toDruid.put(BUILDING_OLD, newBuilding);
                    }
                    toDruid.put(BUILDING_NEW, newBuilding);
                }

                if (oldCampus != null) {
                    if (!oldCampus.equals(newCampus)) {
                        toDruid.put(CAMPUS_OLD, oldCampus);
                    } else {
                        toDruid.put(CAMPUS_OLD, newCampus);
                    }
                    toDruid.put(CAMPUS_NEW, newCampus);
                }

            } else {
                toDruid.put(FLOOR_NEW, newFloor);
                toDruid.put(CAMPUS_NEW, newCampus);
                toDruid.put(BUILDING_NEW, newBuilding);
                toDruid.put(ZONE_NEW, newZone);
                toDruid.put(WIRELESS_STATION_NEW, wirelessStation);
                toDruid.put(FLOOR_OLD, "outside");
                toDruid.put(CAMPUS_OLD, "outside");
                toDruid.put(BUILDING_OLD, "outside");
                toDruid.put(ZONE_OLD, "outside");
                toDruid.put(WIRELESS_STATION_OLD, "outside");
            }


            toDruid.put(CLIENT_MAC, client_mac);

            if (!namespace_id.equals("")) {
                toDruid.put(NAMESPACE_UUID, namespace_id);
            }


            for (String dimension : dimToDruid) {
                Object value = message.get(dimension);
                if (value != null) {
                    toDruid.put(dimension, value);
                }
            }

            toCache.put(FLOOR_UUID, newFloor);
            toCache.put(CAMPUS_UUID, newCampus);
            toCache.put(BUILDING_UUID, newBuilding);
            toCache.put(ZONE_UUID, newZone);
            toCache.put(WIRELESS_STATION, wirelessStation);

            storeLogic.put(client_mac + namespace_id.toString(), toCache);
            Map<String, Object> enrichmentEvent = enrichManager.enrich(toDruid);

            String datasource = DATASOURCE;
            Object namespace = enrichmentEvent.get(Dimension.NAMESPACE_UUID);

            if (namespace != null) {
                datasource = String.format("%s_%s", DATASOURCE, namespace);
            }

            Long counter = countersStore.get(datasource);

            if (counter == null) {
                counter = 0L;
            }

            counter++;
            countersStore.put(datasource, counter);

            Long flows = flowsNumber.get(datasource);

            if (flows != null) {
                enrichmentEvent.put("flows_count", flows);
            }

            collector.send(new OutgoingMessageEnvelope(OUTPUT_STREAM, null, enrichmentEvent));
        }
    }

    @Override
    public String getName() {
        return "location-logic";
    }
}
