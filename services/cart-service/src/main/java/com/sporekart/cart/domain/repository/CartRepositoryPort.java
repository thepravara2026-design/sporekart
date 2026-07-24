package com.sporekart.cart.domain.repository;

import com.sporekart.cart.domain.model.Cart;

import java.util.List;
import java.util.Optional;

public interface CartRepositoryPort {
    Cart save(Cart cart);

    Optional<Cart> findById(String id);

    List<Cart> findByCustomerId(String customerId);

    void deleteById(String id);
}