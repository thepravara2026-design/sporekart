# Persistence Certification Report

**Certification:** Phase 13.5 — Sprint 1 — Part 1  
**Date:** 2026-07-24  
**Result:** ⚠️ **CONDITIONAL PASS** (1 HIGH, 2 MEDIUM)

---

## Certification Criteria

| # | Criterion | Result | Evidence |
|---|-----------|--------|----------|
| 1 | Flyway migrations present | ✅ | 12/17 services have migrations (V1-V5 standard; V1-V36 ai-service) |
| 2 | Database indexes defined | ✅ | 100+ indexes; GIN full-text on knowledge_documents |
| 3 | Foreign key constraints | ✅ | FK constraints across all ai-service migration files |
| 4 | Unique constraints | ✅ | UNIQUE on business keys across all services |
| 5 | Check constraints | ✅ | ENUM-like CHECK constraints in ai-service ERP tables |
| 6 | @Transactional usage | ✅ | 100+ @Transactional annotations (ai-service); readOnly on queries |
| 7 | JPA entity completeness | ✅ | 100+ @Entity classes with @Table, @Column annotations |
| 8 | Repository port/adapter pattern | ✅ | Port interfaces in domain; implementation in infrastructure |
| 9 | HikariCP connection pooling | ⚠️ | ai-service only; other services rely on defaults |
| 10 | ddl-auto strategy | ⚠️ | Inconsistent: `validate` (8 services) vs `none` (7 services) |
| 11 | Migration history continuity | **❌** | inventory-service missing V3 migration |
| 12 | InMemory repository elimination | **❌** | 9 InMemory repos remain — all ACID guarantees bypassed |

---

## HIGH Finding

### H-1: 9 InMemory Repositories Replace Real Database
- **Services:** admin, analytics, catalog, fulfillment, inventory, notification, order, payment, training
- **Implementation:** Each is `ConcurrentHashMap` backed — zero ACID, zero durability, zero integrity
- **Impact:** All persistence work (Flyway, indexes, constraints, transactions) is bypassed at the application layer
- **Remediation:** Replace each with JPA/Spring Data adapter as done in identity-service

---

## MEDIUM Findings

### M-1: inventory-service Missing V3 Migration
- **Path:** `services/inventory-service/src/main/resources/db/migration/`
- **Files:** V1, V2, V4, V5 — V3 is absent
- **Risk:** Flyway will fail if checksums or sequence validation is enabled
- **Remediation:** Add V3 migration or repair the migration baseline

### M-2: HikariCP Only on ai-service
- **Finding:** Only ai-service has explicit HikariCP configuration (max-pool-size: 20, min-idle: 5)
- **Impact:** Other 11 Flyway-enabled services use HikariCP defaults (max-pool-size: 10)
- **Remediation:** Configure HikariCP across all services with consistent pool sizes

---

## Migration Coverage

| Service | Migrations | Indexes | Constraints | Status |
|---------|-----------|---------|-------------|--------|
| ai-service | V1-V36 (36) | 75+ | FK, UNIQUE, CHECK | ✅ |
| admin-service | V1-V5 (5) | 4 | UNIQUE | ✅ |
| analytics-service | V1-V5 (5) | 8 | UNIQUE | ✅ |
| catalog-service | V1-V5 (5) | Standard | Standard | ✅ |
| fulfillment-service | V1-V5 (5) | Standard | Standard | ✅ |
| identity-service | V1-V5 (5) | Standard | Standard | ✅ |
| inventory-service | V1,V2,V4,V5 (4) | Standard | Standard | ⚠️ Missing V3 |
| notification-service | V1-V5 (5) | Standard | Standard | ✅ |
| order-service | V1-V5 (5) | Standard | Standard | ✅ |
| payment-service | V1-V5 (5) | Standard | Standard | ✅ |
| training-service | V1-V5 (5) | 4 | UNIQUE | ✅ |
| cart-service | 0 | 0 | 0 | ❌ Shell |
| content-service | 0 | 0 | 0 | ❌ Shell |
| risk-service | 0 | 0 | 0 | ❌ Shell |
| search-service | 0 | 0 | 0 | ❌ Shell |
| support-service | 0 | 0 | 0 | ❌ Shell |
| gateway-service | 0 | 0 | 0 | N/A (gateway) |

---

## Entity Completeness

| Feature | Status |
|---------|--------|
| @Entity annotations | ✅ 100+ entities |
| @Table(name) | ✅ All entities |
| @Column(name) | ✅ Most entities |
| @Id + @GeneratedValue | ✅ Consistent patterns |
| @Index on entities | ❌ None (all indexes in raw SQL) |
| @Table(indexes) | ❌ Not used |
| UUID type consistency | ⚠️ Mixed String/UUID |
| ON DELETE CASCADE | ⚠️ Only in raw SQL, not JPA annotations |
