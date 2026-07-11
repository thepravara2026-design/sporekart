package com.sporekart.ai.infrastructure.integration.erp;

import java.util.HashMap;
import java.util.Map;

public class SAPAdapter implements ERPAdapter {
    private Map<String, String> config;
    private boolean connected = false;

    @Override
    public void connect(Map<String, String> config) throws Exception {
        this.config = config;
        if (config.get("endpoint") == null || config.get("apiKey") == null) {
            throw new Exception("Missing SAP configuration");
        }
        this.connected = true;
    }

    @Override
    public Map<String, Object> syncInventory(Map<String, Object> data) throws Exception {
        if (!connected)
            throw new Exception("Not connected to SAP");
        return Map.of("provider", "SAP", "syncType", "INVENTORY", "status", "SUCCESS");
    }

    @Override
    public Map<String, Object> syncAccounts(Map<String, Object> data) throws Exception {
        if (!connected)
            throw new Exception("Not connected to SAP");
        return Map.of("provider", "SAP", "syncType", "ACCOUNTS", "status", "SUCCESS");
    }

    @Override
    public Map<String, Object> syncSuppliers(Map<String, Object> data) throws Exception {
        if (!connected)
            throw new Exception("Not connected to SAP");
        return Map.of("provider", "SAP", "syncType", "SUPPLIERS", "status", "SUCCESS");
    }

    @Override
    public Map<String, Object> syncPurchases(Map<String, Object> data) throws Exception {
        if (!connected)
            throw new Exception("Not connected to SAP");
        return Map.of("provider", "SAP", "syncType", "PURCHASES", "status", "SUCCESS");
    }

    @Override
    public Map<String, Object> syncSales(Map<String, Object> data) throws Exception {
        if (!connected)
            throw new Exception("Not connected to SAP");
        return Map.of("provider", "SAP", "syncType", "SALES", "status", "SUCCESS");
    }

    @Override
    public Map<String, Object> getSyncStatus() throws Exception {
        if (!connected)
            throw new Exception("Not connected to SAP");
        return Map.of("provider", "SAP", "connected", true, "lastSync", System.currentTimeMillis());
    }
}
