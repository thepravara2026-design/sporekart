package com.sporekart.support.application.dto;

import com.sporekart.support.domain.model.TicketStatus;
import jakarta.validation.constraints.Size;

public record UpdateTicketRequest(
        TicketStatus status,
        @Size(max = 4000) String resolution,
        String assignedTo) {
}