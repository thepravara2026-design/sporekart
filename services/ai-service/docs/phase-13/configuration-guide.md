# Configuration Guide

## How to Add a New Configuration

1. Define domain record in `configuration/domain/`
2. Add API interface if new resolution logic needed
3. Add `@ConfigurationProperties` in `configuration/config/`
4. Register in `ConfigurationPropertiesConfig.java`
5. Add YAML keys to `application.yml` and all profile YAMLs
6. Add validation rules in `configuration/validation/ConfigValidator.java`
7. Document in `configuration-platform.md`
8. Add architecture test for the new configuration class

## How to Access Configuration

```java
// Via @ConfigurationProperties (preferred)
@ConfigurationProperties(prefix = "sporekart.ai.runtime")
public record RuntimeConfigurationProperties(RuntimeConfiguration runtime) {}

// Via the ConfigurationLoader interface
@Autowired
private ConfigurationLoader configurationLoader;

ConfigKey key = ConfigKey.of("sporekart.ai.runtime.enabled", Boolean.class)
    .withDescription("Runtime enabled");
ConfigValue<Boolean> value = configurationLoader.loadOrDefault(key, true);
boolean enabled = value.value();
```

## Configuration Override Order

1. User/tenant overrides (highest priority)
2. Runtime hot-reload
3. Secret providers
4. Environment variables (`AI_*`)
5. Profile-specific YAML
6. Base `application.yml` (lowest priority)

## Profile Selection

```bash
# Local development
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Development server
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Production
java -jar ai-service.jar --spring.profiles.active=prod

# Docker
SPRING_PROFILES_ACTIVE=docker docker compose up

# Cloud
SPRING_PROFILES_ACTIVE=cloud java -jar ai-service.jar
```
