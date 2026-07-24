# Shared Platform

## Overview

The `shared-platform` module provides reusable enterprise-grade infrastructure shared by all SporeKart microservices.

## Module: `com.sporekart:shared-platform:0.1.0-SNAPSHOT`

### Packages

| Package | Purpose |
|---------|---------|
| `com.sporekart.platform.error` | Exception hierarchy, ProblemDetails, ErrorResponse, GlobalExceptionHandler |
| `com.sporekart.platform.api` | Standardized API response models (ApiResponse, ApiPageResponse) |
| `com.sporekart.platform.validation` | BusinessValidator, PermissionValidator, ValidationGroup |
| `com.sporekart.platform.logging` | CorrelationIdFilter, LoggingContext, PerformanceLogger, AuditLogger, SecurityLogger |
| `com.sporekart.platform.util` | IdGenerator, DateUtils, PaginationRequest/Response, Constants |
| `com.sporekart.platform.mapping` | DtoMapper interface |
| `com.sporekart.platform.security` | SharedSecurityConfig base class |
| `com.sporekart.platform.config` | PlatformConfig auto-configuration |

### Usage

Add dependency:
```xml
<dependency>
    <groupId>com.sporekart</groupId>
    <artifactId>shared-platform</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

Import in application class:
```java
@SpringBootApplication
@Import(PlatformConfig.class)
public class MyServiceApplication { ... }
```

### Architecture Benefits

1. **Eliminates duplication** — No more copy-pasted SecurityConfig, exception classes, response models
2. **Consistent error handling** — RFC 9457 ProblemDetails across all services
3. **Standardized APIs** — Unified ApiResponse/ApiPageResponse for all endpoints
4. **Centralized validation** — BusinessValidator replaces ad-hoc null/blank/range checks
5. **Observability** — CorrelationIdFilter, AuditLogger, PerformanceLogger built-in
6. **Developer productivity** — DtoMapper, IdGenerator, DateUtils remove boilerplate
7. **Forces good practices** — PermissionValidator, SecurityLogger encourage security-first coding
