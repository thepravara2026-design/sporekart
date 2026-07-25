# Workflow Simulation

## Overview
The Simulation Engine runs workflows in simulation mode. Every workflow executes as a dry run — no production actions are ever taken. The simulation produces an execution timeline, business decisions, mock actions, and a result summary.

## Simulation Types
| Type | Description |
|---|---|
| Standard Simulation | Executes existing instance steps with mock actions |
| Rollback Simulation | Simulates reversing all completed actions |
| Dry Run | Executes a definition without creating a persistent instance |

## Simulation Output
| Field | Description |
|---|---|
| success | Whether all actions executed successfully |
| actions | List of mock actions performed |
| decisions | Business decisions made during simulation |
| errors | Any failures encountered |
| result | Summary map with metrics |

## Mock Action Executor
- 95% action success rate (5% random failure for testing)
- Actions return simulated success/failure results
- Rollback reverses actions in opposite order
- All actions tagged with `simulated: true`

## Business Decisions
The Decision Engine evaluates workflow type-specific decisions:
- ORDER: Validate inventory, check payment, process shipment
- INVENTORY: Check thresholds, generate restock recommendation
- TRAINING: Verify eligibility, assign batch/trainer
- VENDOR: Validate documents, background check
- GROWER: Verify identity, check certification
- CUSTOMER: Register, verify, assign tier
- MARKETING: Define campaign, validate budget
- EXECUTIVE: Collect KPIs, generate report
- AI: Prepare data, train, evaluate, deploy
- AUTOMATION: Health checks, process queue, report
