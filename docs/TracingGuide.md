# SporeKart Distributed Tracing Guide

## Overview

Distributed tracing provides end-to-end visibility into request flows across all SporeKart services. Every request is assigned a Trace ID that is propagated across service boundaries, enabling correlation of metrics, logs, and spans.

## Architecture

### Components
- **Trace ID**: 16-character hex string identifying the entire request flow
- **Span ID**: 16-character hex string identifying a single operation
- **Parent Span ID**: Links child spans to their parent
- **Correlation ID**: UUID linking related business transactions
- **Request ID**: UUID for the initial HTTP request

### Propagation
1. **Incoming**: TraceFilter extracts headers from HTTP request
2. **ThreadLocal**: TraceContext stored in ThreadLocal for current thread
3. **MDC**: Trace fields copied to SLF4J MDC for logging correlation
4. **Outgoing**: TraceContext serialized to headers for downstream calls

## Header Protocol

### Request Headers (Incoming)
| Header | Required | Description |
|--------|----------|-------------|
| X-Trace-Id | No* | Trace identifier (generated if missing) |
| X-Span-Id | No | Current span identifier |
| X-Correlation-Id | No | Business correlation identifier |
| X-Request-Id | No | Request identifier |
| X-Parent-Span-Id | No | Parent span for chaining |
| X-Workspace-Id | No | Workspace/tenant |
| X-User-Id | No | Authenticated user |

*\*Generated if not provided*

### Response Headers (Outgoing)
| Header | Always | Description |
|--------|--------|-------------|
| X-Trace-Id | Yes | Trace for current request |
| X-Span-Id | Yes | Current span |
| X-Correlation-Id | Yes | Correlation identifier |
| X-Request-Id | Yes | Request identifier |

## Sampling

| Environment | Sampling Rate | Strategy |
|-------------|--------------|----------|
| Development | 100% | All requests |
| Staging | 50% | Head-based |
| Production | 10% | Head-based |
| High-trace endpoints | 100% | Always sample |

### Head-Based Sampling
Decision made at request entry:
```yaml
management.tracing.sampling.probability: 0.1
```

## Trace Coverage

### Instrumented Operations
- All HTTP requests via Gateway
- All REST API calls
- All database operations (JPA/Hibernate)
- All Redis cache operations
- All Kafka publish/subscribe
- All AI service invocations
- All async task executions

### Span Naming Convention
```
{service}.{operation}.{target}
```
Examples:
- `identity-service.auth.login`
- `order-service.db.order.find`
- `ai-service.provider.completion`
- `event-service.kafka.publish`

## OpenTelemetry Integration

### Collector Configuration
The OTel collector runs as a sidecar, receiving traces via OTLP and exporting to the backend.

### Exporters
| Backend | Protocol | Endpoint |
|---------|----------|----------|
| Jaeger | gRPC | jaeger:14250 |
| Tempo | OTLP | tempo:4317 |
| Logging | Console | stdout |

## Troubleshooting

### Missing Traces
1. Check sampling rate configuration
2. Verify TraceFilter is in the filter chain
3. Check TraceContext propagation in async code
4. Verify downstream service accepts trace headers

### Incomplete Traces
1. Check for async boundary issues
2. Verify ExecutorService wraps TraceContext
3. Check for missing header propagation in HTTP clients

### Correlation Issues
1. Verify correlationId matches across services
2. Check timestamp synchronization
3. Verify span parent/child relationships

## Best Practices

1. **Always propagate**: Pass trace headers to all downstream calls
2. **Async support**: Use wrapped executors for async operations
3. **Meaningful span names**: Use consistent naming conventions
4. **Add baggage**: Include business context in trace baggage
5. **Log correlation**: Include traceId in all log entries
6. **Sampling**: Adjust sampling rate based on traffic patterns
7. **Monitoring**: Monitor trace ingestion rate and drop rate
