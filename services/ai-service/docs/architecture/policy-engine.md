# Enterprise AI Policy Engine Architecture

## Overview

The Policy Engine is the centralized system for defining, resolving, evaluating, and enforcing policies across all AI services. It follows a hexagonal (ports & adapters) architecture with strict separation of domain logic from infrastructure concerns.

## Pipeline Flow

```
EvaluationRequest
       │
       ▼
┌──────────────────┐
│  PolicyResolver   │  resolvePolicies(request) → List<Policy>
│  (Repository)     │  - Filter by module, scope, active status
└────────┬─────────┘
         │ List<Policy>
         ▼
┌──────────────────┐
│  PolicyEvaluator  │  evaluate(policy, request, context) → PolicyEvaluation
│  (Rule matching)  │  - ConditionEvaluation → RuleMatching → ViolationCollection
└────────┬─────────┘
         │ List<PolicyEvaluation>
         ▼
┌──────────────────┐
│ PolicyDecision    │  decide(request, evaluations) → PolicyDecision
│ Service           │  - Conflict resolution (DENY_OVERRIDES default)
└────────┬─────────┘
         │ EvaluationResult
         ▼
┌──────────────────┐
│ PolicyAudit       │  recordAudit(audit) → append-only audit log
│ Service           │  - Decision, violations, timing
└────────┬─────────┘
         │
         ▼
   EvaluationResult
```

## Module Structure

```
com.sporekart.ai.policy
├── domain/                   # Pure domain model (8 enums, 14 records)
│   ├── PolicyStatus.java
│   ├── PolicySeverity.java
│   ├── PolicyDecision.java
│   ├── PolicyScope.java
│   ├── PolicyAction.java
│   ├── ConditionOperator.java
│   ├── ConflictStrategy.java
│   ├── PolicyType.java
│   ├── Policy.java
│   ├── PolicyRule.java
│   ├── PolicyCondition.java
│   ├── PolicyContext.java
│   ├── PolicyEvaluation.java
│   ├── PolicyViolation.java
│   ├── PolicyVersion.java
│   ├── PolicyMetadata.java
│   ├── PolicyAudit.java
│   ├── PolicyRegistry.java
│   ├── EvaluationRequest.java
│   ├── EvaluationResult.java
│   ├── PolicyExpression.java
│   └── PolicyConfiguration.java
│
├── api/                      # Port interfaces (11)
│   ├── PolicyEngine.java
│   ├── PolicyEvaluator.java
│   ├── PolicyResolver.java
│   ├── PolicyLifecycleManager.java
│   ├── PolicyValidator.java
│   ├── PolicyCompiler.java
│   ├── PolicyDecisionService.java
│   ├── PolicyConfigurationService.java
│   ├── PolicyAuditService.java
│   ├── PolicyMetricsService.java
│   └── PolicyRegistry.java
│
├── application/              # Service implementations (11)
│   ├── PolicyEngineImpl.java
│   ├── PolicyEvaluatorImpl.java
│   ├── PolicyResolverImpl.java
│   ├── PolicyLifecycleManagerImpl.java
│   ├── PolicyValidatorImpl.java
│   ├── PolicyCompilerImpl.java
│   ├── PolicyDecisionServiceImpl.java
│   ├── PolicyConfigurationServiceImpl.java
│   ├── PolicyAuditServiceImpl.java
│   ├── PolicyMetricsServiceImpl.java
│   └── PolicyRegistryImpl.java
│
├── engine/                   # Rule engine (2 classes)
│   ├── RuleEngine.java       # Rule matching, conflict resolution
│   └── ConditionEvaluator.java # Condition evaluation (14 operators)
│
├── config/                   # Configuration
│   └── PolicyConfig.java     # @ConfigurationProperties
│
├── infrastructure/           # Adapters (framework-dependent)
│   ├── persistence/          # 7 JPA entities + 7 repositories
│   │   ├── PolicyEntity.java
│   │   ├── PolicyRuleEntity.java
│   │   ├── PolicyConditionEntity.java
│   │   ├── PolicyVersionEntity.java
│   │   ├── PolicyEvaluationEntity.java
│   │   ├── PolicyAuditEntity.java
│   │   ├── PolicyRegistryEntity.java
│   │   ├── PolicyRepository.java
│   │   ├── PolicyRuleRepository.java
│   │   ├── PolicyConditionRepository.java
│   │   ├── PolicyVersionRepository.java
│   │   ├── PolicyEvaluationRepository.java
│   │   ├── PolicyAuditRepository.java
│   │   └── PolicyRegistryRepository.java
│   ├── kafka/
│   │   └── PolicyKafkaEventPublisher.java  # 8 event types
│   ├── redis/
│   │   └── PolicyRedisCacheService.java    # 5 namespaces
│   ├── monitoring/
│   │   └── PolicyMonitoringService.java    # 10 Micrometer metrics
│   └── security/
│       └── PolicyException.java            # Error codes (POL_400, POL_404, POL_500)
│
└── interfaces/
    └── rest/                  # REST adapter
        ├── PolicyController.java     # 10 endpoints
        └── dto/                      # 10 DTO records
            ├── EvaluationRequestDto.java
            ├── EvaluationResultDto.java
            ├── EvaluationListDto.java
            ├── PolicyRequestDto.java
            ├── PolicyResponseDto.java
            ├── PolicyListDto.java
            ├── PolicyHealthDto.java
            ├── PolicyReloadDto.java
            ├── PolicyErrorDto.java
            └── ViolationDto.java
```

## Hexagonal Layers

| Layer | Responsibility | Framework Dependencies |
|-------|---------------|----------------------|
| **domain/** | Pure business logic — enums, records | None (pure Java) |
| **api/** | Port interfaces — service contracts | None (pure Java) |
| **application/** | Use case orchestration | Spring @Service, Lombok |
| **engine/** | Core rule matching & evaluation | Spring @Service |
| **infrastructure/** | Adapters — DB, messaging, cache, metrics | JPA, Kafka, Redis, Micrometer |
| **interfaces/** | REST API — inbound adapter | Spring @RestController |

## Service Dependency Graph

```
PolicyEngine
  ├── PolicyResolver → PolicyRepository (JPA)
  ├── PolicyEvaluator → PolicyDecisionService
  ├── PolicyDecisionService
  ├── PolicyAuditService → PolicyAuditRepository (JPA)
  └── PolicyMetricsService

PolicyEvaluator
  └── PolicyDecisionService

PolicyLifecycleManager
  ├── PolicyRepository (JPA)
  └── PolicyVersionRepository (JPA)

PolicyRegistry
  └── PolicyRegistryRepository (JPA)
```

## Integration with Governance

- Policy Engine is a separate module from Governance (Sprint 18 Part 1)
- Governance handles: policy lifecycle, configuration management, RBAC, audit logging, usage quotas, compliance
- Policy Engine handles: rule matching, condition evaluation, decision determination, evaluation pipeline
- Governance policies define *what* should be enforced; Policy Engine rules define *how* enforcement is evaluated
- Both modules share the same hexagonal pattern but have independent domain models
- Future integration will route governance compliance checks through the policy evaluation pipeline

## Key Design Rules

1. Domain records are immutable — no setters, no mutable state
2. Services depend on interfaces (api/), not implementations
3. Infrastructure depends on application, not vice versa
4. The engine/ layer is framework-agnostic (pure Java with Spring @Service for DI)
5. All external communication goes through infrastructure adapters
