# Redis Review — SporeKart Enterprise AI Platform

**Gate Step:** 5 — Cache & State Strategy
**Document type:** Architecture Review Board Certification Report (non-blocking findings)
**Scope:** Redis usage, cache key namespacing, TTL strategy, invalidation, recovery
**Status:** PASS WITH RECOMMENDATIONS

---

## 1. Executive Summary

Redis is used pervasively across the SporeKart platform as a distributed cache. The
ai-service implements a mature, programmatic caching layer: each domain module owns a
dedicated `*RedisCacheService` backed by Spring `StringRedisTemplate`, with explicit
per-cache TTL constants and namespaced key prefixes. Other services
(content, inventory, cart, catalog, identity, risk, search, support, analytics, admin)
declare only connection settings (`host`/`port`, optionally `password`) in
`application.yml` and rely on Redis for their own purposes (sessions, tokens, etc).

Caching in the ai-service is **programmatic** (manual `opsForValue().set(key, json, ttl)`
calls), not annotation-driven (`@Cacheable`/`@CacheEvict` are not used in the scanned
`main` sources). This gives fine-grained control over TTLs and invalidation but means
cache behavior is decentralized across ~20 service classes.

The strategy is coherent and production-plausible. The principal gaps are the absence of
a centralized cache configuration (most services only set host/port), no documented
cache warm-up, and no explicit cache-consistency or Redis-restart recovery policy.

---

## 2. Redis Connection Configuration (per module)

From `application.yml` of each service:

| Service | host | port | password | Notes |
|---------|------|------|----------|-------|
| ai-service | localhost | 6379 | (none) | Extensive per-module TTL config (see §4) |
| content-service | `${SPRING_REDIS_HOST:localhost}` | `${SPRING_REDIS_PORT:6379}` | — | Env-overridable |
| inventory-service | `${SPRING_REDIS_HOST:localhost}` | `${SPRING_REDIS_PORT:6379}` | — | Env-overridable |
| cart-service | `${SPRING_REDIS_HOST:localhost}` | `${SPRING_REDIS_PORT:6379}` | — | Env-overridable |
| catalog-service | `${SPRING_REDIS_HOST:localhost}` | `${SPRING_REDIS_PORT:6379}` | — | Env-overridable |
| search-service | `${SPRING_REDIS_HOST:localhost}` | `${SPRING_REDIS_PORT:6379}` | — | Env-overridable |
| support-service | `${SPRING_REDIS_HOST:localhost}` | `${SPRING_REDIS_PORT:6379}` | — | Env-overridable |
| risk-service | `${SPRING_REDIS_HOST:localhost}` | `${SPRING_REDIS_PORT:6379}` | — | Env-overridable |
| identity-service | `${SPRING_REDIS_HOST:localhost}` | `${SPRING_REDIS_PORT:6379}` | `${SPRING_REDIS_PASSWORD:}` | Has dedicated `RedisConfig` (Jackson serializer) |
| analytics-service | localhost | 6379 | — | |
| admin-service | localhost | 6379 | — | |

All services point at a single shared Redis instance (`redis:7-alpine` per
`docker/docker-compose.yml` and `platform/docker/docker-compose.yml`). There is no
logical Redis separation (no separate DB indices or clusters per service) in the scanned
configuration.

**Observation:** only the ai-service externalizes Redis host/port/TLS via env vars in
some services; the ai-service, analytics-service, and admin-service hardcode
`localhost:6379`. Recommend externalizing all Redis connection settings and moving to a
managed/HA Redis for production.

---

## 3. Cache Key Namespacing

The ai-service uses consistent, hierarchical key prefixes per domain, which prevents
collisions and makes bulk invalidation trivial (via `redisTemplate.keys(prefix + "*")`
+ `delete`). Observed prefixes:

| Module | Key Prefix(es) |
|--------|----------------|
| analytics | `analytics:dashboard:`, `analytics:metrics:`, `analytics:kpis:`, `analytics:reports:`, `analytics:statistics:` |
| workflow | `workflow:def:`, `workflow:exec:`, `workflow:state:`, `workflow:stats:` |
| semantic | `semantic:emb:metadata:`, `semantic:emb:vector:`, `semantic:search:`, `semantic:similarity:`, `semantic:stats:`, `semantic:context:`, `semantic:index:meta:` |
| conversation | `conversation:session:`, `conversation:message:`, `conversation:session-list:`, `conversation:memory:`, `conversation:context:` |
| decision | `decision:result:`, `decision:metadata:`, `decision:registry:`, `decision:stats:`, `decision:explanation:` |
| admin | `admin:configuration:`, `admin:featureFlag:`, `admin:environment:`, `admin:snapshot:`, `admin:metadata:` |
| automation | `automation:workflow:`, `automation:scheduler:`, `automation:lifecycle:`, `automation:config:`, `automation:statistics:` |
| risk | `risk:scores:`, `risk:trust:`, `risk:confidence:`, `risk:thresholds:`, `risk:metadata:` |
| compliance | `compliance:rules:`, `compliance:frameworks:`, `compliance:validation:`, `compliance:reports:`, `compliance:statistics:` |
| governance | `governance:config:`, `governance:registry:`, `governance:health:`, `governance:metrics:`, `governance:validation:` |
| content | `content:gen:`, `content:summary:`, `content:translation:`, `content:seo:`, `content:template:` |
| provider | `provider:registry:`, `provider:capabilities:`, `provider:config:` |
| assistant | `assistant:profile:`, `assistant:intent:`, `assistant:session:context:`, `assistant:recommendation:`, `assistant:task:` |
| ai (generic) | `ai:cache:` (via `AiCacheService`, default 10 min) |
| gateway | `gateway:*` (via `GatewayRedisCacheService`) |

Serialization is JSON via `ObjectMapper` into `StringRedisTemplate` (value stored as a
JSON string). The identity-service uses a dedicated `RedisConfig` with
`GenericJackson2JsonRedisSerializer` for its cache region.

---

## 4. TTL Strategy (Inventory from application.yml + source constants)

TTLs are defined at two layers: (a) per-module values in `ai-service/application.yml`
under `sporekart.ai.*.cache`, and (b) hard-coded `..._TTL` constants in each
`*RedisCacheService`. Where both exist they are consistent. Values below are seconds
unless noted as min/hours.

### 4.1 ai-service module TTLs (from application.yml)

| Module | Cache entry | TTL |
|--------|-------------|-----|
| content | generation / summary / translation / seo | 15 min (`cache.ttl-minutes`) |
| content | template | 30 min (`*RedisCacheService` `TEMPLATE_TTL_MINUTES`) |
| workflow | definitions | 30 min (`cache-ttl-minutes` + `DEF_TTL_MINUTES`) |
| workflow | execution | 15 min (`EXEC_TTL_MINUTES`) |
| workflow | state | 10 min (`STATE_TTL_MINUTES`) |
| workflow | stats | 5 min (`STATS_TTL_MINUTES`) |
| semantic | embedding metadata | 30 min (`embedding-ttl`) / `EMB_META_TTL_MINUTES` |
| semantic | embedding vector | 60 min (`EMB_VECTOR_TTL_MINUTES`) |
| semantic | search | 5 min (`search-ttl`) / `SEARCH_TTL_MINUTES` |
| semantic | similarity | 10 min (`SIMILARITY_TTL_MINUTES`) |
| semantic | index meta | 30 min (`INDEX_META_TTL_MINUTES`) |
| semantic | stats / context | 5 min / 10 min |
| conversation | session | 30 min (`SESSION_TTL_MINUTES`); config `session-ttl-hours: 24` |
| conversation | message | 15 min (`MESSAGE_TTL_MINUTES`) |
| conversation | memory | 60 min (`MEMORY_TTL_MINUTES`) |
| conversation | context | 10 min (`CONTEXT_TTL_MINUTES`) |
| governance | config / registry | 300 s |
| governance | health | 60 s |
| governance | metrics | 120 s |
| governance | validation | 180 s |
| decision | result / metadata / registry / explanation | 300 s |
| decision | statistics | 120 s |
| approval | pending | 60 s |
| approval | assignment / config / workflow | 120–300 s |
| approval | statistics | 120 s |
| compliance | rules / frameworks / reports | 300 s |
| compliance | validation | 180 s |
| compliance | statistics | 120 s |
| risk | scores / trust / confidence / metadata | 300 s |
| risk | thresholds | 600 s |
| analytics | dashboard / kpis / reports | 300 s |
| analytics | metrics / statistics | 120 s |
| admin | configuration / featureFlag / metadata | 300 s |
| admin | environment / snapshot | 600 s |
| automation | workflow / scheduler / config | 300 s |
| automation | lifecycle | 600 s |
| automation | statistics | 120 s |
| provider (generic) | registry / capabilities / config | 10 min (`DEFAULT_TTL_MINUTES`) |
| ai (generic) | default cache | 10 min (`DEFAULT_TTL_MINUTES`) |
| assistant | profile / recommendation | 1 h (`PROFILE_TTL_HOURS` / `RECOMMENDATION_TTL_HOURS`) |
| assistant | session context | 2 h (`SESSION_CONTEXT_TTL_HOURS`) |
| assistant | intent / task | 30 min |

### 4.2 TTL Observations

- TTLs are short (1–10 min) for volatile/derived data (search, stats, health, validation)
  and longer (5–10 min to 1–2 h) for reference/governance data — a sensible gradient.
- Longest TTLs (600 s / 2 h) are applied to low-churn reference data (thresholds,
  environment, snapshot, session context), which is appropriate.
- No negative/`-1` (eternal) TTLs were found, so every entry expires — good for avoiding
  stale permanent cache.

---

## 5. Cache Invalidation Patterns

- Explicit invalidation is implemented per service via `redisTemplate.delete(key)` and
  bulk invalidation via `redisTemplate.keys(prefix + "*")` followed by `delete(set)`.
  Confirmed in tests: `WorkflowRedisCacheService` deletes `workflow:def:`,
  `workflow:exec:`, `workflow:state:`; `SemanticRedisCacheService` deletes
  `semantic:emb:metadata:`, `semantic:emb:vector:`, `semantic:stats:`; `AdminRedisCacheService`
  deletes `admin:configuration:`, `admin:featureFlag:`, `admin:environment:`,
  `admin:snapshot:`, `admin:metadata:`; `KnowledgeRedisCacheService` flushes
  `knowledge:*`.
- Invalidation is **event-driven in intent** but currently tightly coupled to the service
  that owns the cache; there is no cross-service invalidation bus observed (would be a
  natural consumer of the Kafka `*-events` topics from §Event Review).
- Time-based expiry (TTL) is the primary invalidation mechanism; explicit deletes are
  secondary.

**Recommendation:** for cross-service consistency, subscribe cache-owning services to the
relevant Kafka `*-events` topics and evict (`CacheEvict`-style) on domain mutations,
rather than relying solely on TTL lapse.

---

## 6. Cache Warm-Up

**Current state: not implemented / not documented.** No scheduled warm-up,
`@PostConstruct` pre-population, or cache-loader was observed. Caches are populated
lazily on first read (cache-aside pattern via `opsForValue().get` then `set`).

**Recommendation:** for latency-sensitive hot paths (provider registry, governance config,
decision registry, semantic index metadata), add a warm-up step on startup / on config
change so first-request penalty is avoided.

---

## 7. Consistency

- Cache-aside (read-through, write-through delete) is used; writes update the DB and then
  explicitly invalidate/refresh the cache. Because there are no `@Cacheable` annotations,
  the consistency contract is enforced manually in each service method.
- Risk: any code path that mutates state without calling the matching cache-delete leaves
  a stale entry until TTL expiry. With short TTLs this is bounded but not eliminated.
- The ai-service does not share cache regions across services (each service has its own
  `*RedisCacheService` and key namespace), reducing cross-service consistency risk but
  duplicating data.

---

## 8. Memory Strategy

- All values are JSON strings; no use of Redis native hashes, streams, or compression was
  observed. Large payloads (embeddings vectors cached 60 min, semantic search results,
  conversation memory) can be memory-heavy.
- `semantic:emb:vector:` entries are cached for 60 min and can be large (1536-dim
  embeddings) — monitor memory footprint.
- No `maxmemory` policy, eviction policy (`allkeys-lru`), or key-count budgeting is
  configured in the scanned files (Redis server config lives in docker-compose, not
  reviewed here).

**Recommendation:** set a Redis `maxmemory` + `allkeys-lru` (or `volatile-lru`) policy,
and consider compressing large vector payloads or shortening their TTL.

---

## 9. Recovery (Redis Restart / Outage)

- Because caching is cache-aside with bounded TTLs, a Redis restart simply results in a
  cold cache; subsequent reads fall through to the source and repopulate. This is a safe
  failure mode — no data loss (Redis is not the system of record).
- Risk: a cold-cache thundering-herd on restart for hot keys (provider registry, governance
  config, decision registry). No request-coalescing/`SETNX`-based single-flight guard was
  observed.
- Persistence: docker-compose uses `redis:7-alpine` with default (no `appendonly`/
  `save` overrides seen in scanned files). For caches this is acceptable, but confirm
  intentional.

**Recommendation:** add a simple single-flight (mutex/`SET NX`) guard for hot keys, and
document the cold-cache behavior in the runbook. Consider Redis Sentinel / managed Redis
for HA in production.

---

## 10. Findings (Non-Blocking)

| ID | Severity | Finding | Recommendation |
|----|----------|---------|----------------|
| R-1 | Medium | Most services set only host/port; no centralized cache config / HA. | Externalize all Redis settings; use managed/HA Redis in prod. |
| R-2 | Low | No cross-service cache invalidation bus. | Consume Kafka `*-events` to evict stale entries. |
| R-3 | Low | No cache warm-up implemented. | Warm hot paths (registry/config) on startup. |
| R-4 | Low | Manual invalidation easy to miss on new write paths. | Centralize via a `CacheEvict`-style aspect or helper. |
| R-5 | Low | No `maxmemory`/eviction policy visible; large vector payloads. | Set `allkeys-lru`; compress/shorten vector TTL. |
| R-6 | Low | Cold-cache thundering-herd on restart. | Add single-flight guard for hot keys. |
| R-7 | Info | Single shared Redis instance for all services. | Consider per-service DB index or cluster separation. |

**Verdict: PASS WITH RECOMMENDATIONS.** The namespacing, TTL gradient, and invalidation
patterns are sound and certify Step 5. All findings are non-blocking.
