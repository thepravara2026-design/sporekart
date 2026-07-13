# Event Review — SporeKart Enterprise AI Platform

**Gate Step:** 4 — Centralized Event Catalog
**Document type:** Architecture Review Board Certification Report (non-blocking findings)
**Scope:** `KafkaConfig.java` (ai-service), Kafka producers/consumers across modules
**Status:** PASS WITH RECOMMENDATIONS

---

## 1. Executive Summary

The SporeKart platform defines a large, consistent event surface. The single
`KafkaConfig` class in `services/ai-service` declares **33 `NewTopic` beans**, every
one named with kebab-case `*<domain>-events` convention and provisioned with
`partitions(3).replicas(1)`. Producers are wired through Spring `KafkaTemplate`
throughout the ai-service domain modules. No `@KafkaListener` consumers are present
anywhere in the scanned codebase, which means the platform currently operates in a
**producer-only** posture: events are emitted but no in-process subscriptions have
been registered yet. This is acceptable for Phase 4 (catalog definition) but must be
resolved before production cutover.

The event taxonomy is coherent and domain-aligned. The principal gaps are the absence
of a schema registry/contract enforcement, the absence of a dead-letter strategy, and
the absence of any consumer-side retry container configuration (there is nothing to
retry yet because there are no listeners). These are non-blocking for certification but
are recommended as Phase 5 work.

---

## 2. Topic Inventory (NewTopic beans)

Source: `services/ai-service/src/main/java/com/sporekart/ai/config/KafkaConfig.java`

Total `NewTopic` beans: **33**. All topics use `partitions=3`, `replicas=1`.

| # | Topic Name | Bean Method |
|---|------------|-------------|
| 1 | `ai-gateway-events` | `aiGatewayEventsTopic()` |
| 2 | `warehouse-events` | `warehouseEventsTopic()` |
| 3 | `supplier-events` | `supplierEventsTopic()` |
| 4 | `procurement-events` | `procurementEventsTopic()` |
| 5 | `erp-integration-events` | `erpIntegrationEventsTopic()` |
| 6 | `inventory-sync-events` | `inventorySyncEventsTopic()` |
| 7 | `finance-events` | `financeEventsTopic()` |
| 8 | `gst-events` | `gstEventsTopic()` |
| 9 | `ai-provider-events` | `aiProviderEventsTopic()` |
| 10 | `knowledge-events` | `knowledgeEventsTopic()` |
| 11 | `ai-prompt-events` | `aiPromptEventsTopic()` |
| 12 | `semantic-events` | `semanticEventsTopic()` |
| 13 | `conversation-events` | `conversationEventsTopic()` |
| 14 | `workflow-events` | `workflowEventsTopic()` |
| 15 | `content-events` | `contentEventsTopic()` |
| 16 | `assistant-events` | `assistantEventsTopic()` |
| 17 | `governance-events` | `governanceEventsTopic()` |
| 18 | `policy-events` | `policyEventsTopic()` |
| 19 | `decision-events` | `decisionEventsTopic()` |
| 20 | `approval-events` | `approvalEventsTopic()` |
| 21 | `compliance-events` | `complianceEventsTopic()` |
| 22 | `risk-events` | `riskEventsTopic()` |
| 23 | `analytics-events` | `analyticsEventsTopic()` |
| 24 | `admin-events` | `adminEventsTopic()` |
| 25 | `automation-events` | `automationEventsTopic()` |
| 26 | `provider-registry-events` | `providerRegistryEventsTopic()` |
| 27 | `prompt-registry-events` | `promptRegistryEventsTopic()` |
| 28 | `knowledge-registry-events` | `knowledgeRegistryEventsTopic()` |
| 29 | `usage-tracking-events` | `usageTrackingEventsTopic()` |
| 30 | `config-registry-events` | `configRegistryEventsTopic()` |
| 31 | `event-catalog-events` | `eventCatalogEventsTopic()` |
| 32 | `api-registry-events` | `apiRegistryEventsTopic()` |
| 33 | `capability-events` | `capabilityEventsTopic()` |

Note: A separate `platform` topic is declared in
`services/identity-service/src/main/java/com/sporekart/identity/config/KafkaConfig.java`
(`platformTopic()`). It is the only non-`*-events` topic and is produced by the
identity-service.

---

## 3. Producers and Consumers per Topic

Producers were mapped by tracing `KafkaTemplate` injection in `src/main/java`. The
event-type string (e.g. `"GovernanceAuditCreated"`) is passed as the Kafka record key
to `kafkaTemplate.send(topic, eventType, payload)`, confirming event naming is
`<Domain><Event>` camel-case per record (distinct from the topic kebab-case naming).

| Topic | Producer Module / Class | Known Consumers |
|-------|-------------------------|-----------------|
| `ai-gateway-events` | `KafkaAiEventPublisher`, `GatewayKafkaEventPublisher` | None (in-process) |
| `warehouse-events` | `application.service.WarehouseService` | None |
| `supplier-events` | `application.service.SupplierService` | None |
| `procurement-events` | `application.service.ProcurementService` | None |
| `erp-integration-events` | `application.service.ERPIntegrationService` | None |
| `inventory-sync-events` | `application.service.InventorySyncService` | None |
| `finance-events` | `application.service.FinanceService` | None |
| `gst-events` | `application.service.GSTService` | None |
| `ai-provider-events` | `ai-provider` publisher path | None |
| `knowledge-events` | `knowledge.infrastructure.KnowledgeKafkaEventPublisher` | None |
| `ai-prompt-events` | `prompt.infrastructure.PromptKafkaEventPublisher` (test) | None |
| `semantic-events` | `semantic.infrastructure.SemanticKafkaEventPublisher` | None |
| `conversation-events` | `conversation.infrastructure.ConversationKafkaEventPublisher` (test) | None |
| `workflow-events` | `workflow.infrastructure.WorkflowKafkaEventPublisher` | None |
| `content-events` | `content.infrastructure.ContentKafkaEventPublisher` (test) | None |
| `assistant-events` | `assistant.infrastructure.AssistantKafkaEventPublisher` (test) | None |
| `governance-events` | `governance.infrastructure.governance.GovernanceKafkaEventPublisher` | None |
| `policy-events` | `policy.infrastructure.kafka.PolicyKafkaEventPublisher` (test) | None |
| `decision-events` | `decision.infrastructure.decision.DecisionKafkaEventPublisher` (`KafkaTemplate<String,Object>`) | None |
| `approval-events` | `approval.infrastructure.kafka.ApprovalKafkaEventPublisher` (`KafkaTemplate<String,Object>`) | None |
| `compliance-events` | `compliance.infrastructure.kafka.ComplianceKafkaEventPublisher` (`KafkaTemplate<String,Object>`) | None |
| `risk-events` | `risk.infrastructure.kafka.RiskKafkaEventPublisher` (`KafkaTemplate<String,Object>`) | None |
| `analytics-events` | `analytics.infrastructure.kafka.AnalyticsKafkaEventPublisher` (test) | None |
| `admin-events` | `admin.infrastructure.kafka.AdminKafkaEventPublisher` (test) | None |
| `automation-events` | `automation.infrastructure.kafka.AutomationKafkaEventPublisher` (test) | None |
| `provider-registry-events` | provider-registry module | None |
| `prompt-registry-events` | prompt-registry module | None |
| `knowledge-registry-events` | knowledge-registry module | None |
| `usage-tracking-events` | usage-tracking module | None |
| `config-registry-events` | config-registry module | None |
| `event-catalog-events` | event-catalog module | None |
| `api-registry-events` | api-registry module | None |
| `capability-events` | capability-discovery module | None |
| `platform` (identity) | identity-service | None |

Observation: every topic today is **producer-only**. This is the single most important
structural fact for the catalog: the catalog currently documents intended
publish/subscribe contracts, but no subscriber is registered, so there is no live
consumer to validate against.

---

## 4. Event Naming Convention

- **Topic level:** kebab-case, suffixed `-events` (e.g. `warehouse-events`). Consistent
  across all 33 ai-service topics.
- **Record key (event type):** camel-case `<Domain><Event>` string passed as the Kafka
  key (e.g. `GovernanceAuditCreated`, `DecisionAuditCreated`, `AutomationAuditRecorded`).
  This is consistent within the ai-service.
- **Outlier:** the identity-service `platform` topic does not follow the `-events`
  suffix. Recommend aligning it (`platform-events`) or documenting the exception in the
  catalog.

---

## 5. Payload Schema Conventions

- Producers serialize payloads via Jackson `ObjectMapper` into JSON strings sent on
  `KafkaTemplate<String, String>` (or `<String, Object>` for the governance/decision/
  approval/compliance/risk publishers).
- The `eventcatalog` module already models event metadata (`EventCatalogEntity`,
  `EventCatalogRequestDto`, `EventCatalogResponseDto`) including an `EventRetryStrategy`
  field — so per-event schema/contract metadata is partially captured in a catalog
  entity, but it is not yet connected to a schema registry or enforced at publish time.
- There is no centralized Avro/Protobuf/JSON-Schema registry. Schema is currently
  implicit in the producer DTO and is not validated on the consumer side (no consumers).

**Recommendation:** introduce a schema registry (e.g. Confluent Schema Registry or
Apicurio) in Phase 5 and register every event payload as a versioned schema. Until
then, the implicit Jackson contract is the only guarantee.

---

## 6. Event Versioning

**Current state: none.** No schema version header, no `schemaVersion` field, no
compatibility policy is present in the codebase.

**Recommendation (header-based versioning):**
- Stamp every record with a `schemaVersion` header (and an `eventType` header) at the
  producer, set from the `EventCatalog` metadata.
- Adopt backward-compatible evolution (additive fields only) as the default compatibility
  mode; reserve breaking changes for a new major version consumed by a new listener.
- Centralize version resolution in `KafkaAiEventPublisher` so all ai-service producers
  inherit it.

---

## 7. Retry Policy

- **Producer side:** the ai-service gateway has its own application-level
  `RetryStrategy` (`GatewayPipeline`, `DefaultRetryStrategy`, `AiGatewayConfig`) that
  retries upstream AI provider calls, not Kafka sends. `automation` has a separate
  `RetryManager` (max-retries 3, backoff 1000ms, multiplier 2.0) for job execution.
- **Consumer side:** Spring Kafka defaults would apply (`DefaultErrorHandler` with
  fixed backoff) **once listeners exist**. There is currently **no**
  `ConcurrentKafkaListenerContainerFactory`, no `SeekToCurrentErrorHandler`, and no
  `@RetryableTopic` in the codebase.
- The `eventcatalog` metadata carries an `EventRetryStrategy` enum, but it is descriptive
  metadata, not wired to a Kafka error handler.

**Recommendation:** when consumers are added, configure a
`ConcurrentKafkaListenerContainerFactory` with `SeekToCurrentErrorHandler` and a bounded
retry (e.g. 3 attempts, exponential backoff) so that transient failures replay from the
current offset without skipping messages.

---

## 8. Dead-Letter Strategy

**Current state: not centralized, effectively absent.** No `DeadLetterPublishingRecoverer`,
no `...-dlq` or `...-dead-letter` topics, and no `DltHandler` were found.

**Recommendation (Phase 5):**
- For every consumed topic, provision a companion dead-letter topic
  (`<topic>-dlq`) with the same partition count.
- Wire `DeadLetterPublishingRecoverer` into the `SeekToCurrentErrorHandler` so that after
  retries are exhausted the record is routed to the DLQ with failed-offset, partition,
  and exception headers preserved.
- Add monitoring/alerting on DLQ depth.

---

## 9. Ordering Assumptions

- Topics are provisioned with 3 partitions. The producer `KafkaTemplate.send(topic, key,
  value)` uses the **event-type string as the key** in the observed publishers. This
  means all records of the same event type land on the same partition and preserve
  order *within an event type*, but records of different event types for the same
  aggregate may be spread across partitions and are not globally ordered.
- **Recommendation:** for aggregate-state events (e.g. `warehouse-events`,
  `procurement-events`, `supplier-events`), use the **aggregate id** (e.g. warehouse id,
  PO id, supplier id) as the partition key rather than the event-type string, so that
  per-aggregate ordering is guaranteed. Update `KafkaAiEventPublisher` and the domain
  publishers to accept and use an aggregate id key.

---

## 10. Idempotency Considerations

- There is no consumer-side idempotency key, no deduplication store, and no
  "event id / dedupe id" header observed in producers.
- Because there are no consumers yet, duplicate processing risk is latent.
- **Recommendation:** stamp every event with a globally unique `eventId` (UUID) and an
  `emittedAt` timestamp header at the producer. Consumers should dedupe on `eventId`
  (e.g. a short-lived Redis set keyed by `eventId`) before handling. Also ensure domain
  handlers are written to be idempotent (upsert-by-aggregate-id semantics).

---

## 11. Findings (Non-Blocking)

| ID | Severity | Finding | Recommendation |
|----|----------|---------|----------------|
| E-1 | Medium | No `@KafkaListener` consumers exist; platform is producer-only. | Register consumers in later phases; catalog should mark each topic's subscriber status. |
| E-2 | Medium | No schema registry / contract enforcement. | Adopt a schema registry in Phase 5; register all payloads. |
| E-3 | Medium | No dead-letter strategy or `...-dlq` topics. | Introduce `DeadLetterPublishingRecoverer` + `SeekToCurrentErrorHandler`. |
| E-4 | Low | No event versioning (headers or fields). | Add `schemaVersion`/`eventType` headers; backward-compatible evolution. |
| E-5 | Low | Partition key is the event-type string, not the aggregate id. | Key by aggregate id for per-aggregate ordering. |
| E-6 | Low | No idempotency/dedupe key on events. | Stamp `eventId` + `emittedAt`; dedupe on consume. |
| E-7 | Low | `platform` topic breaks `-events` naming convention. | Align to `platform-events` or document exception. |
| E-8 | Info | `replicas(1)` on all topics; not production-resilient. | Raise `replication-factor` to >= 3 for production. |

**Verdict: PASS WITH RECOMMENDATIONS.** The centralized event catalog (topic
definitions, naming, producer mapping) is well-formed and sufficient to certify Step 4.
DLQ + schema registry are flagged as Phase 5 follow-ups and are non-blocking.
