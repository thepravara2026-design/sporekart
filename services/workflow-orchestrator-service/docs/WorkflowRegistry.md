# Workflow Registry

## Overview
The Workflow Registry manages workflow definitions — the reusable templates that define workflow structure, steps, and configuration for each business process.

## Definition Model
| Field | Type | Description |
|---|---|---|
| id | String | UUID |
| name | String | Human-readable name |
| description | String | Purpose description |
| type | WorkflowType | Category (ORDER, INVENTORY, etc.) |
| status | WorkflowStatus | ACTIVE, INACTIVE, ARCHIVED, DEPRECATED |
| domain | String | Business domain (Orders, Training, etc.) |
| owner | String | Responsible entity |
| version | String | Semantic version |
| steps | List<WorkflowStep> | Ordered execution steps |
| config | Map | Engine configuration |
| metadata | Map | Additional metadata |

## Workflow Types
- ORDER, CHECKOUT, INVENTORY, RESTOCK
- TRAINING, VENDOR, GROWER, CUSTOMER_LIFECYCLE
- MARKETING, EXECUTIVE, AI, AUTOMATION

## Pre-Defined Workflows
| Workflow | Steps | Domain |
|---|---|---|
| Order Processing | 6 | Orders |
| Order Return | 4 | Orders |
| Inventory Restock | 5 | Inventory |
| Low Stock Alert | 5 | Inventory |
| Training Registration | 4 | Training |
| Training Completion | 4 | Training |
| Vendor Onboarding | 4 | Vendors |
| Grower Onboarding | 4 | Growers |
| Customer Lifecycle | 5 | Customers |
| Customer Support | 4 | Customers |
| Marketing Campaign | 5 | Marketing |
| Executive Review | 5 | Executive |
| AI Model Training | 5 | AI Platform |
| Daily Automation | 5 | Automation |

## API
- `GET /definitions` — List all definitions
- `GET /definitions/{id}` — Get by ID
- `GET /definitions/active` — Active only
- `POST /definitions/generate` — Generate all pre-defined workflows
