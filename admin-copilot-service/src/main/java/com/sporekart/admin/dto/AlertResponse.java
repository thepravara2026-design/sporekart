package com.sporekart.admin.dto;

import com.sporekart.admin.domain.OperationalAlert;

import java.util.List;

public record AlertResponse(
    List<OperationalAlert> alerts,
    int totalCount,
    int criticalCount,
    int warningCount
) {}
