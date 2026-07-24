package com.sporekart.fulfillment.infrastructure.persistence;

import com.sporekart.fulfillment.domain.model.Shipment;
import com.sporekart.fulfillment.domain.repository.ShipmentRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class JpaShipmentRepositoryAdapter implements ShipmentRepositoryPort {

    private final ShipmentJpaRepository jpaRepository;

    public JpaShipmentRepositoryAdapter(ShipmentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Shipment save(Shipment shipment) {
        return jpaRepository.save(ShipmentEntity.fromDomain(shipment)).toDomain();
    }

    @Override
    public Optional<Shipment> findById(String id) {
        return jpaRepository.findById(id).map(ShipmentEntity::toDomain);
    }

    @Override
    public List<Shipment> findByCustomerId(String customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
                .map(ShipmentEntity::toDomain).toList();
    }

    @Override
    public List<Shipment> findAll() {
        return jpaRepository.findAll().stream().map(ShipmentEntity::toDomain).toList();
    }
}
