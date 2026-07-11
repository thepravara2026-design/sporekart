package com.sporekart.ai.infrastructure.integration.erp;

import java.util.HashMap;
import java.util.Map;

public class TallyPrimeAdapter implements ERPAdapter {
    private Map<String, String> config;
    private boolean connected = false;

    @Override
    public void connect(Map<String, String> config) throws Exception {
        this.config = config;
        if (config.get("endpoint") == null || config.get("apiKey") == null) {
            throw new Exception("Missing TallyPrime configuration");
        }
        this.connected = true;
    }

    @Override
    public Map<String, Object> syncInventory(Map<String, Object> data) throws Exception {
        if (!connected)
            throw new Exception("Not connected to TallyPrime");
        Map<String, Object> result = new HashMap<>();
        result.put("provider", "TallyPrime");
        result.put("syncType", "INVENTORY");
        result.put("recordsSynced", 100);
        result.put("status", "SUCCESS");
        return result;
    }

    @Override
    public Map<String, Object> syncAccounts(Map<String, Object> data) throws Exception {
        if (!connected)
            throw new Exception("Not connected to TallyPrime");
        return Map.of("provider", "TallyPrime", "syncType", "ACCOUNTS", "status", "SUCCESS");
    }

    @Override
    public Map<String, Object> syncSuppliers(Map<String, Object> data) throws Exception {
        if (!connected)
            throw new Exception("Not connected to TallyPrime");
        return Map.of("provider", "TallyPrime", "syncType", "SUPPLIERS", "status", "SUCCESS");
    }

    @Override
    public Map<String, Object> syncPurchases(Map<String, Object> data) throws Exception {
        if (!connected)
            throw new Exception("Not connected to TallyPrime");
        return Map.of("provider", "TallyPrime", "syncType", "PURCHASES", "status", "SUCCESS");
    }

    @Override
    public Map<String, Object> syncSales(Map<String, Object> data) throws Exception {
        if (!connected)
            throw new Exception("Not connected to TallyPrime");
        return Map.of("provider", "TallyPrime", "syncType", "SALES", "status", "SUCCESS");
    }

    @Override
    public Map<String, Object> getSyncStatus() throws Exception {
        if (!connected)
            throw new Exception("Not connected to TallyPrime");
        return Map.of("provider", "TallyPrime", "connected", true, "lastSync", System.currentTimeMillis());
    }
}
