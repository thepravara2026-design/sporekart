package com.sporekart.ai.infrastructure.integration.erp;

import java.util.Map;

public interface ERPAdapter {
    void connect(Map<String, String> config) throws Exception;

    Map<String, Object> syncInventory(Map<String, Object> data) throws Exception;

    Map<String, Object> syncAccounts(Map<String, Object> data) throws Exception;

    Map<String, Object> syncSuppliers(Map<String, Object> data) throws Exception;

    Map<String, Object> syncPurchases(Map<String, Object> data) throws Exception;

    Map<String, Object> syncSales(Map<String, Object> data) throws Exception;

    Map<String, Object> getSyncStatus() throws Exception;
}
