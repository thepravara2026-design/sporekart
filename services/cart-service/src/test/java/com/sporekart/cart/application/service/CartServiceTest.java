package com.sporekart.cart.application.service;

import com.sporekart.cart.application.dto.AddCartItemRequest;
import com.sporekart.cart.application.dto.CartResponse;
import com.sporekart.cart.application.dto.UpdateCartItemRequest;
import com.sporekart.cart.common.exception.CartNotFoundException;
import com.sporekart.cart.common.exception.InvalidCartOperationException;
import com.sporekart.cart.domain.model.Cart;
import com.sporekart.cart.domain.model.CartStatus;
import com.sporekart.cart.domain.repository.CartRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CartServiceTest {

    @Test
    void createCartReturnsResponse() {
        CartRepositoryPort repositoryPort = Mockito.mock(CartRepositoryPort.class);
        when(repositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        CartService service = new CartService(repositoryPort);

        CartResponse response = service.createCart("customer-1");

        assertNotNull(response);
        assertEquals("customer-1", response.customerId());
        assertEquals(CartStatus.ACTIVE, response.status());
    }

    @Test
    void getCartReturnsResponseWhenFound() {
        CartRepositoryPort repositoryPort = Mockito.mock(CartRepositoryPort.class);
        Cart cart = Cart.create("customer-1");
        when(repositoryPort.findById(cart.getId())).thenReturn(Optional.of(cart));
        CartService service = new CartService(repositoryPort);

        CartResponse response = service.getCart(cart.getId());

        assertNotNull(response);
        assertEquals(cart.getId(), response.id());
    }

    @Test
    void getCartThrowsWhenNotFound() {
        CartRepositoryPort repositoryPort = Mockito.mock(CartRepositoryPort.class);
        when(repositoryPort.findById("unknown")).thenReturn(Optional.empty());
        CartService service = new CartService(repositoryPort);

        assertThrows(CartNotFoundException.class, () -> service.getCart("unknown"));
    }

    @Test
    void addItemWithValidDataSucceeds() {
        CartRepositoryPort repositoryPort = Mockito.mock(CartRepositoryPort.class);
        Cart cart = Cart.create("customer-1");
        when(repositoryPort.findById(cart.getId())).thenReturn(Optional.of(cart));
        when(repositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        CartService service = new CartService(repositoryPort);

        AddCartItemRequest request = new AddCartItemRequest("prod-1", "SKU-1", "Item One", BigDecimal.valueOf(10), 2);
        CartResponse response = service.addItem(cart.getId(), request);

        assertEquals(1, response.items().size());
        assertEquals("prod-1", response.items().getFirst().getProductId());
    }

    @Test
    void addDuplicateProductIncrementsQuantity() {
        CartRepositoryPort repositoryPort = Mockito.mock(CartRepositoryPort.class);
        Cart cart = Cart.create("customer-1");
        cart.addItem("prod-1", "SKU-1", "Item One", BigDecimal.valueOf(10), 2);
        when(repositoryPort.findById(cart.getId())).thenReturn(Optional.of(cart));
        when(repositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        CartService service = new CartService(repositoryPort);

        AddCartItemRequest request = new AddCartItemRequest("prod-1", "SKU-1", "Item One", BigDecimal.valueOf(10), 3);
        CartResponse response = service.addItem(cart.getId(), request);

        assertEquals(1, response.items().size());
        assertEquals(5, response.items().getFirst().getQuantity());
    }

    @Test
    void checkoutEmptyCartThrows() {
        CartRepositoryPort repositoryPort = Mockito.mock(CartRepositoryPort.class);
        Cart cart = Cart.create("customer-1");
        when(repositoryPort.findById(cart.getId())).thenReturn(Optional.of(cart));
        when(repositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        CartService service = new CartService(repositoryPort);

        assertThrows(InvalidCartOperationException.class, () -> service.checkout(cart.getId()));
    }

    @Test
    void checkoutNonEmptyCartSucceeds() {
        CartRepositoryPort repositoryPort = Mockito.mock(CartRepositoryPort.class);
        Cart cart = Cart.create("customer-1");
        cart.addItem("prod-1", "SKU-1", "Item One", BigDecimal.valueOf(10), 2);
        when(repositoryPort.findById(cart.getId())).thenReturn(Optional.of(cart));
        when(repositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        CartService service = new CartService(repositoryPort);

        CartResponse response = service.checkout(cart.getId());

        assertEquals(CartStatus.CHECKED_OUT, response.status());
    }

    @Test
    void updateItemQuantityUpdatesSubtotal() {
        CartRepositoryPort repositoryPort = Mockito.mock(CartRepositoryPort.class);
        Cart cart = Cart.create("customer-1");
        cart.addItem("prod-1", "SKU-1", "Item One", BigDecimal.valueOf(10), 2);
        String itemId = cart.getItems().getFirst().getId();
        when(repositoryPort.findById(cart.getId())).thenReturn(Optional.of(cart));
        when(repositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        CartService service = new CartService(repositoryPort);

        UpdateCartItemRequest request = new UpdateCartItemRequest(5);
        CartResponse response = service.updateItem(cart.getId(), itemId, request);

        assertEquals(5, response.items().getFirst().getQuantity());
        assertEquals(BigDecimal.valueOf(50), response.items().getFirst().getSubtotal());
    }

    @Test
    void removeItemRemovesFromCart() {
        CartRepositoryPort repositoryPort = Mockito.mock(CartRepositoryPort.class);
        Cart cart = Cart.create("customer-1");
        cart.addItem("prod-1", "SKU-1", "Item One", BigDecimal.valueOf(10), 2);
        String itemId = cart.getItems().getFirst().getId();
        when(repositoryPort.findById(cart.getId())).thenReturn(Optional.of(cart));
        when(repositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        CartService service = new CartService(repositoryPort);

        CartResponse response = service.removeItem(cart.getId(), itemId);

        assertTrue(response.items().isEmpty());
    }
}