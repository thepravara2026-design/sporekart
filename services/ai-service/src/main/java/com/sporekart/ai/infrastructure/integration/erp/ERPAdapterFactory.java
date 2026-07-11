package com.sporekart.ai.infrastructure.integration.erp;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class ERPAdapterFactory {
    public ERPAdapter getAdapter(String provider) throws Exception {
        return switch (provider) {
            case "TALLY_PRIME" -> new TallyPrimeAdapter();
            case "ZOHO_BOOKS" -> new ZohoBooksAdapter();
            case "ERPNEXT" -> new ERPNextAdapter();
            case "SAP" -> new SAPAdapter();
            case "ORACLE" -> new OracleERPAdapter();
            default -> throw new Exception("Unknown ERP provider: " + provider);
        };
    }
}
