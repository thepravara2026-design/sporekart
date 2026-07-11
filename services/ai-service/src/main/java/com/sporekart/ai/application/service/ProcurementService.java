package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.PurchaseOrder;
import com.sporekart.ai.common.exception.ProcurementException;
import com.sporekart.ai.infrastructure.persistence.entity.PurchaseOrderEntity;
import com.sporekart.ai.infrastructure.persistence.repository.PurchaseOrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class ProcurementService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final AuditService auditService;

    public ProcurementService(KafkaTemplate<String, String> kafkaTemplate,
            PurchaseOrderRepository purchaseOrderRepository, AuditService auditService) {
        this.kafkaTemplate = kafkaTemplate;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.auditService = auditService;
    }

    public PurchaseOrder createPurchaseOrder(UUID supplierId, LocalDate poDate, BigDecimal total, BigDecimal tax)
            throws ProcurementException {
        PurchaseOrder po = new PurchaseOrder(UUID.randomUUID(), "PO-" + System.currentTimeMillis(),
                supplierId, poDate, total, tax);
        PurchaseOrderEntity entity = new PurchaseOrderEntity(po.getId(), po.getPoNumber(), supplierId, poDate, total,
                tax);
        purchaseOrderRepository.save(entity);
        auditService.logAction("purchase_order", po.getId().toString(), "CREATED", "Purchase order created");
        return po;
    }

    public void submitPurchaseOrder(UUID poId) throws ProcurementException {
        Optional<PurchaseOrderEntity> maybe = purchaseOrderRepository.findById(poId);
        if (maybe.isEmpty())
            throw new ProcurementException("Purchase order not found");
        PurchaseOrderEntity ent = maybe.get();
        ent.setStatus("SUBMITTED");
        purchaseOrderRepository.save(ent);
    }

    public void approvePurchaseOrder(UUID poId, UUID userId) throws ProcurementException {
        Optional<PurchaseOrderEntity> maybe = purchaseOrderRepository.findById(poId);
        if (maybe.isEmpty())
            throw new ProcurementException("Purchase order not found");
        PurchaseOrderEntity ent = maybe.get();
        ent.setStatus("APPROVED");
        purchaseOrderRepository.save(ent);
        auditService.logAction("purchase_order", ent.getId().toString(), "APPROVED", "Purchase order approved");
        publishEvent("PurchaseOrderCreated", ent.getId().toString());
    }

    public void rejectPurchaseOrder(UUID poId) throws ProcurementException {
        Optional<PurchaseOrderEntity> maybe = purchaseOrderRepository.findById(poId);
        if (maybe.isEmpty())
            throw new ProcurementException("Purchase order not found");
        PurchaseOrderEntity ent = maybe.get();
        ent.setStatus("REJECTED");
        purchaseOrderRepository.save(ent);
    }

    public void markAsReceived(UUID poId) throws ProcurementException {
        Optional<PurchaseOrderEntity> maybe = purchaseOrderRepository.findById(poId);
        if (maybe.isEmpty())
            throw new ProcurementException("Purchase order not found");
        PurchaseOrderEntity ent = maybe.get();
        ent.setStatus("RECEIVED");
        purchaseOrderRepository.save(ent);
        auditService.logAction("purchase_order", ent.getId().toString(), "RECEIVED", "Purchase order received");
        publishEvent("GoodsReceived", ent.getId().toString());
    }

    public List<PurchaseOrder> getPurchaseOrders(String status) {
        List<PurchaseOrder> result = new ArrayList<>();
        for (PurchaseOrderEntity ent : purchaseOrderRepository.findAll()) {
            if (status == null || ent.getStatus().equals(status)) {
                result.add(new PurchaseOrder(ent.getId(), ent.getPoNumber(), ent.getSupplierId(), ent.getPoDate(),
                        ent.getTotalAmount(), ent.getTaxAmount()));
            }
        }
        return result;
    }

    public PurchaseOrder getPurchaseOrder(UUID poId) throws ProcurementException {
        Optional<PurchaseOrderEntity> maybe = purchaseOrderRepository.findById(poId);
        if (maybe.isEmpty())
            throw new ProcurementException("Purchase order not found");
        PurchaseOrderEntity ent = maybe.get();
        return new PurchaseOrder(ent.getId(), ent.getPoNumber(), ent.getSupplierId(), ent.getPoDate(),
                ent.getTotalAmount(), ent.getTaxAmount());
    }

    private void publishEvent(String eventType, String eventData) {
        try {
            kafkaTemplate.send("procurement-events", eventType, eventData);
        } catch (Exception e) {
            System.out.println("Failed to publish procurement event: " + e.getMessage());
        }
    }
}
