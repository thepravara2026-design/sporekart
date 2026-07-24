package com.sporekart.cart.infrastructure.persistence;

import com.sporekart.cart.domain.model.Cart;
import com.sporekart.cart.domain.repository.CartRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCartRepository implements CartRepositoryPort {

    private final Map<String, Cart> cartsById = new ConcurrentHashMap<>();

    @Override
    public Cart save(Cart cart) {
        cartsById.put(cart.getId(), cart);
        return cart;
    }

    @Override
    public Optional<Cart> findById(String id) {
        return Optional.ofNullable(cartsById.get(id));
    }

    @Override
    public List<Cart> findByCustomerId(String customerId) {
        return cartsById.values().stream()
                .filter(cart -> cart.getCustomerId().equals(customerId))
                .toList();
    }

    @Override
    public void deleteById(String id) {
        cartsById.remove(id);
    }
}