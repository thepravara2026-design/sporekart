package com.sporekart.inventory.infrastructure.persistence;

import com.sporekart.inventory.domain.model.InventoryItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory_items")
public class InventoryItemEntity {

    @Id
    @Column(name = "product_id", length = 100)
    private String productId;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;

    protected InventoryItemEntity() {
    }

    public InventoryItemEntity(String productId, int stockQuantity, int reservedQuantity, int availableQuantity) {
        this.productId = productId;
        this.stockQuantity = stockQuantity;
        this.reservedQuantity = reservedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public static InventoryItemEntity fromDomain(InventoryItem item) {
        return new InventoryItemEntity(
            item.getProductId(),
            item.getStockQuantity(),
            item.getReservedQuantity(),
            item.getAvailableQuantity()
        );
    }

    public InventoryItem toDomain() {
        return new InventoryItem(productId, stockQuantity, reservedQuantity, availableQuantity);
    }

    public String getProductId() {
        return productId;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }
}
