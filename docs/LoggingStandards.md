# SporeKart Logging Standards

## Overview

This document defines mandatory logging standards for all SporeKart services. Every service must implement structured JSON logging with consistent fields, levels, and formats.

## Mandatory Format

All logs must be emitted as JSON using the Logstash encoder.

### Required Fields
| Field | Type | Required | Example |
|-------|------|----------|---------|
| @timestamp | ISO 8601 | Yes | 2026-07-24T12:00:00.000Z |
| level | string | Yes | INFO |
| logger | string | Yes | com.sporekart.identity.AuthService |
| message | string | Yes | User login successful |
| thread | string | Yes | http-nio-8080-exec-5 |
| service | string | Yes | identity-service |
| traceId | string | Yes | a1b2c3d4e5f6g7h8 |
| spanId | string | Yes | h8g7f6e5d4c3b2a1 |
| correlationId | string | Yes | 550e8400-e29b-41d4-a716-446655440000 |
| requestId | string | Yes | 660e8400-e29b-41d4-a716-446655440001 |
| environment | string | Yes | production |

## Log Level Guidelines

| Level | Usage | Production |
|-------|-------|------------|
| TRACE | Method entry/exit, variable dumps | Disabled |
| DEBUG | Development debugging | Disabled |
| INFO | Service lifecycle, request start/complete, state changes | Enabled |
| WARN | Slow queries, retries, deprecated API, rate limit approaching | Enabled |
| ERROR | Exceptions, service degradation, data issues, failed operations | Enabled |
| FATAL | Unrecoverable errors, process will terminate | Enabled |

## Message Conventions

### Format
```
<Action> <Subject> [<Detail>] [<Key>=<Value>...]
```

### Examples
- `User login successful userId=usr_123`
- `Order created orderId=ord_456 amount=99.99 currency=USD`
- `Payment failed orderId=ord_456 reason=insufficient_funds`
- `AI completion timeout provider=openai duration=6500ms`

## MDC Fields

The following MDC fields must be populated for every request:

| Field | Source | Populated By |
|-------|--------|-------------|
| traceId | X-Trace-Id header | TraceFilter |
| spanId | Generated | TraceFilter |
| correlationId | X-Correlation-Id header | TraceFilter |
| requestId | Generated | LoggingFilter |
| userId | JWT token | SecurityContext |
| workspaceId | X-Workspace-Id header | TraceFilter |
| method | HttpServletRequest | LoggingFilter |
| path | HttpServletRequest | LoggingFilter |
| status | HttpServletResponse | LoggingFilter |
| duration | Calculated | LoggingFilter |
| remoteAddr | HttpServletRequest | LoggingFilter |

## Sensitive Data Protection

### Never Log
- Passwords, tokens, secrets
- Credit card numbers, SSN
- Full database queries (parameter values)
- Internal IP addresses
- Personal health information

### Redaction
Sensitive fields are automatically masked: `***MASKED***`

## Environment Configuration

### Development
```xml
<root level="DEBUG"/>
<logger name="com.sporekart" level="DEBUG"/>
```

### Staging
```xml
<root level="INFO"/>
<logger name="com.sporekart" level="DEBUG"/>
<logger name="org.springframework" level="WARN"/>
```

### Production
```xml
<root level="WARN"/>
<logger name="com.sporekart" level="INFO"/>
<logger name="org.springframework" level="WARN"/>
<logger name="io.micrometer" level="INFO"/>
```

## Audit Logging

Security-relevant events must be logged at INFO level:
- Authentication success/failure
- Authorization decisions
- Secret access
- Configuration changes
- User role/permission changes

## Log Retention

| Environment | Retention | Max Size |
|-------------|-----------|----------|
| Development | 7 days | 1 GB |
| Staging | 14 days | 5 GB |
| Production | 30 days | 10 GB |
| Compliance | 1 year | Archive |

## Compliance

All logs must comply with:
- GDPR (no PII in logs without purpose)
- SOC2 (audit trail integrity)
- PCI DSS (no cardholder data)
- HIPAA (no health information)
