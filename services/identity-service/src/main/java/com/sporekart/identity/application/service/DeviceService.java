package com.sporekart.identity.application.service;

import com.sporekart.identity.domain.model.Device;
import com.sporekart.identity.domain.model.Device.DeviceType;
import com.sporekart.identity.domain.repository.DeviceRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {

    private final DeviceRepositoryPort deviceRepository;

    public DeviceService(DeviceRepositoryPort deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Transactional
    public Device registerDevice(String userId, String name, DeviceType type,
                                  String os, String browser, String deviceIdentifier) {
        var existing = deviceRepository.findByIdentifier(deviceIdentifier);
        if (existing.isPresent()) {
            var device = existing.get();
            device.updateLastUsed();
            deviceRepository.save(device);
            return device;
        }
        var deviceId = UUID.randomUUID().toString();
        var device = new Device(deviceId, userId, name, type, os, browser, deviceIdentifier);
        return deviceRepository.save(device);
    }

    public List<Device> getUserDevices(String userId) {
        return deviceRepository.findByUserId(userId);
    }

    public Optional<Device> getDevice(String deviceId) {
        return deviceRepository.findById(deviceId);
    }

    @Transactional
    public void markTrusted(String deviceId) {
        deviceRepository.findById(deviceId).ifPresent(device -> {
            device.markTrusted();
            deviceRepository.save(device);
        });
    }

    @Transactional
    public void markSuspicious(String deviceId) {
        deviceRepository.findById(deviceId).ifPresent(device -> {
            device.markSuspicious();
            deviceRepository.save(device);
        });
    }

    @Transactional
    public void removeDevice(String deviceId) {
        deviceRepository.deleteById(deviceId);
    }

    public long countByUserId(String userId) {
        return deviceRepository.countByUserId(userId);
    }
}
