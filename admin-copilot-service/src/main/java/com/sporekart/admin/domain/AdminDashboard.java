package com.sporekart.admin.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdminDashboard(
    BigDecimal totalRevenue,
    int totalOrders,
    int totalCustomers,
    int totalProducts,
    int activeUsers,
    int pendingOrders,
    int lowStockItems,
    BigDecimal todaySales,
    BigDecimal growthRate,
    LocalDateTime periodStart,
    LocalDateTime periodEnd
) {}
