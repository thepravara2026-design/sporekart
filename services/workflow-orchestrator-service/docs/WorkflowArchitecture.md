# Workflow Orchestrator Architecture

## Overview
The Enterprise Autonomous Workflow Orchestrator is the central orchestration engine for every business process inside SporeKart. It models, executes, simulates, audits, and monitors business workflows across all business domains.

## Architecture Principles
- **SOLID** — Single responsibility per class; open/closed via ports
- **DDD** — Rich domain models with UUID identifiers; value objects as records
- **Hexagonal Architecture** — Ports (interfaces) + Adapters (implementations); domain isolated from infrastructure
- **State Machine Pattern** — Deterministic state transitions with validation
- **Strategy Pattern** — Per-type decision engine evaluation
- **Mock-First** — All external dependencies are simulated; no production integration

## High-Level Flow
```
Business Event → Workflow Runtime → Registry → Definition → State Machine → Execution Engine → Decision Engine → Simulation Engine → Mock Action Executor → Audit Engine → Timeline
```

## Service Layers
| Layer | Package | Responsibility |
|---|---|---|
| Domain Model | `domain/model/` | Records, enums, value objects |
| Domain Repository | `domain/repository/` | Port interface for persistence |
| Domain Engine | `domain/engine/` | Business logic: state machine, execution, simulation, decision, audit |
| Application Service | `application/service/` | Orchestration layer |
| Application SDK | `application/sdk/` | Client, builder, runtime APIs |
| Infrastructure | `infrastructure/` | In-memory persistence, mock queue, mock executor, cache |
| Interfaces | `interfaces/rest/` | REST API controllers |
| Config | `config/` | Spring configuration, security, binding |

## Service Dependencies
- Port: 8097
- Java 21 + Spring Boot 3.3.3
- No external dependencies (all mocked)
- Stateless; in-memory ConcurrentHashMap persistence
