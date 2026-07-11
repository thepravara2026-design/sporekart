package com.sporekart.ai.infrastructure.persistence.repository;

import com.sporekart.ai.infrastructure.persistence.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, UUID> {
    boolean existsBySupplierCode(String supplierCode);
}
