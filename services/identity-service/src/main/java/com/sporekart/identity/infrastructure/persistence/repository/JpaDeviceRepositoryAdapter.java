package com.sporekart.identity.infrastructure.persistence.repository;

import com.sporekart.identity.domain.model.Device;
import com.sporekart.identity.domain.repository.DeviceRepositoryPort;
import com.sporekart.identity.infrastructure.persistence.entity.DeviceEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaDeviceRepositoryAdapter implements DeviceRepositoryPort {

    private final SpringDataDeviceRepository repository;

    public JpaDeviceRepositoryAdapter(SpringDataDeviceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Device save(Device device) {
        return toDomain(repository.save(toEntity(device)));
    }

    @Override
    public Optional<Device> findById(String deviceId) {
        return repository.findById(deviceId).map(this::toDomain);
    }

    @Override
    public List<Device> findByUserId(String userId) {
        return repository.findByUserId(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Device> findByIdentifier(String deviceIdentifier) {
        return repository.findByDeviceIdentifier(deviceIdentifier).map(this::toDomain);
    }

    @Override
    public void deleteById(String deviceId) {
        repository.deleteById(deviceId);
    }

    @Override
    public long countByUserId(String userId) {
        return repository.countByUserId(userId);
    }

    private Device toDomain(DeviceEntity entity) {
        var device = new Device(
                entity.getDeviceId(), entity.getUserId(), entity.getName(),
                entity.getType(), entity.getOs(), entity.getBrowser(),
                entity.getDeviceIdentifier());
        if (entity.isTrusted()) device.markTrusted();
        if (entity.isSuspicious()) device.markSuspicious();
        return device;
    }

    private DeviceEntity toEntity(Device device) {
        var entity = new DeviceEntity();
        entity.setDeviceId(device.getDeviceId());
        entity.setUserId(device.getUserId());
        entity.setName(device.getName());
        entity.setType(device.getType());
        entity.setOs(device.getOs());
        entity.setBrowser(device.getBrowser());
        entity.setDeviceIdentifier(device.getDeviceIdentifier());
        entity.setTrusted(device.isTrusted());
        entity.setSuspicious(device.isSuspicious());
        entity.setLastUsedAt(device.getLastUsedAt());
        entity.setCreatedAt(device.getCreatedAt());
        return entity;
    }
}
