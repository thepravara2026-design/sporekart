package com.sporekart.identity.infrastructure.persistence.repository;

import com.sporekart.identity.infrastructure.persistence.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataDeviceRepository extends JpaRepository<DeviceEntity, String> {
    List<DeviceEntity> findByUserId(String userId);
    Optional<DeviceEntity> findByDeviceIdentifier(String deviceIdentifier);
    long countByUserId(String userId);
}
