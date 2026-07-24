package com.sporekart.support.domain.repository;

import com.sporekart.support.domain.model.SupportTicket;
import com.sporekart.support.domain.model.TicketStatus;

import java.util.List;
import java.util.Optional;

public interface SupportRepositoryPort {
    SupportTicket save(SupportTicket ticket);

    Optional<SupportTicket> findById(String id);

    List<SupportTicket> findByCustomerId(String customerId);

    List<SupportTicket> findByStatus(TicketStatus status);

    List<SupportTicket> findByAssignedTo(String assignedTo);

    List<SupportTicket> findAll();

    void deleteById(String id);
}