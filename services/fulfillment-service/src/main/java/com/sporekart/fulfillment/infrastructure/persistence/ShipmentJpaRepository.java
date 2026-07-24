package com.sporekart.fulfillment.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentJpaRepository extends JpaRepository<ShipmentEntity, String> {
    List<ShipmentEntity> findByCustomerId(String customerId);
}
