package com.sporekart.ai.application.service;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class FeatureFlagService {
    private final AiCacheService aiCacheService;

    public FeatureFlagService(AiCacheService aiCacheService) {
        this.aiCacheService = aiCacheService;
    }

    public Map<String, Boolean> getFlags() {
        String cachedFlags = aiCacheService.getOrPut("feature-flags", this::buildFlagsString);
        if (cachedFlags == null || cachedFlags.isBlank()) {
            return Map.of();
        }

        Map<String, Boolean> flags = new LinkedHashMap<>();
        for (String flag : cachedFlags.split(",")) {
            if (!flag.isBlank()) {
                flags.put(flag.trim(), Boolean.TRUE);
            }
        }
        return flags;
    }

    private String buildFlagsString() {
        return "Marketplace Enabled,Vendor Registration Enabled,Vendor Approval Required,Commission Engine Enabled,Settlement Engine Enabled,Dealer Portal Enabled,Distributor Portal Enabled,Quotation Engine Enabled,Credit System Enabled,B2B Enabled,ERP Enabled,Accounting Enabled,GST Enabled,Warehouse Enabled,Procurement Enabled";
    }
}
