package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.CommissionRule;
import com.sporekart.ai.domain.model.Settlement;
import com.sporekart.ai.domain.model.Vendor;
import com.sporekart.ai.domain.model.VendorStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class MarketplaceService {
    private final VendorService vendorService;

    public MarketplaceService(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    public List<Vendor> listMarketplaceVendors() {
        List<Vendor> vendors = vendorService.listVendors();
        if (!vendors.isEmpty()) {
            return vendors;
        }
        return List.of(new Vendor(UUID.randomUUID(), "SporeKart Trusted Vendor", "GST0001", "BANK-0001",
                VendorStatus.APPROVED));
    }

    public List<CommissionRule> listCommissionRules() {
        return List.of(
                new CommissionRule(UUID.randomUUID(), "general", BigDecimal.valueOf(0.10)),
                new CommissionRule(UUID.randomUUID(), "premium", BigDecimal.valueOf(0.15)),
                new CommissionRule(UUID.randomUUID(), "seasonal", BigDecimal.valueOf(0.08)));
    }

    public List<Settlement> listSettlements() {
        return List.of(
                new Settlement(UUID.randomUUID(), "PENDING", BigDecimal.valueOf(2500)),
                new Settlement(UUID.randomUUID(), "COMPLETED", BigDecimal.valueOf(11200)),
                new Settlement(UUID.randomUUID(), "FAILED", BigDecimal.valueOf(0)));
    }
}
