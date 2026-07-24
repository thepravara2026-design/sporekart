package com.sporekart.cart.application.service;

import com.sporekart.cart.application.dto.AddCartItemRequest;
import com.sporekart.cart.application.dto.CartResponse;
import com.sporekart.cart.application.dto.UpdateCartItemRequest;
import com.sporekart.cart.common.exception.CartNotFoundException;
import com.sporekart.cart.domain.model.Cart;
import com.sporekart.cart.domain.repository.CartRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);

    private final CartRepositoryPort cartRepositoryPort;

    public CartService(CartRepositoryPort cartRepositoryPort) {
        this.cartRepositoryPort = cartRepositoryPort;
    }

    public CartResponse createCart(String customerId) {
        Cart cart = Cart.create(customerId);
        Cart saved = cartRepositoryPort.save(cart);
        LOGGER.info("Cart created: {} for customer: {}", saved.getId(), customerId);
        return CartResponse.from(saved);
    }

    public CartResponse getCart(String cartId) {
        Cart cart = cartRepositoryPort.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found: " + cartId));
        return CartResponse.from(cart);
    }

    public List<CartResponse> getCartsByCustomer(String customerId) {
        return cartRepositoryPort.findByCustomerId(customerId)
                .stream()
                .map(CartResponse::from)
                .toList();
    }

    public CartResponse addItem(String cartId, AddCartItemRequest request) {
        Cart cart = cartRepositoryPort.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found: " + cartId));
        cart.addItem(request.productId(), request.sku(), request.name(), request.unitPrice(), request.quantity());
        Cart saved = cartRepositoryPort.save(cart);
        LOGGER.info("Item added to cart: {} for product: {}", cartId, request.productId());
        return CartResponse.from(saved);
    }

    public CartResponse updateItem(String cartId, String itemId, UpdateCartItemRequest request) {
        Cart cart = cartRepositoryPort.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found: " + cartId));
        cart.updateItemQuantity(itemId, request.quantity());
        Cart saved = cartRepositoryPort.save(cart);
        LOGGER.info("Item updated in cart: {} item: {} quantity: {}", cartId, itemId, request.quantity());
        return CartResponse.from(saved);
    }

    public CartResponse removeItem(String cartId, String itemId) {
        Cart cart = cartRepositoryPort.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found: " + cartId));
        cart.removeItem(itemId);
        Cart saved = cartRepositoryPort.save(cart);
        LOGGER.info("Item removed from cart: {} item: {}", cartId, itemId);
        return CartResponse.from(saved);
    }

    public CartResponse checkout(String cartId) {
        Cart cart = cartRepositoryPort.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found: " + cartId));
        cart.checkout();
        Cart saved = cartRepositoryPort.save(cart);
        LOGGER.info("Cart checked out: {}", cartId);
        return CartResponse.from(saved);
    }
}