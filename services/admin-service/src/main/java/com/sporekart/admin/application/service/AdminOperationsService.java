package com.sporekart.admin.application.service;

import com.sporekart.admin.domain.model.ApprovalRequest;
import com.sporekart.admin.domain.model.SupportTicket;
import com.sporekart.admin.domain.repository.AdminRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminOperationsService {
    private final AdminRepositoryPort repositoryPort;

    public AdminOperationsService(AdminRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public Map<String, Object> getDashboardSummary() {
        return Map.of(
                "pendingApprovals",
                repositoryPort.findAllApprovals().stream().filter(a -> a.getStatus().name().equals("PENDING")).count(),
                "openTickets",
                repositoryPort.findAllTickets().stream().filter(t -> t.getStatus().name().equals("OPEN")).count(),
                "systemHealth", "HEALTHY");
    }

    public SupportTicket createSupportTicket(String subject, String description, String requester) {
        SupportTicket ticket = SupportTicket.create(subject, description, requester);
        return repositoryPort.saveTicket(ticket);
    }

    public Optional<SupportTicket> getSupportTicket(String id) {
        return repositoryPort.findTicketById(id);
    }

    public List<SupportTicket> listSupportTickets() {
        return repositoryPort.findAllTickets();
    }

    public ApprovalRequest createApproval(String targetType, String targetId) {
        ApprovalRequest approval = ApprovalRequest.create(targetType, targetId);
        return repositoryPort.saveApproval(approval);
    }

    public List<ApprovalRequest> listApprovals() {
        return repositoryPort.findAllApprovals();
    }
}
