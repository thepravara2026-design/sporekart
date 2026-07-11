package com.sporekart.ai.infrastructure.integration.erp;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ERPAdapterFactoryTest {
    private final ERPAdapterFactory factory = new ERPAdapterFactory();

    @Test
    void shouldReturnTallyPrimeAdapter() throws Exception {
        ERPAdapter adapter = factory.getAdapter("TALLY_PRIME");

        assertThat(adapter).isInstanceOf(TallyPrimeAdapter.class);
        adapter.connect(Map.of("endpoint", "https://tally.example.com", "apiKey", "key"));
        assertThat(adapter.syncInventory(Map.of("itemId", "SKU-001", "quantity", 20)))
                .containsEntry("provider", "TallyPrime");
    }

    @Test
    void shouldReturnERPNextAdapter() throws Exception {
        ERPAdapter adapter = factory.getAdapter("ERPNEXT");

        assertThat(adapter).isInstanceOf(ERPNextAdapter.class);
        adapter.connect(Map.of("endpoint", "https://erpnext.example.com", "apiKey", "key"));
        assertThat(adapter.getSyncStatus()).containsEntry("provider", "ERPNext");
        assertThat(adapter.syncSales(Map.of())).containsEntry("syncType", "SALES");
    }

    @Test
    void shouldReturnSAPAdapter() throws Exception {
        ERPAdapter adapter = factory.getAdapter("SAP");

        assertThat(adapter).isInstanceOf(SAPAdapter.class);
        adapter.connect(Map.of("endpoint", "https://sap.example.com", "apiKey", "key"));
        assertThat(adapter.syncAccounts(Map.of())).containsEntry("provider", "SAP");
    }

    @Test
    void shouldReturnOracleAdapter() throws Exception {
        ERPAdapter adapter = factory.getAdapter("ORACLE");

        assertThat(adapter).isInstanceOf(OracleERPAdapter.class);
        adapter.connect(Map.of("endpoint", "https://oracle.example.com", "apiKey", "key"));
        assertThat(adapter.syncPurchases(Map.of())).containsEntry("provider", "OracleERP");
    }

    @Test
    void shouldReturnZohoBooksAdapter() throws Exception {
        ERPAdapter adapter = factory.getAdapter("ZOHO_BOOKS");

        assertThat(adapter).isInstanceOf(ZohoBooksAdapter.class);
        adapter.connect(Map.of("endpoint", "https://zoho.example.com", "apiKey", "key"));
        assertThat(adapter.syncSuppliers(Map.of())).containsEntry("provider", "ZohoBooks");
    }

    @Test
    void shouldThrowOnUnknownProvider() {
        assertThatThrownBy(() -> factory.getAdapter("UNKNOWN"))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Unknown ERP provider");
    }
}
