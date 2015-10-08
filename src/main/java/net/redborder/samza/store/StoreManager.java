package net.redborder.samza.store;

import org.apache.samza.config.Config;
import org.apache.samza.storage.kv.KeyValueStore;
import org.apache.samza.task.TaskContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static net.redborder.samza.util.constants.Dimension.*;

public class StoreManager {

    private static Map<String, Store> stores = new LinkedHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(StoreManager.class);

    public StoreManager(Config config, TaskContext context) {
        List<String> storesList = config.getList("redborder.stores", Collections.<String>emptyList());

        log.info("Making stores: ");
        for (String store : storesList) {
            Store storeData = new Store();
            storeData.setKey(config.get("redborder.store." + store + ".key", CLIENT_MAC));
            storeData.setOverwrite(config.getBoolean("redborder.store." + store + ".overwrite", true));
            storeData.setStore((KeyValueStore<String, Map<String, Object>>) context.getStore(store));
            log.info("  * Store: {} {}", store, storeData.toString());
            stores.put(store, storeData);
        }
    }

    public KeyValueStore<String, Map<String, Object>> getStore(String store) {
        Store storeData = stores.get(store);
        KeyValueStore<String, Map<String, Object>> keyValueStore = null;

        if (storeData != null) {
            keyValueStore = storeData.getStore();
        }

        return keyValueStore;
    }

    public boolean hasOverwriteEnabled(String store) {
        Store storeData = stores.get(store);
        boolean overwrite = true;

        if (storeData != null) {
            overwrite = storeData.mustOverwrite();
        }

        return overwrite;
    }

    public Map<String, Object> enrich(Map<String, Object> message) {
        Map<String, Object> enrichment = new HashMap<>();
        enrichment.putAll(message);

        for (Map.Entry<String, Store> store : stores.entrySet()) {
            Store storeData = store.getValue();

            String key = (String) enrichment.get(storeData.getKey());
            String namespace_id = enrichment.get(NAMESPACE_UUID) == null ? "" : String.valueOf(enrichment.get(NAMESPACE_UUID));
            KeyValueStore<String, Map<String, Object>> keyValueStore = storeData.getStore();

            Map<String, Object> contents = keyValueStore.get(key + namespace_id);


            log.debug("STORE: {} CONTENTS: {}", store.getKey(), contents);
            if (contents != null) {
                if (storeData.mustOverwrite()) {
                    enrichment.putAll(contents);
                } else {
                    Map<String, Object> newData = new HashMap<>();
                    newData.putAll(contents);
                    newData.putAll(enrichment);
                    enrichment = newData;
                }
            }
        }

        return enrichment;
    }

    private class Store {
        private String key;
        private boolean overwrite;
        private KeyValueStore<String, Map<String, Object>> store;

        public void setStore(KeyValueStore<String, Map<String, Object>> store) {
            this.store = store;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setOverwrite(boolean overwrite) {
            this.overwrite = overwrite;
        }

        public KeyValueStore<String, Map<String, Object>> getStore() {
            return store;
        }

        public String getKey() {
            return key;
        }

        public boolean mustOverwrite() {
            return overwrite;
        }

        @Override
        public String toString() {
            return new StringBuffer()
                    .append("KEY: ").append(key).append(" ")
                    .append("OVERWRITE: ").append(overwrite).toString();
        }
    }
}
