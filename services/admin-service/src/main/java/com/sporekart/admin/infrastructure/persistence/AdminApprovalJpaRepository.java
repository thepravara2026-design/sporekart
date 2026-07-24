package com.sporekart.admin.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminApprovalJpaRepository extends JpaRepository<ApprovalRequestEntity, String> {
}
