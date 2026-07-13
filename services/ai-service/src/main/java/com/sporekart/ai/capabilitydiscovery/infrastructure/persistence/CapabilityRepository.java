package com.sporekart.ai.capabilitydiscovery.infrastructure.persistence;

import com.sporekart.ai.capabilitydiscovery.domain.CapabilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapabilityRepository extends JpaRepository<CapabilityEntity, String> {

    List<CapabilityEntity> findByCapabilityType(CapabilityType capabilityType);

    List<CapabilityEntity> findByModule(String module);

    List<CapabilityEntity> findBySupportedFeaturesContaining(String feature);
}
