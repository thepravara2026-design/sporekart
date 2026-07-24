package com.sporekart.support.infrastructure.persistence;

import com.sporekart.support.domain.model.SupportTicket;
import com.sporekart.support.domain.model.TicketStatus;
import com.sporekart.support.domain.repository.SupportRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class JpaSupportRepositoryAdapter implements SupportRepositoryPort {

    private final SupportJpaRepository jpaRepository;

    public JpaSupportRepositoryAdapter(SupportJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SupportTicket save(SupportTicket ticket) {
        return jpaRepository.save(SupportTicketEntity.fromDomain(ticket)).toDomain();
    }

    @Override
    public Optional<SupportTicket> findById(String id) {
        return jpaRepository.findById(id).map(SupportTicketEntity::toDomain);
    }

    @Override
    public List<SupportTicket> findByCustomerId(String customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
                .map(SupportTicketEntity::toDomain).toList();
    }

    @Override
    public List<SupportTicket> findByStatus(TicketStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(SupportTicketEntity::toDomain).toList();
    }

    @Override
    public List<SupportTicket> findByAssignedTo(String assignedTo) {
        return jpaRepository.findByAssignedTo(assignedTo).stream()
                .map(SupportTicketEntity::toDomain).toList();
    }

    @Override
    public List<SupportTicket> findAll() {
        return jpaRepository.findAll().stream().map(SupportTicketEntity::toDomain).toList();
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
