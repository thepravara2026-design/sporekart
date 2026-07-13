package com.sporekart.ai.compliance.infrastructure.persistence;

import com.sporekart.ai.compliance.domain.ComplianceRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComplianceRuleRepository extends JpaRepository<ComplianceRule, UUID> {
    List<ComplianceRule> findByFrameworkId(UUID frameworkId);
    List<ComplianceRule> findByFrameworkIdAndActiveTrue(UUID frameworkId);
}
