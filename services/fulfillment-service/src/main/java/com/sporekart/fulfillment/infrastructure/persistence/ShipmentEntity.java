package com.sporekart.fulfillment.infrastructure.persistence;

import com.sporekart.fulfillment.domain.model.Shipment;
import com.sporekart.fulfillment.domain.model.ShipmentItem;
import com.sporekart.fulfillment.domain.model.ShipmentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "shipments")
public class ShipmentEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "order_id", nullable = false, length = 36)
    private String orderId;

    @Column(name = "customer_id", nullable = false, length = 100)
    private String customerId;

    @Column(name = "shipping_charge", nullable = false, precision = 10, scale = 2)
    private BigDecimal shippingCharge;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ShipmentStatus status;

    @Column(name = "awb", length = 100)
    private String awb;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ShipmentItemEntity> items = new ArrayList<>();

    protected ShipmentEntity() {}

    public ShipmentEntity(String id, String orderId, String customerId, BigDecimal shippingCharge,
                          ShipmentStatus status, String awb, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.shippingCharge = shippingCharge;
        this.status = status;
        this.awb = awb;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ShipmentEntity fromDomain(Shipment shipment) {
        ShipmentEntity entity = new ShipmentEntity(
            shipment.getId(), shipment.getOrderId(), shipment.getCustomerId(),
            shipment.getShippingCharge(), shipment.getStatus(), shipment.getAwb(),
            shipment.getCreatedAt(), shipment.getUpdatedAt());
        entity.items = shipment.getItems().stream()
            .map(item -> ShipmentItemEntity.fromDomain(item, entity))
            .collect(Collectors.toList());
        return entity;
    }

    public Shipment toDomain() {
        List<ShipmentItem> domainItems = items.stream()
            .map(ShipmentItemEntity::toDomain)
            .collect(Collectors.toList());
        return new Shipment(id, orderId, customerId, shippingCharge, status, domainItems, awb, createdAt, updatedAt);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public BigDecimal getShippingCharge() { return shippingCharge; }
    public void setShippingCharge(BigDecimal shippingCharge) { this.shippingCharge = shippingCharge; }
    public ShipmentStatus getStatus() { return status; }
    public void setStatus(ShipmentStatus status) { this.status = status; }
    public String getAwb() { return awb; }
    public void setAwb(String awb) { this.awb = awb; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public List<ShipmentItemEntity> getItems() { return items; }
    public void setItems(List<ShipmentItemEntity> items) { this.items = items; }
}
