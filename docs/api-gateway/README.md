# Enterprise API Gateway

## Overview

The Enterprise API Gateway is the single entry point for all frontend, mobile, AI, and external clients. Built on Spring Cloud Gateway (Reactive WebFlux), it provides authentication, authorization, rate limiting, routing, circuit breaking, observability, and security headers.

## Architecture

```
Client → CorrelationIdFilter → LoggingFilter → AuthenticationFilter → AuthorizationFilter → ValidationFilter → MetricsFilter → Route → Downstream Service
                                                                    ↓
                                                            SecurityHeaderFilter (response)
```

### Filter Pipeline Order

| Order | Filter                    | Responsibility                                    |
|-------|---------------------------|---------------------------------------------------|
| MIN   | CorrelationIdFilter       | Generates/injects X-Correlation-Id and X-Request-Id |
| MIN+1 | LoggingFilter             | Logs request/response with timing                 |
| MIN+2 | AuthenticationFilter      | Validates JWT, extracts user identity             |
| MIN+3 | AuthorizationFilter       | Checks role-based access to protected paths       |
| MIN+4 | ValidationFilter          | Validates HTTP method, content length             |
| MIN+5 | TracingFilter             | Injects trace ID from Micrometer Tracing          |
| MAX-1 | MetricsFilter             | Records request count, duration, errors; applies security headers |

## Components

### Config (`com.sporekart.gateway.config`)

| Class                  | Purpose                                              |
|------------------------|------------------------------------------------------|
| `GatewayConfig`        | `@ConfigurationProperties(prefix = "sporekart.gateway")` |
| `RouteConfig`          | Registers routes from `ServiceRegistry` with circuit breakers and retry |
| `SecurityConfig`       | Spring Security WebFlux: disables CSRF, configures public paths |
| `CorsConfig`           | CORS configuration (allowed origins, methods, headers) |
| `RateLimiterConfig`    | Redis-based rate limiter (conditional on property)   |
| `FallbackConfig`       | Fallback endpoints for circuit breaker               |
| `OpenApiConfig`        | SpringDoc OpenAPI / Swagger UI configuration         |

### Security (`com.sporekart.gateway.security`)

| Class                  | Purpose                                              |
|------------------------|------------------------------------------------------|
| `JwtValidator`         | Lightweight JWT claim extraction (subject, roles)    |
| `SecurityHeaderFilter` | Appends security headers to responses (HSTS, CSP, XSS, frame options) |

### Filters (`com.sporekart.gateway.filter`)

| Class                  | Purpose                                              |
|------------------------|------------------------------------------------------|
| `CorrelationIdFilter`  | Tracing: correlation ID and request ID headers + MDC |
| `LoggingFilter`        | Request/response logging with duration               |
| `AuthenticationFilter` | JWT bearer token validation, public path whitelist   |
| `AuthorizationFilter`  | Role-based path access control                       |
| `ValidationFilter`     | HTTP method and content-length validation            |

### Registry (`com.sporekart.gateway.registry`)

| Class               | Purpose                                             |
|---------------------|-----------------------------------------------------|
| `ServiceRegistry`   | YAML-configured service catalog (ConcurrentHashMap-based) |

### Proxy (`com.sporekart.gateway.proxy`)

| Class           | Purpose                                                |
|-----------------|--------------------------------------------------------|
| `ServiceProxy`  | URI resolution and service name extraction from paths  |

### Validation (`com.sporekart.gateway.validation`)

| Class              | Purpose                                              |
|--------------------|------------------------------------------------------|
| `ConfigValidator`  | Startup validation: checks service URLs, timeouts, duplicates |
| `ValidationResult` | Record with errors/warnings list                     |

### Error Handling (`com.sporekart.gateway.error`)

| Class                     | Purpose                                              |
|---------------------------|------------------------------------------------------|
| `GatewayException`        | Domain exception with `HttpStatus` and `errorCode`   |
| `ProblemDetails`          | RFC 9457 problem detail response body                |
| `GatewayExceptionHandler` | Global reactive error handler (`ErrorWebExceptionHandler`) |

### Health (`com.sporekart.gateway.health`)

| Class                     | Purpose                                              |
|---------------------------|------------------------------------------------------|
| `AggregatedHealthIndicator` | Probes all downstream service health endpoints, aggregates status |

### Observability (`com.sporekart.gateway.observability`)

| Class              | Purpose                                              |
|--------------------|------------------------------------------------------|
| `MetricsRecorder`  | Micrometer counters, timers, gauges for requests, errors, connections |
| `MetricsFilter`    | Global filter that records metrics and applies security headers |
| `TracingFilter`    | Injects trace ID from Micrometer Tracing             |

### Bootstrap (`com.sporekart.gateway.bootstrap`)

| Class                 | Purpose                                              |
|-----------------------|------------------------------------------------------|
| `GatewayBootstrapper` | `@PostConstruct` lifecycle: validates config, logs stages, fail-fast on errors |

## Configuration

### `application.yml` Structure

```yaml
sporekart:
  gateway:
    cors:
      allowed-origins: "*"
      allowed-methods: GET,POST,PUT,DELETE,PATCH,OPTIONS
      allowed-headers: "*"
      max-age: 3600
    security:
      jwt-enabled: true
      require-authentication: true
      public-paths:
        - /actuator/health/**
        - /actuator/info
        - /api/public/**
        - /webjars/**
        - /v3/api-docs/**
        - /swagger-ui/**
    rate-limiter:
      enabled: true
      default-permit-per-second: 100
      default-burst-capacity: 200
    routing:
      service-timeout: 30s
      max-header-size: 8192
      max-request-size: 10MB
    security-headers:
      hsts-enabled: true
      hsts-max-age: 31536000
      csp-enabled: true
      csp-policy: "default-src 'self'"
      xss-protection: true
      content-type-options: true
      frame-options: DENY
    observability:
      metrics-enabled: true
      tracing-enabled: true
      audit-enabled: true
    services:
      identity:
        url: http://localhost:8081
        health-path: /actuator/health
        timeout: 5s
      catalog:
        url: http://localhost:8082
        ...
```

## Registered Routes

| Service       | Route Prefix     |
|---------------|------------------|
| identity      | `/api/auth`      |
| catalog       | `/api/catalog`   |
| order         | `/api/orders`    |
| cart          | `/api/cart`      |
| training      | `/api/training`  |
| admin         | `/api/admin`     |
| analytics     | `/api/analytics` |
| ai            | `/api/ai`        |
| memory        | `/api/memory`    |
| notification  | `/api/notifications` |
| payment       | `/api/payments`  |
| inventory     | `/api/inventory` |
| fulfillment   | `/api/fulfillment` |
| support       | `/api/support`   |
| risk          | `/api/risk`      |
| content       | `/api/content`   |
| search        | `/api/search`    |

## Error Responses

All errors follow RFC 9457 Problem Details format:

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "The requested service was not found",
  "instance": "/api/nonexistent",
  "timestamp": "2026-07-22T08:00:00Z",
  "errorCode": "SERVICE_NOT_FOUND"
}
```

## Endpoints

| Path               | Method | Auth Required | Description                    |
|--------------------|--------|---------------|--------------------------------|
| `/actuator/health` | GET    | No            | Health check (aggregated)      |
| `/actuator/info`   | GET    | No            | Application info               |
| `/actuator/metrics`| GET    | No            | Micrometer metrics             |
| `/v3/api-docs`     | GET    | No            | OpenAPI spec                   |
| `/swagger-ui/**`   | GET    | No            | Swagger UI                     |
| `/api/{service}/**`| *      | Yes (JWT)     | Proxied downstream services    |

## Metrics

| Metric                              | Type    | Tags                                         |
|-------------------------------------|---------|----------------------------------------------|
| `gateway.requests.total`            | Counter | method, path, route, status, status_group    |
| `gateway.requests.status`           | Counter | status                                       |
| `gateway.request.duration`          | Timer   | method, path, route, status, status_group    |
| `gateway.errors.total`              | Counter | error_code                                   |
| `gateway.service.call.duration`     | Timer   | service, success                             |
| `gateway.ratelimit.hits`            | Counter | route                                        |
| `gateway.connections.active`        | Gauge   | route                                        |

## Build & Run

```bash
# Build
mvn clean compile -f services/gateway-service/pom.xml

# Test
mvn clean test -f services/gateway-service/pom.xml

# Package
mvn clean package -f services/gateway-service/pom.xml

# Run
java -jar services/gateway-service/target/gateway-service-0.1.0-SNAPSHOT.jar

# Docker
docker build -t sporekart/gateway-service:latest -f services/gateway-service/Dockerfile services/gateway-service
docker run -p 8080:8080 sporekart/gateway-service:latest
```
