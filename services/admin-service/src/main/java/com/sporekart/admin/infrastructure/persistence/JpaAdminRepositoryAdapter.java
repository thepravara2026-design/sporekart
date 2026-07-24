package com.sporekart.admin.infrastructure.persistence;

import com.sporekart.admin.domain.model.ApprovalRequest;
import com.sporekart.admin.domain.model.SupportTicket;
import com.sporekart.admin.domain.repository.AdminRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class JpaAdminRepositoryAdapter implements AdminRepositoryPort {

    private final AdminSupportTicketJpaRepository ticketJpaRepository;
    private final AdminApprovalJpaRepository approvalJpaRepository;

    public JpaAdminRepositoryAdapter(AdminSupportTicketJpaRepository ticketJpaRepository,
                                     AdminApprovalJpaRepository approvalJpaRepository) {
        this.ticketJpaRepository = ticketJpaRepository;
        this.approvalJpaRepository = approvalJpaRepository;
    }

    @Override
    public SupportTicket saveTicket(SupportTicket ticket) {
        return ticketJpaRepository.save(SupportTicketEntity.fromDomain(ticket)).toDomain();
    }

    @Override
    public List<SupportTicket> findAllTickets() {
        return ticketJpaRepository.findAll().stream().map(SupportTicketEntity::toDomain).toList();
    }

    @Override
    public Optional<SupportTicket> findTicketById(String id) {
        return ticketJpaRepository.findById(id).map(SupportTicketEntity::toDomain);
    }

    @Override
    public ApprovalRequest saveApproval(ApprovalRequest approval) {
        return approvalJpaRepository.save(ApprovalRequestEntity.fromDomain(approval)).toDomain();
    }

    @Override
    public List<ApprovalRequest> findAllApprovals() {
        return approvalJpaRepository.findAll().stream().map(ApprovalRequestEntity::toDomain).toList();
    }
}
