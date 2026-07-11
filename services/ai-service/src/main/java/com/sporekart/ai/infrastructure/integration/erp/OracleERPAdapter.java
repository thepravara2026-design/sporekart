package com.sporekart.ai.infrastructure.integration.erp;

import java.util.HashMap;
import java.util.Map;

public class OracleERPAdapter implements ERPAdapter {
    private Map<String, String> config;
    private boolean connected = false;

    @Override
    public void connect(Map<String, String> config) throws Exception {
        this.config = config;
        if (config.get("endpoint") == null || config.get("apiKey") == null) {
            throw new Exception("Missing Oracle ERP configuration");
        }
        this.connected = true;
    }

    @Override
    public Map<String, Object> syncInventory(Map<String, Object> data) throws Exception {
        if (!connected)
            throw new Exception("Not connected to Oracle ERP");
        return Map.of("provider", "OracleERP", "syncType", "INVENTORY", "status", "SUCCESS");
    }

    @Override
    public Map<String, Object> syncAccounts(Map<String, Object> data) throws Exception {
        if (!connected)
            throw new Exception("Not connected to Oracle ERP");
        return Map.of("provider", "OracleERP", "syncType", "ACCOUNTS", "status", "SUCCESS");
    }

    @Override
    public Map<String, Object> syncSuppliers(Map<String, Object> data) throws Exception {
        if (!connected)
            throw new Exception("Not connected to Oracle ERP");
        return Map.of("provider", "OracleERP", "syncType", "SUPPLIERS", "status", "SUCCESS");
    }

    @Override
    public Map<String, Object> syncPurchases(Map<String, Object> data) throws Exception {
        if (!connected)
            throw new Exception("Not connected to Oracle ERP");
        return Map.of("provider", "OracleERP", "syncType", "PURCHASES", "status", "SUCCESS");
    }

    @Override
    public Map<String, Object> syncSales(Map<String, Object> data) throws Exception {
        if (!connected)
            throw new Exception("Not connected to Oracle ERP");
        return Map.of("provider", "OracleERP", "syncType", "SALES", "status", "SUCCESS");
    }

    @Override
    public Map<String, Object> getSyncStatus() throws Exception {
        if (!connected)
            throw new Exception("Not connected to Oracle ERP");
        return Map.of("provider", "OracleERP", "connected", true, "lastSync", System.currentTimeMillis());
    }
}
