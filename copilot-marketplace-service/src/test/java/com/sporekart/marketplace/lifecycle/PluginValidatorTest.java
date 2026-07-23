package com.sporekart.marketplace.lifecycle;

import com.sporekart.marketplace.sdk.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PluginValidatorTest {

    @Mock
    private VersionManager versionManager;

    private PluginValidator pluginValidator;

    @BeforeEach
    void setUp() {
        pluginValidator = new PluginValidator(versionManager);
    }

    @Test
    void validate_shouldReturnNoErrorsForValidManifest() {
        when(versionManager.isCompatible(anyString(), anyString())).thenReturn(true);

        var manifest = createValidManifest();
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.isEmpty());
    }

    @Test
    void validate_shouldReturnErrorWhenPluginIdIsNull() {
        var manifest = createValidManifest("pluginId", null);
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.contains("pluginId is required"));
    }

    @Test
    void validate_shouldReturnErrorWhenPluginIdIsBlank() {
        var manifest = createValidManifest("pluginId", "  ");
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.contains("pluginId is required"));
    }

    @Test
    void validate_shouldReturnErrorWhenNameIsNull() {
        var manifest = createValidManifest("name", null);
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.contains("name is required"));
    }

    @Test
    void validate_shouldReturnErrorWhenNameIsBlank() {
        var manifest = createValidManifest("name", "");
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.contains("name is required"));
    }

    @Test
    void validate_shouldReturnErrorWhenVersionIsNull() {
        var manifest = createValidManifest("version", null);
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.contains("version is required"));
    }

    @Test
    void validate_shouldReturnErrorWhenVersionIsBlank() {
        var manifest = createValidManifest("version", "  ");
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.contains("version is required"));
    }

    @Test
    void validate_shouldReturnErrorWhenTypeIsNull() {
        var manifest = createValidManifest("type", null);
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.contains("type is required"));
    }

    @Test
    void validate_shouldReturnErrorWhenAuthorIsNull() {
        var manifest = createValidManifest("author", null);
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.contains("author is required"));
    }

    @Test
    void validate_shouldReturnErrorWhenAuthorIsBlank() {
        var manifest = createValidManifest("author", "");
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.contains("author is required"));
    }

    @Test
    void validate_shouldReturnErrorWhenEntryPointIsNull() {
        var manifest = createValidManifest("entryPoint", null);
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.contains("entryPoint is required"));
    }

    @Test
    void validate_shouldReturnErrorWhenEntryPointIsBlank() {
        var manifest = createValidManifest("entryPoint", "");
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.contains("entryPoint is required"));
    }

    @Test
    void validate_shouldReturnErrorWhenCapabilitiesIsNull() {
        var manifest = createValidManifest("capabilities", null);
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.contains("at least one capability is required"));
    }

    @Test
    void validate_shouldReturnErrorWhenCapabilitiesIsEmpty() {
        var manifest = createValidManifest("capabilities", List.of());
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.contains("at least one capability is required"));
    }

    @Test
    void validate_shouldReturnErrorWhenRequiredPermissionsIsNull() {
        var manifest = createValidManifest("requiredPermissions", null);
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.contains("requiredPermissions must not be null"));
    }

    @Test
    void validate_shouldReturnErrorWhenPlatformVersionIncompatible() {
        when(versionManager.isCompatible("2.0.0", "3.0.0")).thenReturn(false);

        var manifest = new PluginManifest(
            "valid-id", "Valid Plugin", "1.0.0", "author", "desc",
            PluginType.AI_COPILOT, List.of(PluginCapability.CONVERSATION),
            List.of(PluginPermission.READ_PRODUCTS), List.of(), "2.0.0", "3.0.0",
            "/health", Map.of(), "exec.Main"
        );
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.stream().anyMatch(e -> e.contains("platform version incompatibility")));
    }

    @Test
    void validate_shouldNotCheckPlatformCompatibilityWhenMinPlatformVersionIsNull() {
        var manifest = new PluginManifest(
            "valid-id", "Valid Plugin", "1.0.0", "author", "desc",
            PluginType.AI_COPILOT, List.of(PluginCapability.CONVERSATION),
            List.of(PluginPermission.READ_PRODUCTS), List.of(), null, "3.0.0",
            "/health", Map.of(), "exec.Main"
        );
        var errors = pluginValidator.validate(manifest);

        assertFalse(errors.stream().anyMatch(e -> e.contains("platform version incompatibility")));
    }

    @Test
    void validate_shouldNotCheckPlatformCompatibilityWhenMinPlatformVersionIsBlank() {
        var manifest = new PluginManifest(
            "valid-id", "Valid Plugin", "1.0.0", "author", "desc",
            PluginType.AI_COPILOT, List.of(PluginCapability.CONVERSATION),
            List.of(PluginPermission.READ_PRODUCTS), List.of(), "", "3.0.0",
            "/health", Map.of(), "exec.Main"
        );
        var errors = pluginValidator.validate(manifest);

        assertFalse(errors.stream().anyMatch(e -> e.contains("platform version incompatibility")));
    }

    @Test
    void validate_shouldReturnMultipleErrorsSimultaneously() {
        var manifest = new PluginManifest(
            null, null, null, null, "desc",
            null, null, null, List.of(), null, null,
            "/health", Map.of(), null
        );
        var errors = pluginValidator.validate(manifest);

        assertTrue(errors.contains("pluginId is required"));
        assertTrue(errors.contains("name is required"));
        assertTrue(errors.contains("version is required"));
        assertTrue(errors.contains("type is required"));
        assertTrue(errors.contains("author is required"));
        assertTrue(errors.contains("entryPoint is required"));
        assertTrue(errors.contains("at least one capability is required"));
        assertTrue(errors.contains("requiredPermissions must not be null"));
    }

    @Test
    void validate_shouldNotReturnPlatformErrorWhenMinVersionNullButIncompatible() {
        var manifest = new PluginManifest(
            "valid-id", "Valid Plugin", "1.0.0", "author", "desc",
            PluginType.AI_COPILOT, List.of(PluginCapability.CONVERSATION),
            List.of(PluginPermission.READ_PRODUCTS), List.of(), null, null,
            "/health", Map.of(), "exec.Main"
        );
        var errors = pluginValidator.validate(manifest);

        assertFalse(errors.stream().anyMatch(e -> e.contains("platform version incompatibility")));
    }

    private PluginManifest createValidManifest() {
        return new PluginManifest(
            "valid-id", "Valid Plugin", "1.0.0", "author", "desc",
            PluginType.AI_COPILOT, List.of(PluginCapability.CONVERSATION),
            List.of(PluginPermission.READ_PRODUCTS), List.of(), "1.0.0", "2.0.0",
            "/health", Map.of(), "exec.Main"
        );
    }

    private PluginManifest createValidManifest(String fieldToNull, Object value) {
        var base = createValidManifest();
        return new PluginManifest(
            "pluginId".equals(fieldToNull) ? (String) value : base.pluginId(),
            "name".equals(fieldToNull) ? (String) value : base.name(),
            "version".equals(fieldToNull) ? (String) value : base.version(),
            "author".equals(fieldToNull) ? (String) value : base.author(),
            base.description(),
            "type".equals(fieldToNull) ? (PluginType) value : base.type(),
            "capabilities".equals(fieldToNull) ? (List<PluginCapability>) value : base.capabilities(),
            "requiredPermissions".equals(fieldToNull) ? (List<PluginPermission>) value : base.requiredPermissions(),
            base.dependencies(),
            base.minPlatformVersion(),
            base.maxPlatformVersion(),
            base.healthEndpoint(),
            base.configurationSchema(),
            "entryPoint".equals(fieldToNull) ? (String) value : base.entryPoint()
        );
    }
}
