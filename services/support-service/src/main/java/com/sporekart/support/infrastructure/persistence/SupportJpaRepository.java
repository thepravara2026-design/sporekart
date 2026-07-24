package com.sporekart.support.infrastructure.persistence;

import com.sporekart.support.domain.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportJpaRepository extends JpaRepository<SupportTicketEntity, String> {
    List<SupportTicketEntity> findByCustomerId(String customerId);
    List<SupportTicketEntity> findByStatus(TicketStatus status);
    List<SupportTicketEntity> findByAssignedTo(String assignedTo);
}
