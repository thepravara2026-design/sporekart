package com.sporekart.cart.infrastructure.persistence;

import com.sporekart.cart.domain.model.Cart;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCartRepositoryTest {

    @Test
    void saveAndFindByIdRoundTrip() {
        InMemoryCartRepository repository = new InMemoryCartRepository();
        Cart cart = Cart.create("customer-1");

        Cart saved = repository.save(cart);

        assertEquals(saved.getId(), repository.findById(saved.getId()).orElseThrow().getId());
    }

    @Test
    void findByCustomerIdReturnsMatchingCarts() {
        InMemoryCartRepository repository = new InMemoryCartRepository();
        Cart cart1 = Cart.create("customer-1");
        Cart cart2 = Cart.create("customer-1");
        Cart cart3 = Cart.create("customer-2");
        repository.save(cart1);
        repository.save(cart2);
        repository.save(cart3);

        List<Cart> customer1Carts = repository.findByCustomerId("customer-1");

        assertEquals(2, customer1Carts.size());
    }

    @Test
    void deleteByIdRemovesCart() {
        InMemoryCartRepository repository = new InMemoryCartRepository();
        Cart cart = Cart.create("customer-1");
        repository.save(cart);

        repository.deleteById(cart.getId());

        assertTrue(repository.findById(cart.getId()).isEmpty());
    }

    @Test
    void findByIdReturnsEmptyForUnknownId() {
        InMemoryCartRepository repository = new InMemoryCartRepository();

        assertTrue(repository.findById("unknown").isEmpty());
    }
}