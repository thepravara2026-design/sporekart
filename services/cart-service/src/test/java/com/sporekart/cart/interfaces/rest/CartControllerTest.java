package com.sporekart.cart.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.cart.application.dto.AddCartItemRequest;
import com.sporekart.cart.application.dto.CartResponse;
import com.sporekart.cart.application.dto.UpdateCartItemRequest;
import com.sporekart.cart.application.service.CartService;
import com.sporekart.cart.domain.model.CartItem;
import com.sporekart.cart.domain.model.CartStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    @Test
    void getCartReturns200() throws Exception {
        CartResponse response = new CartResponse("cart-1", "cust-1", List.of(), CartStatus.ACTIVE,
                Instant.now(), Instant.now());
        when(cartService.getCart("cart-1")).thenReturn(response);

        mockMvc.perform(get("/carts/cart-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart-1"));
    }

    @Test
    void createCartReturns201() throws Exception {
        CartResponse response = new CartResponse("cart-1", "cust-1", List.of(), CartStatus.ACTIVE,
                Instant.now(), Instant.now());
        when(cartService.createCart("cust-1")).thenReturn(response);

        mockMvc.perform(post("/carts").param("customerId", "cust-1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("cart-1"));
    }

    @Test
    void addItemReturns200() throws Exception {
        CartItem item = CartItem.create("prod-1", "SKU-1", "Item", BigDecimal.valueOf(10), 2);
        CartResponse response = new CartResponse("cart-1", "cust-1", List.of(item), CartStatus.ACTIVE,
                Instant.now(), Instant.now());
        when(cartService.addItem(eq("cart-1"), any(AddCartItemRequest.class))).thenReturn(response);

        AddCartItemRequest request = new AddCartItemRequest("prod-1", "SKU-1", "Item", BigDecimal.valueOf(10), 2);
        mockMvc.perform(post("/carts/cart-1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart-1"));
    }

    @Test
    void updateItemReturns200() throws Exception {
        CartItem item = CartItem.create("prod-1", "SKU-1", "Item", BigDecimal.valueOf(10), 5);
        CartResponse response = new CartResponse("cart-1", "cust-1", List.of(item), CartStatus.ACTIVE,
                Instant.now(), Instant.now());
        when(cartService.updateItem(eq("cart-1"), eq("item-1"), any(UpdateCartItemRequest.class))).thenReturn(response);

        UpdateCartItemRequest request = new UpdateCartItemRequest(5);
        mockMvc.perform(put("/carts/cart-1/items/item-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart-1"));
    }

    @Test
    void removeItemReturns200() throws Exception {
        CartResponse response = new CartResponse("cart-1", "cust-1", List.of(), CartStatus.ACTIVE,
                Instant.now(), Instant.now());
        when(cartService.removeItem("cart-1", "item-1")).thenReturn(response);

        mockMvc.perform(delete("/carts/cart-1/items/item-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart-1"));
    }

    @Test
    void checkoutReturns200() throws Exception {
        CartResponse response = new CartResponse("cart-1", "cust-1", List.of(), CartStatus.CHECKED_OUT,
                Instant.now(), Instant.now());
        when(cartService.checkout("cart-1")).thenReturn(response);

        mockMvc.perform(post("/carts/cart-1/checkout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CHECKED_OUT"));
    }
}