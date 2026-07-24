package com.sporekart.support.application.service;

import com.sporekart.support.application.dto.CreateTicketRequest;
import com.sporekart.support.application.dto.TicketResponse;
import com.sporekart.support.application.dto.UpdateTicketRequest;
import com.sporekart.support.common.exception.TicketNotFoundException;
import com.sporekart.support.domain.model.SupportTicket;
import com.sporekart.support.domain.model.TicketStatus;
import com.sporekart.support.domain.repository.SupportRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupportService.class);

    private final SupportRepositoryPort repository;

    public SupportService(SupportRepositoryPort repository) {
        this.repository = repository;
    }

    public TicketResponse createTicket(CreateTicketRequest request) {
        SupportTicket ticket = SupportTicket.create(
                request.customerId(),
                request.subject(),
                request.description(),
                request.category(),
                request.priority());
        SupportTicket saved = repository.save(ticket);
        LOGGER.info("Created ticket {} for customer {}", saved.getId(), saved.getCustomerId());
        return TicketResponse.from(saved);
    }

    public TicketResponse getTicket(String id) {
        SupportTicket ticket = repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found: " + id));
        return TicketResponse.from(ticket);
    }

    public List<TicketResponse> getCustomerTickets(String customerId) {
        return repository.findByCustomerId(customerId).stream()
                .map(TicketResponse::from)
                .toList();
    }

    public TicketResponse assignTicket(String ticketId, String agentId) {
        SupportTicket ticket = repository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found: " + ticketId));
        ticket.assignTo(agentId);
        SupportTicket saved = repository.save(ticket);
        LOGGER.info("Assigned ticket {} to agent {}", ticketId, agentId);
        return TicketResponse.from(saved);
    }

    public TicketResponse updateTicketStatus(String ticketId, TicketStatus newStatus, String resolution) {
        SupportTicket ticket = repository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found: " + ticketId));

        if (newStatus == TicketStatus.RESOLVED) {
            if (resolution == null || resolution.isBlank()) {
                throw new IllegalArgumentException("Resolution is required when resolving a ticket");
            }
            ticket.resolve(resolution);
        } else if (newStatus == TicketStatus.CLOSED) {
            ticket.updateStatus(newStatus);
        } else {
            ticket.updateStatus(newStatus);
        }

        SupportTicket saved = repository.save(ticket);
        LOGGER.info("Updated ticket {} status to {}", ticketId, newStatus);
        return TicketResponse.from(saved);
    }

    public TicketResponse resolveTicket(String ticketId, String resolution) {
        SupportTicket ticket = repository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found: " + ticketId));
        ticket.resolve(resolution);
        SupportTicket saved = repository.save(ticket);
        LOGGER.info("Resolved ticket {} with resolution", ticketId);
        return TicketResponse.from(saved);
    }

    public TicketResponse addResolution(String ticketId, String resolution) {
        SupportTicket ticket = repository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found: " + ticketId));
        ticket.addResolution(resolution);
        SupportTicket saved = repository.save(ticket);
        LOGGER.info("Added resolution to ticket {}", ticketId);
        return TicketResponse.from(saved);
    }
}