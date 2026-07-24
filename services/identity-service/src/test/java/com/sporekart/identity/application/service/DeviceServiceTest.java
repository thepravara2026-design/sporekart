package com.sporekart.identity.application.service;

import com.sporekart.identity.domain.model.Device;
import com.sporekart.identity.domain.repository.DeviceRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DeviceServiceTest {

    private DeviceRepositoryPort deviceRepository;
    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        deviceRepository = mock(DeviceRepositoryPort.class);
        deviceService = new DeviceService(deviceRepository);
    }

    @Test
    void shouldRegisterDevice() {
        when(deviceRepository.findByIdentifier(anyString())).thenReturn(Optional.empty());
        when(deviceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var device = deviceService.registerDevice("user-1", "My Phone", Device.DeviceType.MOBILE,
                "iOS", "Safari", "id-123");

        assertNotNull(device);
        assertEquals("user-1", device.getUserId());
        assertEquals("My Phone", device.getName());
        assertEquals(Device.DeviceType.MOBILE, device.getType());
        assertEquals("iOS", device.getOs());
        assertEquals("Safari", device.getBrowser());
        assertEquals("id-123", device.getDeviceIdentifier());
        verify(deviceRepository).save(any(Device.class));
    }

    @Test
    void shouldReturnExistingDeviceByIdentifier() {
        var existingDevice = new Device("dev-1", "user-1", "My Phone", Device.DeviceType.MOBILE,
                "iOS", "Safari", "id-123");
        when(deviceRepository.findByIdentifier("id-123")).thenReturn(Optional.of(existingDevice));
        when(deviceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var device = deviceService.registerDevice("user-1", "My Phone", Device.DeviceType.MOBILE,
                "iOS", "Safari", "id-123");

        assertNotNull(device);
        assertEquals("dev-1", device.getDeviceId());
        verify(deviceRepository).save(existingDevice);
    }

    @Test
    void shouldMarkTrusted() {
        var device = new Device("dev-1", "user-1", "My Phone", Device.DeviceType.MOBILE,
                "iOS", "Safari", "id-123");
        when(deviceRepository.findById("dev-1")).thenReturn(Optional.of(device));
        when(deviceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        deviceService.markTrusted("dev-1");

        assertTrue(device.isTrusted());
        assertFalse(device.isSuspicious());
        verify(deviceRepository).save(device);
    }

    @Test
    void shouldMarkSuspicious() {
        var device = new Device("dev-1", "user-1", "My Phone", Device.DeviceType.MOBILE,
                "iOS", "Safari", "id-123");
        when(deviceRepository.findById("dev-1")).thenReturn(Optional.of(device));
        when(deviceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        deviceService.markSuspicious("dev-1");

        assertFalse(device.isTrusted());
        assertTrue(device.isSuspicious());
        verify(deviceRepository).save(device);
    }

    @Test
    void shouldRemoveDevice() {
        deviceService.removeDevice("dev-1");

        verify(deviceRepository).deleteById("dev-1");
    }
}
