package com.sporekart.admin.domain.repository;

import com.sporekart.admin.domain.model.ApprovalRequest;
import com.sporekart.admin.domain.model.SupportTicket;

import java.util.List;
import java.util.Optional;

public interface AdminRepositoryPort {
    SupportTicket saveTicket(SupportTicket ticket);

    List<SupportTicket> findAllTickets();

    Optional<SupportTicket> findTicketById(String id);

    ApprovalRequest saveApproval(ApprovalRequest approval);

    List<ApprovalRequest> findAllApprovals();
}
