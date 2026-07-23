package com.sporekart.operations.copilot.domain;

public record PurchaseLineItem(
    String sku,
    String productName,
    int quantity,
    double unitPrice,
    double totalPrice,
    int receivedQuantity
) {}
