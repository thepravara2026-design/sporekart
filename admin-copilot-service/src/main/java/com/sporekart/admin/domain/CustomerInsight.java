package com.sporekart.admin.domain;

import java.math.BigDecimal;
import java.util.List;

public record CustomerInsight(
    int totalCustomers,
    int newCustomers,
    int returningCustomers,
    BigDecimal churnRate,
    BigDecimal retentionRate,
    BigDecimal averageLifetimeValue,
    List<String> topCustomers,
    int inactiveCustomers
) {}
