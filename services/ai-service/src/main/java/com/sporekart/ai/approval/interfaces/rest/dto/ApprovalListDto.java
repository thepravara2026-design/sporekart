package com.sporekart.ai.approval.interfaces.rest.dto;

import java.util.List;

public record ApprovalListDto(
    List<ApprovalResponseDto> approvals,
    int total
) {}
