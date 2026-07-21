# Sprint 29 — Chapter 1: Prompt Aggregate (DDD Foundation)

## Architecture Overview

The Prompt Aggregate is the single source of truth for every prompt inside the SporeKart Enterprise Platform. It follows Domain-Driven Design with clear aggregate boundaries, rich domain models, and enforced invariants.

```
┌─────────────────────────────────────────────────────────────────────────┐
│                      PROMPT AGGREGATE BOUNDARY                           │
│                                                                          │
│  ┌──────────────────────────────────────────────────────────────────┐   │
│  │                        Prompt (Aggregate Root)                    │   │
│  │                                                                   │   │
│  │  - PromptId (VO)                                                   │   │
│  │  - name / description / category / type / owner                    │   │
│  │  - status (DRAFT → REVIEW → APPROVED → PUBLISHED → DEPRECATED →   │   │
│  │           ARCHIVED)                                                 │   │
│  │  - visibility / scope / priority                                   │   │
│  │  - PromptMetadata (VO)                                              │   │
│  │  - PromptExecutionPolicy (VO)                                       │   │
│  │  - PromptVersion (Entity) ◄── current version                       │   │
│  │  - PromptVersion[] (Entity) ◄── version history                     │   │
│  │  - PromptAuditEntry[] (VO) ◄── audit trail                          │   │
│  │  - dependencies / tags / labels                                     │   │
│  │  - PromptDomainEvent[] (Event) ◄── pending events                   │   │
│  └──────────────────────────────────────────────────────────────────┘   │
│                              │ owns                                      │
│              ┌───────────────┼───────────────┐                          │
│              ▼               ▼               ▼                          │
│  ┌──────────────────┐ ┌──────────────┐ ┌──────────────────┐            │
│  │  PromptVersion   │ │PromptTemplate│ │PromptAuditEntry  │            │
│  │  (Entity)        │ │ (Entity)     │ │ (VO)             │            │
│  │                  │ │              │ │                  │            │
│  │ - SemanticVersion│ │ - type       │ │ - AuditAction   │            │
│  │ - changeSummary  │ │ - rawContent │ │ - performedBy   │            │
│  │ - publishedBy/At │ │ - sections[] │ │ - timestamp     │            │
│  │ - draft/immutable│ │ - variables[]│ │ - previousValue │            │
│  │ - deprecated     │ │ - execPolicy│ │ - newValue      │            │
│  │ - template (ref) │ └──────────────┘ │ - reason        │            │
│  └──────────────────┘                  └──────────────────┘            │
│                                               │                         │
│                                               ▼                         │
│                                  ┌──────────────────────┐              │
│                                  │   PromptVariable     │              │
│                                  │   (VO)               │              │
│                                  │                      │              │
│                                  │ - name / type        │              │
│                                  │ - required / default │              │
│                                  │ - validation rules   │              │
│                                  │ - sensitive / visible│              │
│                                  └──────────────────────┘              │
└─────────────────────────────────────────────────────────────────────────┘
```

## Package Structure

```
com.sporekart.ai.prompt.domain
├── aggregate/
│   └── Prompt.java                    Aggregate root
├── entity/
│   ├── PromptVersion.java             Version entity
│   └── PromptTemplate.java            Template entity
├── valueobject/
│   ├── PromptId.java                  Strongly-typed identifier
│   ├── SemanticVersion.java           Semantic versioning (major.minor.patch)
│   ├── PromptStatus.java              Lifecycle state machine enum
│   ├── PromptCategory.java            Category enum (16 types)
│   ├── PromptType.java                Type enum (6 types)
│   ├── PromptVisibility.java          Visibility enum (5 levels)
│   ├── PromptScope.java               Scope enum (5 levels)
│   ├── PromptPriority.java            Priority enum (4 levels)
│   ├── SensitivityLevel.java          Sensitivity enum (5 levels)
│   ├── ComplianceLevel.java           Compliance enum (5 levels)
│   ├── RiskLevel.java                 Risk enum (5 levels)
│   ├── CostClassification.java        Cost enum (5 levels)
│   ├── VariableType.java              Variable type enum (9 types)
│   ├── SectionType.java               Template section enum (7 types)
│   ├── AuditAction.java               Audit action enum (16 actions)
│   ├── PromptMetadata.java            Business metadata record
│   ├── PromptVariable.java            Variable definition record
│   ├── VariableValidation.java        Variable validation rules
│   ├── TemplateSection.java           Template section record
│   ├── PromptExecutionPolicy.java     Execution configuration
│   └── PromptAuditEntry.java          Audit entry record
├── event/
│   ├── PromptDomainEvent.java         Base event interface
│   ├── BasePromptEvent.java           Abstract event base
│   ├── PromptCreatedEvent.java
│   ├── PromptUpdatedEvent.java
│   ├── PromptVersionCreatedEvent.java
│   ├── PromptPublishedEvent.java
│   ├── PromptDeprecatedEvent.java
│   ├── PromptArchivedEvent.java
│   ├── PromptMetadataUpdatedEvent.java
│   └── PromptExecutionPolicyChangedEvent.java
├── repository/
│   └── PromptRepository.java          Repository interface
├── factory/
│   └── PromptFactory.java             Factory for aggregate creation
├── specification/
│   ├── PromptSpecification.java       Specification interface
│   ├── ActiveVersionSpecification.java
│   ├── PublishedVersionSpecification.java
│   ├── ValidTemplateSpecification.java
│   └── VariableValidationSpecification.java
├── exception/
│   ├── PromptDomainException.java     Base domain exception
│   ├── InvalidStatusTransitionException.java
│   ├── PublishedVersionImmutableException.java
│   ├── DuplicateVersionException.java
│   ├── InvalidTemplateException.java
│   ├── RequiredVariableMissingException.java
│   └── PromptNotFoundException.java
└── service/
    └── PromptDomainService.java       Domain service
```

## Lifecycle State Machine

```
                    ┌─────────────────────────────────────────────┐
                    │              PROMPT LIFECYCLE                │
                    └─────────────────────────────────────────────┘

     ┌─────────┐
     │  DRAFT  │
     └────┬────┘
          │
          ├────────────────── submitForReview()
          │
          ▼
     ┌─────────┐
     │ REVIEW  │
     └────┬────┘
          │
          ├────────────────── approve()
          │
          ▼
     ┌──────────┐
     │ APPROVED │
     └────┬─────┘
          │
          ├────────────────── publish()
          │
          ▼
     ┌───────────┐
     │ PUBLISHED │
     └─────┬─────┘
           │
           ├────────────────── deprecate()
           │
           ▼
     ┌────────────┐
     │ DEPRECATED │
     └──────┬─────┘
            │
            ├────────────────── archive()
            │
            ▼
     ┌──────────┐
     │ ARCHIVED │
     └────┬─────┘
          │
          └────────────────── restore() → DRAFT
```

### Allowed Transitions

| From | To | Required Condition |
|------|----|-------------------|
| DRAFT | REVIEW | Has active version |
| DRAFT | ARCHIVED | — |
| REVIEW | APPROVED | — |
| REVIEW | DRAFT | — |
| APPROVED | PUBLISHED | Has active version |
| APPROVED | DRAFT | — |
| PUBLISHED | DEPRECATED | — |
| PUBLISHED | DRAFT | — |
| DEPRECATED | ARCHIVED | — |
| DEPRECATED | PUBLISHED | Re-publish |
| ARCHIVED | DRAFT | Restore |

## Business Rules (Invariants)

1. **Cannot publish without approval** — Only APPROVED prompts can transition to PUBLISHED
2. **Cannot delete published version** — Published versions are immutable snapshots
3. **Cannot archive active prompt** — Must deprecate before archiving
4. **Cannot remove required variables** — Required variables are protected
5. **Cannot change immutable versions** — Published versions are read-only
6. **Cannot publish invalid template** — Template must have non-empty content
7. **Cannot downgrade semantic versions** — Version numbers must increase
8. **Cannot duplicate version number** — Each version must be unique per prompt
9. **Cannot assign multiple active versions** — Only one active version at a time
10. **Name must not exceed 200 characters** — Enforced by aggregate

## Domain Events

| Event | Triggered By | Payload |
|-------|-------------|---------|
| PromptCreatedEvent | Factory.create() | promptId, name, createdBy |
| PromptUpdatedEvent | updateName/Description | promptId, performedBy |
| PromptVersionCreatedEvent | createVersion/createNextVersion | promptId, version, createdBy |
| PromptPublishedEvent | publish() | promptId, version, performedBy |
| PromptDeprecatedEvent | deprecate() | promptId, performedBy, reason |
| PromptArchivedEvent | archive() | promptId, performedBy, reason |
| PromptMetadataUpdatedEvent | updateMetadata() | promptId, performedBy |
| PromptExecutionPolicyChangedEvent | updateExecutionPolicy() | promptId, performedBy |

## File Count

| Layer | Files |
|-------|-------|
| Value Objects | 20 |
| Entities | 2 |
| Aggregate Root | 1 |
| Domain Events | 9 |
| Exceptions | 7 |
| Repository | 1 |
| Factory | 1 |
| Specifications | 5 |
| Domain Service | 1 |
| Tests | 1 (40 test cases) |
| Documentation | 1 |
| **Total** | **49 files** |

## Design Decisions

1. **Aggregate Root owns all children** — Prompt owns versions, templates, variables. No external service manipulates them.
2. **Immutable value objects** — All value objects are Java records with constructor validation.
3. **Rich domain model** — Business logic inside the aggregate, not in services.
4. **State machine in enum** — PromptStatus encodes allowed transitions, preventing invalid states at compile-time.
5. **Event-sourcing ready** — Domain events capture every state change for eventual event sourcing.
6. **Semantic versioning** — Versions follow major.minor.patch with pre-release and build metadata.
7. **Published versions are immutable** — Once published, a version cannot be modified.
8. **Audit trail** — Every mutation produces an audit entry with before/after values.
9. **Strong typing** — No primitive obsession; PromptId, SemanticVersion, etc. are typed.
10. **Specification pattern** — Business rules are encapsulated in reusable specifications.

## Extension Strategy

- Add `PromptCategory` values as new business domains emerge
- Extend `PromptExecutionPolicy` with new provider-specific parameters
- Add `TemplateSection` types for new prompt structures (e.g., FEW_SHOT, CHAIN_OF_THOUGHT)
- Implement `PromptRepository` with JPA or Mongo for persistence
- Add `PromptVersion` comparison for diff analysis
- Add `PromptTemplate` compilation for variable resolution
- Add `PromptDomainService` for cross-aggregate operations
