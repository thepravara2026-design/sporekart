package com.sporekart.support.infrastructure.persistence;

import com.sporekart.support.domain.model.SupportTicket;
import com.sporekart.support.domain.model.TicketStatus;
import com.sporekart.support.domain.repository.SupportRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemorySupportRepository implements SupportRepositoryPort {
    private final Map<String, SupportTicket> ticketsById = new ConcurrentHashMap<>();

    @Override
    public SupportTicket save(SupportTicket ticket) {
        ticketsById.put(ticket.getId(), ticket);
        return ticket;
    }

    @Override
    public Optional<SupportTicket> findById(String id) {
        return Optional.ofNullable(ticketsById.get(id));
    }

    @Override
    public List<SupportTicket> findByCustomerId(String customerId) {
        return ticketsById.values().stream()
                .filter(t -> t.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<SupportTicket> findByStatus(TicketStatus status) {
        return ticketsById.values().stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupportTicket> findByAssignedTo(String assignedTo) {
        return ticketsById.values().stream()
                .filter(t -> assignedTo.equals(t.getAssignedTo()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SupportTicket> findAll() {
        return new ArrayList<>(ticketsById.values());
    }

    @Override
    public void deleteById(String id) {
        ticketsById.remove(id);
    }
}