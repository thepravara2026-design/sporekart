package com.sporekart.fulfillment.infrastructure.persistence;

import com.sporekart.fulfillment.domain.model.Shipment;
import com.sporekart.fulfillment.domain.repository.ShipmentRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryShipmentRepository implements ShipmentRepositoryPort {
    private final Map<String, Shipment> shipmentsById = new ConcurrentHashMap<>();

    @Override
    public Shipment save(Shipment shipment) {
        shipmentsById.put(shipment.getId(), shipment);
        return shipment;
    }

    @Override
    public Optional<Shipment> findById(String id) {
        return Optional.ofNullable(shipmentsById.get(id));
    }

    @Override
    public List<Shipment> findByCustomerId(String customerId) {
        return shipmentsById.values().stream().filter(shipment -> customerId.equals(shipment.getCustomerId())).toList();
    }

    @Override
    public List<Shipment> findAll() {
        return new ArrayList<>(shipmentsById.values());
    }
}
