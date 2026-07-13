package com.sporekart.ai.admin.application;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ConfigurationValidationServiceImplTest {

    private final ConfigurationValidationServiceImpl service = new ConfigurationValidationServiceImpl();

    @Test
    void testValidateConfiguration_ValidJson() {
        assertTrue(service.validateConfiguration("key", "{\"enabled\": true}", "module"));
    }

    @Test
    void testValidateConfiguration_ValidStringValue() {
        assertTrue(service.validateConfiguration("key", "\"simple-string\"", "module"));
    }

    @Test
    void testValidateConfiguration_ValidNumberValue() {
        assertTrue(service.validateConfiguration("key", "42", "module"));
    }

    @Test
    void testValidateConfiguration_NullKey() {
        assertFalse(service.validateConfiguration(null, "value", "module"));
    }

    @Test
    void testValidateConfiguration_EmptyKey() {
        assertFalse(service.validateConfiguration("", "value", "module"));
    }

    @Test
    void testValidateConfiguration_BlankKey() {
        assertFalse(service.validateConfiguration("   ", "value", "module"));
    }

    @Test
    void testValidateConfiguration_NullValue() {
        assertFalse(service.validateConfiguration("key", null, "module"));
    }

    @Test
    void testValidateConfiguration_EmptyValue() {
        assertFalse(service.validateConfiguration("key", "", "module"));
    }

    @Test
    void testValidateConfiguration_InvalidJson() {
        assertFalse(service.validateConfiguration("key", "{invalid}", "module"));
    }

    @Test
    void testValidateAll_ValidModule() {
        var errors = service.validateAll("valid-module");
        assertTrue(errors.isEmpty());
    }

    @Test
    void testValidateAll_NullModule() {
        var errors = service.validateAll(null);
        assertEquals(1, errors.size());
        assertEquals("Module must not be empty", errors.get(0));
    }

    @Test
    void testValidateImport_AllValid() {
        var config = Map.of("key1", "{\"val\": 1}", "key2", "\"string\"");
        var result = service.validateImport(config);
        assertEquals(2, result.size());
        assertTrue(result.get("key1"));
        assertTrue(result.get("key2"));
    }

    @Test
    void testValidateImport_WithInvalid() {
        var config = Map.of("valid-key", "\"ok\"", "", "{invalid}", "key3", "");
        var result = service.validateImport(config);
        assertTrue(result.get("valid-key"));
        assertFalse(result.get(""));
        assertFalse(result.get("key3"));
    }

    @Test
    void testValidateImport_Null() {
        var result = service.validateImport(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testValidateEnvironment_Valid() {
        assertTrue(service.validateEnvironment("production"));
    }

    @Test
    void testValidateEnvironment_Null() {
        assertFalse(service.validateEnvironment(null));
    }

    @Test
    void testValidateEnvironment_Empty() {
        assertFalse(service.validateEnvironment(""));
    }
}
