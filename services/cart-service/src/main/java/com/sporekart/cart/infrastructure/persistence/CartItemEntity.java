package com.sporekart.cart.infrastructure.persistence;

import com.sporekart.cart.domain.model.CartItem;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "cart_items")
public class CartItemEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    @Column(name = "product_id", nullable = false, length = 100)
    private String productId;

    @Column(name = "sku", nullable = false, length = 100)
    private String sku;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    protected CartItemEntity() {}

    public CartItemEntity(String id, CartEntity cart, String productId, String sku, String name,
                          BigDecimal unitPrice, int quantity, BigDecimal subtotal) {
        this.id = id;
        this.cart = cart;
        this.productId = productId;
        this.sku = sku;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public static CartItemEntity fromDomain(CartItem item, CartEntity cart) {
        return new CartItemEntity(
            item.getId(), cart, item.getProductId(), item.getSku(), item.getName(),
            item.getUnitPrice(), item.getQuantity(), item.getSubtotal());
    }

    public CartItem toDomain() {
        return new CartItem(id, productId, sku, name, unitPrice, quantity, subtotal);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public CartEntity getCart() { return cart; }
    public void setCart(CartEntity cart) { this.cart = cart; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
