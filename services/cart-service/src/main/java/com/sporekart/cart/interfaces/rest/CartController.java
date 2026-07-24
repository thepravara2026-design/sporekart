package com.sporekart.cart.interfaces.rest;

import com.sporekart.cart.application.dto.AddCartItemRequest;
import com.sporekart.cart.application.dto.CartResponse;
import com.sporekart.cart.application.dto.UpdateCartItemRequest;
import com.sporekart.cart.application.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> createCart(@RequestParam String customerId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCart(customerId));
    }

    @GetMapping("/{cartId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> getCart(@PathVariable String cartId) {
        return ResponseEntity.ok(cartService.getCart(cartId));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CartResponse>> getCartsByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(cartService.getCartsByCustomer(customerId));
    }

    @PostMapping("/{cartId}/items")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> addItem(@PathVariable String cartId,
            @Valid @RequestBody AddCartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(cartId, request));
    }

    @PutMapping("/{cartId}/items/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> updateItem(@PathVariable String cartId, @PathVariable String itemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateItem(cartId, itemId, request));
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> removeItem(@PathVariable String cartId, @PathVariable String itemId) {
        return ResponseEntity.ok(cartService.removeItem(cartId, itemId));
    }

    @PostMapping("/{cartId}/checkout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> checkout(@PathVariable String cartId) {
        return ResponseEntity.ok(cartService.checkout(cartId));
    }
}