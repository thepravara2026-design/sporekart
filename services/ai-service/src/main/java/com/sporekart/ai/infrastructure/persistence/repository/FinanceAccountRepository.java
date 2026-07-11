package com.sporekart.ai.infrastructure.persistence.repository;

import com.sporekart.ai.infrastructure.persistence.entity.FinanceAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FinanceAccountRepository extends JpaRepository<FinanceAccountEntity, UUID> {
    boolean existsByAccountCode(String accountCode);
}
