package com.sporekart.ai.capabilitydiscovery.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapabilityHealthRepository extends JpaRepository<CapabilityHealthEntity, Long> {

    List<CapabilityHealthEntity> findByCapabilityIdOrderByCheckedAtDesc(String capabilityId);
}
