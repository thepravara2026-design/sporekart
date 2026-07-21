# Configuration Matrix

## Platform Configuration

| Key | Type | Default | Local | Dev | Stage | Prod | Docker | Cloud |
|-----|------|---------|-------|-----|-------|------|--------|-------|
| sporekart.ai.enabled | boolean | true | true | true | true | true | true | true |
| sporekart.ai.default-provider | string | MOCK | MOCK | openai | openai | openai | openai | openai |
| sporekart.ai.max-retries | int | 3 | 3 | 3 | 3 | 3 | 3 | 3 |
| sporekart.ai.request-timeout | duration | 30s | 30s | 30s | 30s | 30s | 30s | 30s |

## Gateway Configuration

| Key | Type | Default | Local | Dev | Stage | Prod | Docker | Cloud |
|-----|------|---------|-------|-----|-------|------|--------|-------|
| sporekart.ai.gateway.enabled | boolean | true | true | true | true | true | true | true |
| sporekart.ai.gateway.rate-limit-requests-per-minute | int | 100 | 100 | 100 | 100 | 100 | 100 | 100 |
| sporekart.ai.gateway.circuit-breaker-enabled | boolean | true | true | true | true | true | true | true |

## Runtime Configuration

| Key | Type | Default | Local | Dev | Stage | Prod | Docker | Cloud |
|-----|------|---------|-------|-----|-------|------|--------|-------|
| sporekart.ai.runtime.enabled | boolean | true | true | true | true | true | true | true |
| sporekart.ai.runtime.hot-reload-enabled | boolean | true | false | true | true | true | true | true |
| sporekart.ai.runtime.maintenance-mode | boolean | false | false | false | false | false | false | false |
| sporekart.ai.runtime.max-agent-executions | int | 1000 | 1000 | 1000 | 1000 | 1000 | 1000 | 1000 |

## Security Configuration

| Key | Type | Default | Local | Dev | Stage | Prod | Docker | Cloud |
|-----|------|---------|-------|-----|-------|------|--------|-------|
| sporekart.ai.security.rate-limiter-enabled | boolean | true | false | true | true | true | true | true |
| sporekart.ai.security.content-moderation-enabled | boolean | true | false | false | true | true | true | true |
| sporekart.ai.security.csrf-enabled | boolean | true | false | true | true | true | true | true |

## Cache Configuration

| Key | Type | Default | Local | Dev | Stage | Prod | Docker | Cloud |
|-----|------|---------|-------|-----|-------|------|--------|-------|
| sporekart.ai.cache.enabled | boolean | true | true | true | true | true | true | true |
| sporekart.ai.cache.default-ttl | duration | 30m | 30m | 10m | 5m | 2m | 5m | 2m |
| sporekart.ai.cache.refresh-strategy | string | CACHE_ASIDE | CACHE_ASIDE | CACHE_ASIDE | CACHE_ASIDE | REFRESH_AHEAD | CACHE_ASIDE | REFRESH_AHEAD |

## Observability Configuration

| Key | Type | Default | Local | Dev | Stage | Prod | Docker | Cloud |
|-----|------|---------|-------|-----|-------|------|--------|-------|
| sporekart.ai.observability.logging-level | string | INFO | DEBUG | DEBUG | DEBUG | INFO | INFO | INFO |
| sporekart.ai.observability.sentry-enabled | boolean | true | false | false | true | true | false | true |
| sporekart.ai.observability.prometheus-enabled | boolean | true | false | true | true | true | true | true |
