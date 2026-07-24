package com.sporekart.cart.infrastructure.persistence;

import com.sporekart.cart.domain.model.Cart;
import com.sporekart.cart.domain.model.CartItem;
import com.sporekart.cart.domain.model.CartStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "carts")
public class CartEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "customer_id", nullable = false, length = 100)
    private String customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private CartStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItemEntity> items = new ArrayList<>();

    protected CartEntity() {}

    public CartEntity(String id, String customerId, CartStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CartEntity fromDomain(Cart cart) {
        CartEntity entity = new CartEntity(
            cart.getId(), cart.getCustomerId(), cart.getStatus(),
            cart.getCreatedAt(), cart.getUpdatedAt());
        entity.items = cart.getItems().stream()
            .map(item -> CartItemEntity.fromDomain(item, entity))
            .collect(Collectors.toList());
        return entity;
    }

    public Cart toDomain() {
        Cart cart = new Cart(id, customerId, new ArrayList<>(), status, createdAt, updatedAt);
        for (CartItemEntity itemEntity : items) {
            CartItem item = itemEntity.toDomain();
            cart.addItem(item.getProductId(), item.getSku(), item.getName(),
                item.getUnitPrice(), item.getQuantity());
        }
        return cart;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public CartStatus getStatus() { return status; }
    public void setStatus(CartStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public List<CartItemEntity> getItems() { return items; }
    public void setItems(List<CartItemEntity> items) { this.items = items; }
}
