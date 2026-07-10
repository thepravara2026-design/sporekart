package com.sporekart.fulfillment.domain.repository;

import com.sporekart.fulfillment.domain.model.Shipment;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepositoryPort {
    Shipment save(Shipment shipment);

    Optional<Shipment> findById(String id);

    List<Shipment> findByCustomerId(String customerId);

    List<Shipment> findAll();
}
