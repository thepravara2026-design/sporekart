package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.CommissionRule;
import com.sporekart.ai.domain.model.Settlement;
import com.sporekart.ai.domain.model.Vendor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class MarketplaceServiceTest {
    @Test
    void shouldReturnDefaultMarketplaceVendorsWhenNoVendorsExist() {
        VendorService vendorService = Mockito.mock(VendorService.class);
        when(vendorService.listVendors()).thenReturn(List.of());

        MarketplaceService service = new MarketplaceService(vendorService);

        List<Vendor> vendors = service.listMarketplaceVendors();

        assertThat(vendors).isNotEmpty();
        assertThat(vendors.get(0).getStatus()).isEqualTo(com.sporekart.ai.domain.model.VendorStatus.APPROVED);
    }

    @Test
    void shouldReturnVendorServiceMarketplaceVendorsWhenAvailable() {
        Vendor vendor = new Vendor(java.util.UUID.randomUUID(), "Vendor A", "GST111", "BANK111",
                com.sporekart.ai.domain.model.VendorStatus.APPROVED);
        VendorService vendorService = Mockito.mock(VendorService.class);
        when(vendorService.listVendors()).thenReturn(List.of(vendor));

        MarketplaceService service = new MarketplaceService(vendorService);

        List<Vendor> vendors = service.listMarketplaceVendors();

        assertThat(vendors).contains(vendor);
    }

    @Test
    void shouldReturnCommissionRules() {
        VendorService vendorService = Mockito.mock(VendorService.class);
        MarketplaceService service = new MarketplaceService(vendorService);

        List<CommissionRule> rules = service.listCommissionRules();

        assertThat(rules).hasSize(3);
        assertThat(rules).extracting(CommissionRule::getCategory).containsExactly("general", "premium", "seasonal");
    }

    @Test
    void shouldReturnSettlements() {
        VendorService vendorService = Mockito.mock(VendorService.class);
        MarketplaceService service = new MarketplaceService(vendorService);

        List<Settlement> settlements = service.listSettlements();

        assertThat(settlements).hasSize(3);
        assertThat(settlements).extracting(Settlement::getStatus).containsExactly("PENDING", "COMPLETED", "FAILED");
    }
}
