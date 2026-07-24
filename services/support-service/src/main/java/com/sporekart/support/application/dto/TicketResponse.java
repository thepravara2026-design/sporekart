package com.sporekart.support.application.dto;

import com.sporekart.support.domain.model.SupportCategory;
import com.sporekart.support.domain.model.SupportPriority;
import com.sporekart.support.domain.model.SupportTicket;
import com.sporekart.support.domain.model.TicketStatus;

import java.time.Instant;

public record TicketResponse(
        String id,
        String customerId,
        String subject,
        String description,
        SupportCategory category,
        SupportPriority priority,
        TicketStatus status,
        String assignedTo,
        String resolution,
        Instant createdAt,
        Instant updatedAt,
        Instant resolvedAt) {

    public static TicketResponse from(SupportTicket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getCustomerId(),
                ticket.getSubject(),
                ticket.getDescription(),
                ticket.getCategory(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getAssignedTo(),
                ticket.getResolution(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ticket.getResolvedAt());
    }
}