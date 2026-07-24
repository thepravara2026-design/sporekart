package com.sporekart.identity.domain.repository;

import com.sporekart.identity.domain.model.Device;
import java.util.List;
import java.util.Optional;

public interface DeviceRepositoryPort {
    Device save(Device device);
    Optional<Device> findById(String deviceId);
    List<Device> findByUserId(String userId);
    Optional<Device> findByIdentifier(String deviceIdentifier);
    void deleteById(String deviceId);
    long countByUserId(String userId);
}
