package com.sporekart.ai.infrastructure.persistence;

import com.sporekart.ai.infrastructure.persistence.entity.PurchaseOrderEntity;
import com.sporekart.ai.infrastructure.persistence.entity.SupplierEntity;
import com.sporekart.ai.infrastructure.persistence.entity.WarehouseEntity;
import com.sporekart.ai.infrastructure.persistence.repository.PurchaseOrderRepository;
import com.sporekart.ai.infrastructure.persistence.repository.SupplierRepository;
import com.sporekart.ai.infrastructure.persistence.repository.WarehouseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class Sprint16PersistenceTest {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Test
    void shouldPersistWarehouseSupplierAndPurchaseOrder() {
        WarehouseEntity warehouse = new WarehouseEntity(UUID.randomUUID(), "WH-001", "North Hub",
                new BigDecimal("1000"), "DISTRIBUTION");
        warehouseRepository.saveAndFlush(warehouse);

        SupplierEntity supplier = new SupplierEntity(UUID.randomUUID(), "SUP-001", "Seed Co", "27ABCDE1234F1Z5",
                "ops@seedco.com");
        supplierRepository.saveAndFlush(supplier);

        PurchaseOrderEntity order = new PurchaseOrderEntity(UUID.randomUUID(), "PO-001", supplier.getId(),
                LocalDate.now(), new BigDecimal("100"), new BigDecimal("18"));
        purchaseOrderRepository.saveAndFlush(order);

        assertThat(warehouseRepository.findById(warehouse.getId())).isPresent();
        assertThat(supplierRepository.findById(supplier.getId())).isPresent();
        assertThat(purchaseOrderRepository.findById(order.getId())).isPresent();
    }
}
