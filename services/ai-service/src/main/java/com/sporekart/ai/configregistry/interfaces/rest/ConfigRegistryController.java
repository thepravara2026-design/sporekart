package com.sporekart.ai.configregistry.interfaces.rest;

import com.sporekart.ai.configregistry.api.ConfigRegistryService;
import com.sporekart.ai.configregistry.api.ConfigSnapshotService;
import com.sporekart.ai.configregistry.api.ConfigValidationService;
import com.sporekart.ai.configregistry.domain.ConfigurationEntry;
import com.sporekart.ai.configregistry.domain.ConfigSnapshot;
import com.sporekart.ai.configregistry.domain.ConfigType;
import com.sporekart.ai.configregistry.domain.ConfigValidationResult;
import com.sporekart.ai.configregistry.interfaces.rest.dto.ConfigRequestDto;
import com.sporekart.ai.configregistry.interfaces.rest.dto.ConfigResponseDto;
import com.sporekart.ai.configregistry.interfaces.rest.dto.ConfigSnapshotRequestDto;
import com.sporekart.ai.configregistry.interfaces.rest.dto.ConfigSnapshotResponseDto;
import com.sporekart.ai.configregistry.interfaces.rest.dto.ConfigValidationResultDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/config-registry")
public class ConfigRegistryController {

    private final ConfigRegistryService configRegistryService;
    private final ConfigSnapshotService configSnapshotService;
    private final ConfigValidationService configValidationService;

    public ConfigRegistryController(ConfigRegistryService configRegistryService,
            ConfigSnapshotService configSnapshotService,
            ConfigValidationService configValidationService) {
        this.configRegistryService = configRegistryService;
        this.configSnapshotService = configSnapshotService;
        this.configValidationService = configValidationService;
    }

    @GetMapping("/{key}")
    public ResponseEntity<ConfigResponseDto> getConfig(@PathVariable String key) {
        ConfigurationEntry entry = configRegistryService.getConfig(key);
        if (entry == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toResponse(entry));
    }

    @PutMapping("/set")
    public ResponseEntity<ConfigResponseDto> setConfig(@RequestBody ConfigRequestDto request) {
        ConfigurationEntry entry = new ConfigurationEntry();
        entry.setConfigKey(request.getConfigKey());
        entry.setConfigValue(request.getConfigValue());
        entry.setConfigType(request.getConfigType());
        entry.setDescription(request.getDescription());
        entry.setModule(request.getModule());
        entry.setEnvironment(request.getEnvironment());
        entry.setMetadata(request.getMetadata());
        entry.setValid(request.isValid());
        entry.setCreatedBy(request.getCreatedBy());
        configRegistryService.setConfig(entry);
        return ResponseEntity.ok(toResponse(configRegistryService.getConfig(request.getConfigKey())));
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> deleteConfig(@PathVariable String key) {
        configRegistryService.deleteConfig(key);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ConfigResponseDto>> listByType(@PathVariable String type) {
        ConfigType configType = ConfigType.valueOf(type.toUpperCase());
        List<ConfigResponseDto> result = new ArrayList<>();
        for (ConfigurationEntry entry : configRegistryService.listByType(configType)) {
            result.add(toResponse(entry));
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/module/{module}")
    public ResponseEntity<List<ConfigResponseDto>> listByModule(@PathVariable String module) {
        List<ConfigResponseDto> result = new ArrayList<>();
        for (ConfigurationEntry entry : configRegistryService.listByModule(module)) {
            result.add(toResponse(entry));
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ConfigResponseDto>> searchConfig(@RequestParam("q") String query) {
        List<ConfigResponseDto> result = new ArrayList<>();
        for (ConfigurationEntry entry : configRegistryService.searchConfig(query)) {
            result.add(toResponse(entry));
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/snapshot")
    public ResponseEntity<ConfigSnapshotResponseDto> createSnapshot(@RequestBody ConfigSnapshotRequestDto request) {
        ConfigSnapshot snapshot = configSnapshotService.createSnapshot(
                request.getName(), request.getDescription(), request.getCreatedBy());
        return ResponseEntity.status(HttpStatus.CREATED).body(toSnapshotResponse(snapshot));
    }

    @GetMapping("/snapshots")
    public ResponseEntity<List<ConfigSnapshotResponseDto>> listSnapshots() {
        List<ConfigSnapshotResponseDto> result = new ArrayList<>();
        for (ConfigSnapshot snapshot : configSnapshotService.listSnapshots()) {
            result.add(toSnapshotResponse(snapshot));
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/rollback/{snapshotId}")
    public ResponseEntity<Void> rollbackToSnapshot(@PathVariable String snapshotId) {
        configSnapshotService.rollbackToSnapshot(snapshotId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/compare")
    public ResponseEntity<Map<String, String>> compareSnapshots(
            @RequestParam("a") String a, @RequestParam("b") String b) {
        return ResponseEntity.ok(configSnapshotService.compareSnapshots(a, b));
    }

    @PostMapping("/validate")
    public ResponseEntity<ConfigValidationResultDto> validate(@RequestBody ConfigRequestDto request) {
        ConfigurationEntry entry = new ConfigurationEntry();
        entry.setConfigKey(request.getConfigKey());
        entry.setConfigValue(request.getConfigValue());
        entry.setConfigType(request.getConfigType());
        entry.setDescription(request.getDescription());
        entry.setModule(request.getModule());
        entry.setEnvironment(request.getEnvironment());
        entry.setMetadata(request.getMetadata());
        entry.setValid(request.isValid());
        entry.setCreatedBy(request.getCreatedBy());
        ConfigValidationResult result = configValidationService.validateConfig(entry);
        ConfigValidationResultDto dto = new ConfigValidationResultDto();
        dto.setValid(result.valid());
        dto.setErrors(result.errors());
        dto.setWarnings(result.warnings());
        dto.setValidatedAt(result.validatedAt());
        return ResponseEntity.ok(dto);
    }

    private ConfigResponseDto toResponse(ConfigurationEntry entry) {
        ConfigResponseDto dto = new ConfigResponseDto();
        dto.setConfigId(entry.getConfigId());
        dto.setConfigKey(entry.getConfigKey());
        dto.setConfigValue(entry.getConfigValue());
        dto.setConfigType(entry.getConfigType());
        dto.setDescription(entry.getDescription());
        dto.setVersion(entry.getVersion());
        dto.setModule(entry.getModule());
        dto.setEnvironment(entry.getEnvironment());
        dto.setMetadata(entry.getMetadata());
        dto.setSnapshotId(entry.getSnapshotId());
        dto.setValid(entry.isValid());
        dto.setCreatedAt(entry.getCreatedAt());
        dto.setUpdatedAt(entry.getUpdatedAt());
        dto.setCreatedBy(entry.getCreatedBy());
        return dto;
    }

    private ConfigSnapshotResponseDto toSnapshotResponse(ConfigSnapshot snapshot) {
        ConfigSnapshotResponseDto dto = new ConfigSnapshotResponseDto();
        dto.setSnapshotId(snapshot.getSnapshotId());
        dto.setName(snapshot.getName());
        dto.setDescription(snapshot.getDescription());
        dto.setConfigurations(snapshot.getConfigurations());
        dto.setVersion(snapshot.getVersion());
        dto.setCreatedAt(snapshot.getCreatedAt());
        dto.setCreatedBy(snapshot.getCreatedBy());
        return dto;
    }
}
