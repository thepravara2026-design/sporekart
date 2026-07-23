package com.sporekart.identity.interfaces.rest;

import com.sporekart.identity.application.dto.DeviceInfo;
import com.sporekart.identity.application.service.DeviceService;
import com.sporekart.identity.domain.model.Device;
import com.sporekart.identity.domain.model.Device.DeviceType;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/me")
    public ResponseEntity<List<Device>> myDevices(Authentication authentication) {
        return ResponseEntity.ok(deviceService.getUserDevices(authentication.getName()));
    }

    @PostMapping("/register")
    public ResponseEntity<Device> registerDevice(Authentication authentication,
                                                  @Valid @RequestBody DeviceInfo info) {
        var type = DeviceType.valueOf(info.deviceType().toUpperCase());
        var device = deviceService.registerDevice(
                authentication.getName(), info.deviceName(), type,
                info.os(), info.browser(), info.deviceIdentifier());
        return ResponseEntity.ok(device);
    }

    @PostMapping("/{deviceId}/trust")
    public ResponseEntity<Void> markTrusted(@PathVariable String deviceId) {
        deviceService.markTrusted(deviceId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> removeDevice(@PathVariable String deviceId) {
        deviceService.removeDevice(deviceId);
        return ResponseEntity.noContent().build();
    }
}
