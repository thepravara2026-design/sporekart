package com.sporekart.events.domain.catalog;

import com.sporekart.events.model.DomainEvent;
import java.math.BigDecimal;

public class ProductCreated extends DomainEvent {
    private final String productId;
    private final String name;
    private final BigDecimal price;

    private ProductCreated(Builder builder) {
        super(builder);
        this.productId = builder.productId;
        this.name = builder.name;
        this.price = builder.price;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String productId;
        private String name;
        private BigDecimal price;

        public Builder productId(String productId) { this.productId = productId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder price(BigDecimal price) { this.price = price; return this; }

        public ProductCreated build() {
            return new ProductCreated(this);
        }
    }
}
