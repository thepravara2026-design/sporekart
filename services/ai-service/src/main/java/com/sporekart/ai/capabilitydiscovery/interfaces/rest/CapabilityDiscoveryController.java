package com.sporekart.ai.capabilitydiscovery.interfaces.rest;

import com.sporekart.ai.capabilitydiscovery.api.CapabilityDiscoveryService;
import com.sporekart.ai.capabilitydiscovery.api.CapabilityHealthService;
import com.sporekart.ai.capabilitydiscovery.api.CapabilityRegistryService;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityAvailability;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityEntry;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityType;
import com.sporekart.ai.capabilitydiscovery.interfaces.rest.dto.CapabilityDiscoveryRequestDto;
import com.sporekart.ai.capabilitydiscovery.interfaces.rest.dto.CapabilityDiscoveryResponseDto;
import com.sporekart.ai.capabilitydiscovery.interfaces.rest.dto.CapabilityHealthDto;
import com.sporekart.ai.capabilitydiscovery.interfaces.rest.dto.CapabilityRequestDto;
import com.sporekart.ai.capabilitydiscovery.interfaces.rest.dto.CapabilityResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/capability-discovery")
public class CapabilityDiscoveryController {

    private final CapabilityRegistryService registryService;
    private final CapabilityDiscoveryService discoveryService;
    private final CapabilityHealthService healthService;

    public CapabilityDiscoveryController(CapabilityRegistryService registryService,
                                         CapabilityDiscoveryService discoveryService,
                                         CapabilityHealthService healthService) {
        this.registryService = registryService;
        this.discoveryService = discoveryService;
        this.healthService = healthService;
    }

    @PostMapping("/register")
    public ResponseEntity<CapabilityResponseDto> register(
            @Valid @RequestBody CapabilityRequestDto request) {
        CapabilityEntry created = registryService.registerCapability(toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(CapabilityResponseDto.from(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CapabilityResponseDto> update(
            @PathVariable("id") String id,
            @Valid @RequestBody CapabilityRequestDto request) {
        CapabilityEntry updated = registryService.updateCapability(id, toDomain(request));
        return ResponseEntity.ok(CapabilityResponseDto.from(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CapabilityResponseDto> get(@PathVariable("id") String id) {
        return ResponseEntity.ok(CapabilityResponseDto.from(registryService.getCapability(id)));
    }

    @GetMapping("/list")
    public ResponseEntity<List<CapabilityResponseDto>> list() {
        return ResponseEntity.ok(registryService.listCapabilities().stream()
                .map(CapabilityResponseDto::from).collect(Collectors.toList()));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<CapabilityResponseDto>> getByType(
            @PathVariable("type") CapabilityType type) {
        return ResponseEntity.ok(registryService.searchByType(type).stream()
                .map(CapabilityResponseDto::from).collect(Collectors.toList()));
    }

    @GetMapping("/module/{module}")
    public ResponseEntity<List<CapabilityResponseDto>> getByModule(
            @PathVariable("module") String module) {
        return ResponseEntity.ok(registryService.searchByModule(module).stream()
                .map(CapabilityResponseDto::from).collect(Collectors.toList()));
    }

    @GetMapping("/feature/{feature}")
    public ResponseEntity<List<CapabilityResponseDto>> getByFeature(
            @PathVariable("feature") String feature) {
        return ResponseEntity.ok(registryService.searchByFeature(feature).stream()
                .map(CapabilityResponseDto::from).collect(Collectors.toList()));
    }

    @PostMapping("/discover")
    public ResponseEntity<CapabilityDiscoveryResponseDto> discover(
            @Valid @RequestBody CapabilityDiscoveryRequestDto request) {
        List<CapabilityEntry> matched = discoveryService.discoverByRequirement(request.getRequiredFeatures());
        return ResponseEntity.ok(CapabilityDiscoveryResponseDto.from(matched));
    }

    @GetMapping("/compatible")
    public ResponseEntity<List<CapabilityResponseDto>> compatibleProviders(
            @RequestParam("type") CapabilityType type) {
        return ResponseEntity.ok(discoveryService.findCompatibleProviders(type).stream()
                .map(CapabilityResponseDto::from).collect(Collectors.toList()));
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<CapabilityAvailability> getAvailability(@PathVariable("id") String id) {
        return ResponseEntity.ok(discoveryService.checkAvailability(id));
    }

    @PostMapping("/{id}/availability")
    public ResponseEntity<CapabilityHealthDto> setAvailability(
            @PathVariable("id") String id,
            @Valid @RequestBody CapabilityHealthDto request) {
        CapabilityHealthDto recorded = CapabilityHealthDto.from(
                healthService.recordAvailability(id, request.getAvailability()));
        return ResponseEntity.status(HttpStatus.CREATED).body(recorded);
    }

    @GetMapping("/{id}/health")
    public ResponseEntity<CapabilityHealthDto> getHealth(@PathVariable("id") String id) {
        CapabilityAvailability status = healthService.getCapabilityStatus(id);
        CapabilityHealthDto dto = new CapabilityHealthDto();
        dto.setCapabilityId(id);
        dto.setAvailability(status);
        dto.setCheckedAt(java.time.Instant.now());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/health-report")
    public ResponseEntity<List<CapabilityHealthDto>> healthReport() {
        List<CapabilityHealthDto> report = healthService.getCapabilityHealthReport().stream()
                .map(CapabilityHealthDto::from).collect(Collectors.toList());
        return ResponseEntity.ok(report);
    }

    private CapabilityEntry toDomain(CapabilityRequestDto request) {
        CapabilityEntry entry = new CapabilityEntry();
        entry.setCapabilityId(request.getCapabilityId());
        entry.setCapabilityName(request.getCapabilityName());
        entry.setCapabilityType(request.getCapabilityType());
        entry.setDescription(request.getDescription());
        entry.setModule(request.getModule());
        entry.setSupportedFeatures(request.getSupportedFeatures() == null
                ? new ArrayList<>() : new ArrayList<>(request.getSupportedFeatures()));
        entry.setDependencies(request.getDependencies() == null
                ? new ArrayList<>() : new ArrayList<>(request.getDependencies()));
        entry.setVersion(request.getVersion());
        entry.setProviderCompatibility(request.getProviderCompatibility() == null
                ? new ArrayList<>() : new ArrayList<>(request.getProviderCompatibility()));
        entry.setMetadata(request.getMetadata() == null
                ? new HashMap<>() : new HashMap<>(request.getMetadata()));
        entry.setFeatureFlag(request.getFeatureFlag());
        entry.setEnabled(request.isEnabled());
        return entry;
    }
}
