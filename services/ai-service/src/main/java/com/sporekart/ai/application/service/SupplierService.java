package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.Supplier;
import com.sporekart.ai.common.exception.SupplierException;
import com.sporekart.ai.infrastructure.persistence.entity.SupplierEntity;
import com.sporekart.ai.infrastructure.persistence.repository.SupplierRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SupplierService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final SupplierRepository supplierRepository;
    private final AuditService auditService;

    public SupplierService(KafkaTemplate<String, String> kafkaTemplate, SupplierRepository supplierRepository,
            AuditService auditService) {
        this.kafkaTemplate = kafkaTemplate;
        this.supplierRepository = supplierRepository;
        this.auditService = auditService;
    }

    public Supplier registerSupplier(String code, String name, String type, String gstin, String email)
            throws SupplierException {
        if (supplierRepository.existsBySupplierCode(code)) {
            throw new SupplierException("Supplier code already exists");
        }
        if (!isValidGSTIN(gstin)) {
            throw new SupplierException("Invalid GSTIN format");
        }

        Supplier supplier = new Supplier(UUID.randomUUID(), code, name, type, gstin, email);
        SupplierEntity entity = new SupplierEntity(supplier.getId(), code, name, gstin, email, type);
        supplierRepository.save(entity);
        auditService.logAction("supplier", supplier.getId().toString(), "REGISTERED", "Supplier registered: " + code);
        publishEvent("SupplierRegistered", supplier.getId().toString());
        return supplier;
    }

    public void approveSupplier(UUID supplierId, UUID userId) throws SupplierException {
        Optional<SupplierEntity> maybe = supplierRepository.findById(supplierId);
        if (maybe.isEmpty())
            throw new SupplierException("Supplier not found");
        SupplierEntity entity = maybe.get();
        entity = supplierRepository.save(entity);
    }

    public void blacklistSupplier(UUID supplierId) throws SupplierException {
        Optional<SupplierEntity> maybe = supplierRepository.findById(supplierId);
        if (maybe.isEmpty())
            throw new SupplierException("Supplier not found");
        SupplierEntity entity = maybe.get();
        entity.setStatus("BLACKLISTED");
        supplierRepository.save(entity);
        auditService.logAction("supplier", supplierId.toString(), "BLACKLISTED", "Supplier blacklisted");
    }

    public void updateRating(UUID supplierId, BigDecimal rating) throws SupplierException {
        Optional<SupplierEntity> maybe = supplierRepository.findById(supplierId);
        if (maybe.isEmpty())
            throw new SupplierException("Supplier not found");
        if (rating.compareTo(BigDecimal.ZERO) < 0 || rating.compareTo(new BigDecimal(5)) > 0) {
            throw new SupplierException("Rating must be between 0 and 5");
        }
        SupplierEntity entity = maybe.get();
        entity.setRating(rating);
        supplierRepository.save(entity);
    }

    public List<Supplier> getSuppliers(String status) {
        List<Supplier> result = new ArrayList<>();
        for (SupplierEntity e : supplierRepository.findAll()) {
            if (status == null || e.getStatus().equals(status)) {
                result.add(new Supplier(e.getId(), e.getSupplierCode(), e.getSupplierName(), null, e.getGstin(),
                        e.getEmail()));
            }
        }
        return result;
    }

    public Supplier getSupplier(UUID supplierId) throws SupplierException {
        Optional<SupplierEntity> maybe = supplierRepository.findById(supplierId);
        if (maybe.isEmpty())
            throw new SupplierException("Supplier not found");
        SupplierEntity e = maybe.get();
        return new Supplier(e.getId(), e.getSupplierCode(), e.getSupplierName(), null, e.getGstin(), e.getEmail());
    }

    private boolean isValidGSTIN(String gstin) {
        return gstin == null || (gstin.length() == 15 && gstin.matches("[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[0-9]{1}"));
    }

    private void publishEvent(String eventType, String eventData) {
        try {
            kafkaTemplate.send("supplier-events", eventType, eventData);
        } catch (Exception e) {
            System.out.println("Failed to publish supplier event: " + e.getMessage());
        }
    }
}
