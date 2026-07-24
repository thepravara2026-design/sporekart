package com.sporekart.cart.application.dto;

import jakarta.validation.constraints.Positive;

public record UpdateCartItemRequest(
        @Positive int quantity) {
}