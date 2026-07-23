package com.sporekart.admin.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record InventoryInsight(
    int totalProducts,
    int totalStock,
    List<Map<String, Object>> lowStockItems,
    List<Map<String, Object>> outOfStockItems,
    List<Map<String, Object>> fastMoving,
    List<Map<String, Object>> slowMoving,
    BigDecimal inventoryValue,
    BigDecimal turnoverRate
) {}
