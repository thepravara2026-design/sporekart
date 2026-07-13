# SporeKart Enterprise AI Platform — Database Review

**Certification Gate:** Step 3 — Database / Persistence Review
**Reviewing Body:** Enterprise Architecture Review Board (EARB)
**Service Under Review:** `ai-service` (Spring Boot + JPA/Hibernate + Flyway)
**Scope:** Flyway migrations under `services/ai-service/src/main/resources/db/migration/`
**Date:** 2026-07-12
**Status:** CERTIFIED WITH NON-BLOCKING FINDINGS

---

## 1. Executive Summary

The `ai-service` persistence layer is managed by **Flyway** (enabled in `application.yml`, `locations: classpath:db/migration`) against an H2 in-memory database running in **PostgreSQL compatibility mode** (`jdbc:h2:mem:ai-service;MODE=PostgreSQL`). The schema is defined by **36 versioned migrations (V1–V36)** plus one non-executable template (`README_sprint16_pk_switch_template.sql`), creating **225 tables** in aggregate.

The schema is well-structured: **snake_case** table/column naming, **per-module prefixes** (`ai_`, `pr_`, `kr_`, `ut_`, `cr_`, `ec_`, `ar_`, `cd_` and others), **UUID primary keys**, pervasive **audit columns** (`created_at` / `updated_at`), broad **indexing** (341 index definitions), and **soft-delete** support via `deleted_at`/`is_deleted` (190 references). Referential integrity is partially enforced (19 explicit foreign keys). No blocking defects were identified for this gate.

**Mobile Client Statement:** The schema is API-driven (accessed exclusively through the versioned REST layer reviewed in Step 2). It is fully capable of supporting future Android/iOS clients without any relational redesign — mobile compatibility is a contract concern, not a schema concern.

---

## 2. Flyway Migration Hygiene

- **Sequential numbering:** Migrations are labeled `V1` through `V36` in strict ascending order with no gaps — Flyway will apply them deterministically.
- **Sprint labeling:** File suffixes encode sprint provenance (e.g., `sprint17_ai_platform_foundation`, `sprint18_governance`). Naming is descriptive and traceable to delivery increments.
- **Non-executable artifact:** `README_sprint16_pk_switch_template.sql` is present as documentation/template only (not a `V*` file) — correct, will not be picked up by Flyway.
- **`IF NOT EXISTS` coverage (F-01):** 462 `IF NOT EXISTS` clauses are present across migrations, but **not every `CREATE TABLE` uses it**. The `knowledge_*`, `semantic_*`, and `ai_admin_*` tables (V14, V15, V27) are declared as plain `CREATE TABLE` without `IF NOT EXISTS`. While Flyway's checksum mechanism prevents re-runs, missing `IF NOT EXISTS` reduces portability for ad-hoc/restore scripts.
- **Sprint/version ordering mismatch (F-02):** Version order does not track sprint chronology perfectly — e.g., `V18__sprint18_content` is followed by `V19__sprint17_assistant`, and `V10–V19` are mostly `sprint17` while `V18` is `sprint18`. This is cosmetic (Flyway orders by version, not sprint label) but can confuse readers. Recommend aligning future migration comments with version order.
- **Idempotency / rollback:** No `V*__*_rollback.sql` or repeatable (`R__`) migrations are present. Rollforward-only is acceptable for this platform but should be documented in the ops runbook.

---

## 3. Migration Inventory

| Version | File | Module / Sprint | # Tables |
|---------|------|-----------------|----------|
| V1 | V1__ai_foundation.sql | AI Foundation | 9 |
| V2 | V2__sprint12_ai_experiences.sql | AI Experiences (s12) | 5 |
| V3 | V3__sprint13_marketplace.sql | Marketplace (s13) | 10 |
| V4 | V4__sprint14_b2b_commerce.sql | B2B Commerce (s14) | 12 |
| V5 | V5__sprint15_mobile_platform.sql | Mobile Platform (s15) | 6 |
| V6 | V6__sprint16_enterprise_erp.sql | Enterprise ERP (s16) | 19 |
| V7 | V7__sprint16_erp_read_models.sql | ERP Read Models (s16) | 0 |
| V8 | V8__sprint16_operations_persistence.sql | Operations (s16) | 1 |
| V9 | V9__sprint16_persistence_audit_fix.sql | Audit Fix (s16) | 0 |
| V10 | V10__sprint17_ai_platform_foundation.sql | AI Platform (s17) | 3 |
| V11 | V11__sprint17_ai_gateway.sql | AI Gateway (s17) | 3 |
| V12 | V12__sprint17_ai_provider_abstraction.sql | Provider Abstraction (s17) | 5 |
| V13 | V13__sprint17_ai_prompt_management.sql | Prompt Mgmt (s17) | 6 |
| V14 | V14__sprint17_knowledge_management.sql | Knowledge Mgmt (s17) | 9 |
| V15 | V15__sprint17_semantic_intelligence.sql | Semantic (s17) | 6 |
| V16 | V16__sprint17_conversation.sql | Conversation (s17) | 8 |
| V17 | V17__sprint17_workflow.sql | Workflow (s17) | 9 |
| V18 | V18__sprint18_content.sql | Content (s18) | 8 |
| V19 | V19__sprint17_assistant.sql | Assistant (s17) | 8 |
| V20 | V20__sprint18_governance.sql | Governance (s18) | 6 |
| V21 | V21__sprint18_policy.sql | Policy (s18) | 7 |
| V22 | V22__sprint18_decision.sql | Decision (s18) | 6 |
| V23 | V23__sprint18_approval.sql | Approval (s18) | 9 |
| V24 | V24__sprint18_compliance.sql | Compliance (s18) | 8 |
| V25 | V25__sprint18_risk.sql | Risk (s18) | 8 |
| V26 | V26__sprint18_analytics.sql | Analytics (s18) | 7 |
| V27 | V27__sprint18_admin.sql | Admin (s18) | 7 |
| V28 | V28__sprint18_automation.sql | Automation (s18) | 9 |
| V29 | V29__sprint18_providerregistry.sql | Provider Registry (s18) | 4 |
| V30 | V30__sprint18_promptregistry.sql | Prompt Registry (s18) | 4 |
| V31 | V31__sprint18_knowledgeregistry.sql | Knowledge Registry (s18) | 3 |
| V32 | V32__sprint18_usagetracking.sql | Usage Tracking (s18) | 2 |
| V33 | V33__sprint18_configregistry.sql | Config Registry (s18) | 4 |
| V34 | V34__sprint18_eventcatalog.sql | Event Catalog (s18) | 3 |
| V35 | V35__sprint18_apiregistry.sql | API Registry (s18) | 5 |
| V36 | V36__sprint18_capabilitydiscovery.sql | Capability Discovery (s18) | 6 |
| — | README_sprint16_pk_switch_template.sql | Template (not executed) | 0 |
| **Total** | | | **225** |

---

## 4. Table Naming Conventions

- **snake_case** is used consistently for both tables and columns.
- **Per-module prefixes** are applied, enabling clear ownership and safe cross-module joins. Observed prefixes and representative tables:

| Prefix | Module | Example Tables |
|--------|--------|----------------|
| `ai_` | Core AI platform | `ai_providers`, `ai_prompts`, `ai_conversations`, `ai_messages`, `ai_admin_configuration`, `ai_feature_flags` |
| `pr_` | Provider Registry | `pr_providers`, `pr_provider_models` |
| `kr_` | Knowledge Registry | `kr_knowledge_sources`, `kr_knowledge_entries` |
| `ut_` | Usage Tracking | `ut_usage_records`, `ut_usage_daily` |
| `cr_` | Config Registry | `cr_config_entries`, `cr_config_versions` |
| `ec_` | Event Catalog | `ec_events`, `ec_event_schemas` |
| `ar_` | API Registry | `ar_api_definitions`, `ar_api_versions` |
| `cd_` | Capability Discovery | `cd_capabilities`, `cd_capability_profiles` |
| `conversation_` | Conversation module | `conversation_sessions`, `conversation_messages` |
| `knowledge_` | Knowledge module | `knowledge_documents`, `knowledge_chunks`, `knowledge_tags` |
| `semantic_` | Semantic module | `semantic_embeddings`, `semantic_vector_index` |
| `vendor_` / `purchase_` / `supplier_` / `warehouse_` / `gst_` / `finance_` / `erp_` / `mobile_` | Commerce / ERP / Mobile legacy domains | marketplace, B2B, ERP, mobile platform tables |

- **Minor inconsistency (F-03):** Some modules use the prefix as a full word rather than the 2–3 letter abbreviation mandated by policy — e.g., `conversation_*`, `knowledge_*`, `semantic_*`, `vendor_*`, `purchase_*`, `supplier_*`, `warehouse_*`, `finance_*`, `erp_*`, `mobile_*`, `gst_*`, `credit_*`, `push_*`. While still snake_case and module-scoped, they deviate from the terse `ai_/pr_/kr_/ut_/cr_/ec_/ar_/cd_` abbreviation standard. New modules should follow the abbreviated form; retro-fixing existing tables is optional (carry cost vs. benefit).

---

## 5. Primary Keys and UUID Usage

- **UUID primary keys are the dominant strategy.** Two physical representations coexist:
  - **Native `UUID` type** — used in early migrations (e.g., `V1__ai_foundation.sql`: `id UUID PRIMARY KEY`).
  - **`VARCHAR(36)`** — used in later registry migrations (e.g., `V29–V36`: `id VARCHAR(36) NOT NULL`).
- **JPA alignment:** 124 occurrences of `@GeneratedValue` / `GenerationType.UUID` in entity code confirm UUID generation is centralized in the application (Hibernate generates the UUID before insert). This is consistent with the `VARCHAR(36)` storage and the API's string-based identifiers.
- **Finding (F-04):** Mixed UUID storage (`UUID` native vs `VARCHAR(36)`) across migrations. Under H2/Postgres mode both work, but for a production Postgres target, `UUID` native is preferable (smaller, index-friendlier, type-checked). Recommend standardizing new tables on native `UUID` and planning a follow-up migration to convert `VARCHAR(36)` PKs where feasible.
- **String-safety:** UUID-as-string keys are ideal for the mobile/API contract (no binary leakage), reinforcing Step 2's mobile-compatibility conclusion.

---

## 6. Constraints and Foreign Keys

- **Primary keys:** Every table carries an explicit PK (UUID-based).
- **Foreign keys:** 19 explicit `FOREIGN KEY` constraints were found — **low relative to 225 tables**. Most inter-table references in the newer modules appear to be **logical** (UUID columns without declared FK constraints).
- **Finding (F-05):** Referential integrity is under-enforced at the database level. Declaring FKs (with appropriate `ON DELETE` behavior) would prevent orphan rows and make the schema self-documenting. The trade-off is migration/insert ordering complexity; recommend at minimum adding FKs for high-criticality parent/child relationships (e.g., workflow steps → workflow, conversation messages → conversation, registry entries → owners).
- **NOT NULL discipline:** Core identity/audit columns are consistently `NOT NULL` with sensible defaults (`created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP`).

---

## 7. Indexes

- **341 index definitions** (`CREATE INDEX` / `CREATE UNIQUE INDEX`) across migrations — strong coverage for lookup and join performance.
- Indexes are present on foreign-key-like columns, sync-status, type, owner, and provider/model dimensions (observed in registry/usage modules).
- **Finding (F-06):** Index coverage should be validated against the actual query patterns of the `v1` API (especially list/filter endpoints noted in Step 2 F-05). The absence of a uniform pagination/filter contract makes index tuning harder; once query patterns stabilize, confirm composite indexes exist for the most common `WHERE`/`ORDER BY` combinations.
- Unique indexes are used where appropriate (registry uniqueness, provider/model pairs).

---

## 8. Audit Columns

- **`created_at` / `updated_at`** appear in **315** locations, indicating audit timestamps are near-universal across tables.
- Pattern observed: `created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP`, `updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP`.
- **Finding (F-07):** A minority of tables (e.g., some read-model / log / analytics tables) omit `updated_at`. This is acceptable for append-only/log tables but should be intentional. Consider a standardized audit-column template applied by every migration for mutable entities.
- **No DB-level trigger-based audit** was observed; auditing is application-driven (consistent with the `*-audit: true` feature flags in `application.yml`). This is the recommended pattern for an API-first platform.

---

## 9. Soft Delete

- Soft delete **IS present and standardized**: **190** references to `deleted_at` / `is_deleted` columns across modules.
- Pattern: nullable `deleted_at TIMESTAMP` (or boolean `is_deleted`) enables logical deletion and audit retention without physical row loss — important for compliance/ governance modules (compliance, approval, risk, decision).
- **Recommendation:** Document the soft-delete convention in the data dictionary and ensure all list/read queries in the `v1` API filter out soft-deleted rows by default (with an explicit `includeDeleted` parameter where needed). This is a contract concern to confirm in Step 2 follow-up.

---

## 10. Query Performance Considerations

- UUID PKs yield wide, non-sequential indexes; under high write volume this can cause index bloat. Acceptable at current scale; monitor as data grows.
- H2 in-memory (current `application.yml`) is for dev/test only — production must target Postgres (already implied by `MODE=PostgreSQL`). Validate that all DDL is Postgres-compatible (native `UUID`, `TEXT`, `TIMESTAMP`) before deployment.
- 341 indexes are healthy but should be reviewed for unused/redundant indexes after production observability data is available.
- Read models (V7 `erp_read_models`) indicate a CQRS-style separation — good for query performance isolation.
- Vector/search tables (`semantic_embeddings`, `semantic_vector_index`) suggest future semantic retrieval; ensure appropriate index types (e.g., pgvector IVFFlat/HNSW) are planned for the Postgres target.

---

## 11. Referential Integrity Summary

| Aspect | Status | Notes |
|--------|--------|-------|
| Primary keys | Strong | UUID on all tables |
| Foreign keys | Partial | 19 explicit; many logical-only |
| Unique constraints | Present | Registry/config uniqueness enforced |
| NOT NULL | Strong | Core columns enforced |
| Audit columns | Strong | 315 refs; near-universal |
| Soft delete | Present | 190 refs; standardized |
| Indexes | Strong | 341 definitions |

---

## 12. Mobile Client Readiness (Schema Perspective)

- The schema is **never accessed directly by clients** — all reads/writes flow through the versioned REST API (Step 2). Therefore no relational redesign is required to support Android/iOS clients.
- UUID string keys, ISO-8601 timestamps (`TIMESTAMP`/`OffsetDateTime`), and JSON-serializable column types make the data layer natively compatible with mobile representations.
- Soft-delete and audit columns provide the temporal/retention semantics mobile offline-sync scenarios need (conflict resolution can be built on `updated_at`/`version`).
- **Action:** Ensure the `v1` API exposes stable, paginated list endpoints backed by the indexed columns so mobile clients can sync efficiently (ties to Step 2 F-05).

---

## 13. Findings (Non-Blocking)

| ID | Severity | Finding | Recommended Action |
|----|----------|---------|--------------------|
| F-01 | Low | Some `CREATE TABLE` (knowledge/semantic/admin) lack `IF NOT EXISTS`. | Add `IF NOT EXISTS` for portability of restore scripts. |
| F-02 | Low | Sprint labels vs version order not aligned (V18=s18 before V19=s17). | Annotate migration comments to match version sequence. |
| F-03 | Low | Mixed prefix styles (`ai_` vs full-word `knowledge_`, `vendor_`). | Standardize new tables on 2–3 letter prefixes; optionally retro-fix. |
| F-04 | Medium | UUID stored as both native `UUID` and `VARCHAR(36)`. | Standardize on native `UUID` for Postgres target; plan conversion migration. |
| F-05 | Medium | Only 19 explicit foreign keys across 225 tables. | Add FKs for critical parent/child relationships to enforce integrity. |
| F-06 | Low | Index coverage not yet validated against real query patterns. | Tune composite indexes once `v1` query patterns stabilize. |
| F-07 | Low | Some append-only tables omit `updated_at`. | Apply standard audit-column template to mutable entities. |
| F-08 | Medium | H2 in-memory is dev-only; production must be Postgres. | Confirm DDL Postgres-compatibility; document target engine. |

---

## 14. Certification Decision

**Result: PASS (with non-blocking findings).** The Flyway-managed schema satisfies the enterprise expectations for naming, modularization, UUID identity, auditability, soft-delete, and indexing. The schema fully supports future mobile clients without redesign. Findings F-04 and F-05 are recommended as pre-production hardening items but do not block certification.
