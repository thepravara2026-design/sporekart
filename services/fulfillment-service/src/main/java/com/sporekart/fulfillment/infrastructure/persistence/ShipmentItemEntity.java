package com.sporekart.fulfillment.infrastructure.persistence;

import com.sporekart.fulfillment.domain.model.ShipmentItem;
import jakarta.persistence.*;

@Entity
@Table(name = "shipment_items")
public class ShipmentItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private ShipmentEntity shipment;

    @Column(name = "sku", nullable = false, length = 100)
    private String sku;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    protected ShipmentItemEntity() {}

    public ShipmentItemEntity(String id, ShipmentEntity shipment, String sku, int quantity) {
        this.id = id;
        this.shipment = shipment;
        this.sku = sku;
        this.quantity = quantity;
    }

    public static ShipmentItemEntity fromDomain(ShipmentItem item, ShipmentEntity shipment) {
        return new ShipmentItemEntity(null, shipment, item.getSku(), item.getQuantity());
    }

    public ShipmentItem toDomain() {
        return new ShipmentItem(sku, quantity);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public ShipmentEntity getShipment() { return shipment; }
    public void setShipment(ShipmentEntity shipment) { this.shipment = shipment; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
