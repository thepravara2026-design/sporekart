package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.Vendor;
import com.sporekart.ai.domain.model.VendorStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class VendorService {
    private final List<Vendor> vendors = new ArrayList<>();

    public Vendor registerVendor(String name, String gstNumber, String bankAccount) {
        Vendor vendor = new Vendor(UUID.randomUUID(), name, gstNumber, bankAccount, VendorStatus.PENDING);
        vendors.add(vendor);
        return vendor;
    }

    public List<Vendor> listVendors() {
        return new ArrayList<>(vendors);
    }

    public Vendor approveVendor(UUID vendorId) {
        return vendors.stream()
                .filter(vendor -> vendor.getId().equals(vendorId))
                .findFirst()
                .map(vendor -> new Vendor(vendor.getId(), vendor.getName(), vendor.getGstNumber(),
                        vendor.getBankAccount(), VendorStatus.APPROVED))
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));
    }
}

