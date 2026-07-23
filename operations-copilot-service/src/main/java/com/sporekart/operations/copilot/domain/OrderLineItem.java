package com.sporekart.operations.copilot.domain;

public record OrderLineItem(
    String sku,
    String productName,
    int quantity,
    double unitPrice,
    double totalPrice
) {}
