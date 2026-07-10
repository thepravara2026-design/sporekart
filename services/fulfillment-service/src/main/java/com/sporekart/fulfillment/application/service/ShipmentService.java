package com.sporekart.fulfillment.application.service;

import com.sporekart.fulfillment.domain.model.Shipment;
import com.sporekart.fulfillment.domain.model.ShipmentItem;
import com.sporekart.fulfillment.domain.repository.ShipmentRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ShipmentService {
    private final ShipmentRepositoryPort repositoryPort;

    public ShipmentService(ShipmentRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public Shipment create(String orderId, String customerId, BigDecimal shippingCharge, List<ShipmentItem> items) {
        Shipment shipment = Shipment.create(orderId, customerId, shippingCharge, items);
        return repositoryPort.save(shipment);
    }

    public Optional<Shipment> getById(String id) {
        return repositoryPort.findById(id);
    }

    public List<Shipment> listByCustomer(String customerId) {
        return repositoryPort.findByCustomerId(customerId);
    }

    public Shipment cancel(String id) {
        Shipment shipment = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));
        return repositoryPort.save(shipment.cancel());
    }

    public Shipment schedulePickup(String id) {
        Shipment shipment = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));
        return repositoryPort.save(shipment.schedulePickup());
    }
}
