# Business Services

## Overview

16 microservices organized into commerce, logistics, platform, intelligence, and engagement domains. All follow hexagonal architecture with domain-driven design packages.

## Commerce Domain

| Service | Capability | Status |
|---------|-----------|--------|
| **cart-service** | Shopping cart CRUD, item management, checkout | Complete, 20 tests |
| **catalog-service** | Product catalog, SKU/slug dedup | Complete, 2 tests |
| **order-service** | Order lifecycle, cancellation, self-ownership | Complete, 1 test |
| **payment-service** | Payment initiation, capture, failure | Complete, 1 test |

## Logistics Domain

| Service | Capability | Status |
|---------|-----------|--------|
| **fulfillment-service** | Shipment creation, cancellation, pickup scheduling | Complete, 1 test |
| **inventory-service** | Stock snapshots, reserved/available quantities | Complete, 1 test |

## Platform Domain

| Service | Capability | Status |
|---------|-----------|--------|
| **gateway-service** | API routing, auth filters, rate limiting, logging, metrics | Complete, 27 tests |
| **identity-service** | User registration, login, role management | Complete, 3 tests |
| **notification-service** | Multi-channel notification (email, SMS, WhatsApp, push) | Complete, 1 test |
| **admin-service** | Dashboard, support ticket admin, approvals | Complete, 1 test |

## Intelligence Domain

| Service | Capability | Status |
|---------|-----------|--------|
| **analytics-service** | Dashboard, sales/metrics/reports, SEO metadata | Complete, 1 test |
| **risk-service** | Risk assessment scoring, mitigation, escalation | Complete, 15 tests |

## Engagement Domain

| Service | Capability | Status |
|---------|-----------|--------|
| **content-service** | Product reviews, moderation workflow | Complete, 17 tests |
| **search-service** | Document indexing, full-text search, reindexing | Complete, 28 tests |
| **support-service** | Support tickets, assignment, resolution workflow | Complete, 23 tests |
| **training-service** | Training program creation, publishing | Complete, 1 test |

## Architecture

Each service follows a uniform structure:

```
src/main/java/com/sporekart/{service}/
├── domain/
│   ├── model/          # Entities, value objects, enums
│   └── repository/     # Port interfaces
├── application/
│   ├── service/        # Use case orchestrators
│   └── dto/            # Request/response DTOs
├── infrastructure/
│   └── persistence/    # InMemory repositories (or JPA adapters)
├── interfaces/
│   └── rest/           # Controllers
├── common/
│   └── exception/      # Exception hierarchy, handlers, ProblemDetails
├── config/             # SecurityConfig, other Spring config
└── ...
```

## Security Model

| Scope | Annotation |
|-------|-----------|
| Public endpoints | No `@PreAuthorize` |
| Authenticated users | `@PreAuthorize("isAuthenticated()")` |
| Admin-only operations | `@PreAuthorize("hasRole('ADMIN')")` |
| Self-ownership | Enforced in service layer (order-service) |
