package com.sporekart.fulfillment.application.service;

import com.sporekart.fulfillment.domain.model.Shipment;
import com.sporekart.fulfillment.domain.model.ShipmentItem;
import com.sporekart.fulfillment.domain.model.ShipmentStatus;
import com.sporekart.fulfillment.infrastructure.persistence.InMemoryShipmentRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ShipmentServiceTest {
    @Test
    void createAndCancelShipment() {
        ShipmentService service = new ShipmentService(new InMemoryShipmentRepository());
        Shipment shipment = service.create("order-1", "customer-1", new BigDecimal("49.99"),
                List.of(new ShipmentItem("SKU-1", 1)));

        assertNotNull(shipment);
        assertEquals(ShipmentStatus.CREATED, shipment.getStatus());

        Shipment cancelled = service.cancel(shipment.getId());
        assertEquals(ShipmentStatus.CANCELLED, cancelled.getStatus());
    }
}
