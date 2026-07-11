package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.Vendor;
import com.sporekart.ai.domain.model.VendorStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VendorServiceTest {

    @Test
    void shouldRegisterVendorAndSetPendingStatus() {
        VendorService service = new VendorService();

        Vendor vendor = service.registerVendor("Vendor One", "GST123", "BANK-001");

        assertThat(vendor.getName()).isEqualTo("Vendor One");
        assertThat(vendor.getStatus()).isEqualTo(VendorStatus.PENDING);
    }
}
