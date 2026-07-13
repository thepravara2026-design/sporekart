package com.sporekart.ai.policy.application;

import com.sporekart.ai.policy.api.PolicyLifecycleManager;
import com.sporekart.ai.policy.domain.*;
import com.sporekart.ai.policy.infrastructure.persistence.PolicyEntity;
import com.sporekart.ai.policy.infrastructure.persistence.PolicyRepository;
import com.sporekart.ai.policy.infrastructure.persistence.PolicyVersionEntity;
import com.sporekart.ai.policy.infrastructure.persistence.PolicyVersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyLifecycleManagerImpl implements PolicyLifecycleManager {

    private final PolicyRepository policyRepository;
    private final PolicyVersionRepository versionRepository;

    @Override
    public Policy activatePolicy(UUID policyId, String triggeredBy) {
        return updateStatus(policyId, PolicyStatus.ACTIVE, triggeredBy);
    }

    @Override
    public Policy deactivatePolicy(UUID policyId, String triggeredBy) {
        return updateStatus(policyId, PolicyStatus.INACTIVE, triggeredBy);
    }

    @Override
    public Policy archivePolicy(UUID policyId, String triggeredBy) {
        return updateStatus(policyId, PolicyStatus.ARCHIVED, triggeredBy);
    }

    @Override
    public Policy draftPolicy(Policy policy, String triggeredBy) {
        return updateStatus(policy.id(), PolicyStatus.DRAFT, triggeredBy);
    }

    @Override
    public PolicyVersion createVersion(UUID policyId, String changeNotes, String triggeredBy) {
        PolicyVersionEntity entity = new PolicyVersionEntity();
        entity.setId(UUID.randomUUID());
        entity.setPolicyId(policyId);
        entity.setVersionNumber(getNextVersion(policyId));
        entity.setChangeNotes(changeNotes);
        entity.setCreatedBy(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        entity.setCreatedAt(OffsetDateTime.now());
        versionRepository.save(entity);
        return new PolicyVersion(
            entity.getId(), entity.getPolicyId(), entity.getVersionNumber(),
            null, null, null, PolicyStatus.DRAFT,
            entity.getChangeNotes(), null, entity.getCreatedAt()
        );
    }

    @Override
    public List<PolicyVersion> getVersions(UUID policyId) {
        return versionRepository.findByPolicyIdAndIsDeletedFalse(policyId).stream()
            .map(e -> new PolicyVersion(
                e.getId(), e.getPolicyId(), e.getVersionNumber(),
                e.getName(), e.getDescription(), e.getContent(),
                PolicyStatus.valueOf(e.getStatus()), e.getChangeNotes(),
                e.getCreatedBy(), e.getCreatedAt()
            )).toList();
    }

    @Override
    public boolean canTransition(PolicyStatus from, PolicyStatus to) {
        Set<String> valid = Set.of(
            "DRAFT->ACTIVE", "DRAFT->ARCHIVED",
            "ACTIVE->INACTIVE", "ACTIVE->ARCHIVED",
            "INACTIVE->ACTIVE", "INACTIVE->ARCHIVED",
            "ARCHIVED->DEPRECATED"
        );
        return valid.contains(from.name() + "->" + to.name());
    }

    private Policy updateStatus(UUID policyId, PolicyStatus newStatus, String triggeredBy) {
        policyRepository.findById(policyId).ifPresent(e -> {
            e.setStatus(newStatus.name());
            e.setUpdatedAt(OffsetDateTime.now());
            policyRepository.save(e);
            log.info("Policy {} status changed to {} by {}", policyId, newStatus, triggeredBy);
        });
        return null;
    }

    private int getNextVersion(UUID policyId) {
        return versionRepository.findByPolicyIdAndIsDeletedFalse(policyId).size() + 1;
    }
}
