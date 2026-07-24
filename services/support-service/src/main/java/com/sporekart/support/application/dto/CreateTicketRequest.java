package com.sporekart.support.application.dto;

import com.sporekart.support.domain.model.SupportCategory;
import com.sporekart.support.domain.model.SupportPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
        @NotBlank String customerId,
        @NotBlank @Size(max = 200) String subject,
        @NotBlank @Size(max = 4000) String description,
        @NotNull SupportCategory category,
        @NotNull SupportPriority priority) {
}