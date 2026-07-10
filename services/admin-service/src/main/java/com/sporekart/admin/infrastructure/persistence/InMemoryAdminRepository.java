package com.sporekart.admin.infrastructure.persistence;

import com.sporekart.admin.domain.model.ApprovalRequest;
import com.sporekart.admin.domain.model.SupportTicket;
import com.sporekart.admin.domain.repository.AdminRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAdminRepository implements AdminRepositoryPort {
    private final Map<String, SupportTicket> tickets = new ConcurrentHashMap<>();
    private final Map<String, ApprovalRequest> approvals = new ConcurrentHashMap<>();

    @Override
    public SupportTicket saveTicket(SupportTicket ticket) {
        tickets.put(ticket.getId(), ticket);
        return ticket;
    }

    @Override
    public List<SupportTicket> findAllTickets() {
        return new ArrayList<>(tickets.values());
    }

    @Override
    public Optional<SupportTicket> findTicketById(String id) {
        return Optional.ofNullable(tickets.get(id));
    }

    @Override
    public ApprovalRequest saveApproval(ApprovalRequest approval) {
        approvals.put(approval.getId(), approval);
        return approval;
    }

    @Override
    public List<ApprovalRequest> findAllApprovals() {
        return new ArrayList<>(approvals.values());
    }
}
