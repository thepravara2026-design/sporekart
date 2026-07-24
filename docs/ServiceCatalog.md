# Service Catalog

Total: 16 services across business, infrastructure, and AI domains.

## Business Services (10)

| # | Service | Module | Role | Endpoints | Auth | Tech |
|---|---------|--------|------|-----------|------|------|
| 1 | **cart-service** | Commerce | Shopping cart management | 7 REST | isAuthenticated() | InMemory, Spring Security |
| 2 | **catalog-service** | Commerce | Product catalog | 2 REST | Public + ADMIN | InMemory, Spring Security |
| 3 | **order-service** | Commerce | Order lifecycle | 4 REST | Public (self-ownership) | InMemory, Spring Security |
| 4 | **payment-service** | Commerce | Payment processing | 3 REST | ADMIN | InMemory, Spring Security |
| 5 | **fulfillment-service** | Logistics | Shipment management | 5 REST | ADMIN | InMemory, Spring Security |
| 6 | **inventory-service** | Logistics | Stock management | 1 REST | ADMIN | InMemory + JPA, Spring Security |
| 7 | **identity-service** | Platform | Auth & user management | 4 REST | Public | JPA, Redis, Kafka, BCrypt |
| 8 | **notification-service** | Platform | Multi-channel notifications | 2 REST | ADMIN | InMemory, Spring Security |
| 9 | **admin-service** | Platform | Admin operations | 4 REST | ADMIN | InMemory + JPA, Spring Security |
| 10 | **analytics-service** | Intelligence | Analytics, reports, SEO | 16 REST | ADMIN + 2 Public | InMemory + JPA, Spring Security |

## Domain Services (5)

| # | Service | Module | Role | Endpoints | Auth | Tech |
|---|---------|--------|------|-----------|------|------|
| 11 | **content-service** | Engagement | Reviews & moderation | 5 REST | Public + ADMIN | InMemory, Spring Security |
| 12 | **risk-service** | Intelligence | Risk assessment | 5 REST | Public + ADMIN | InMemory, Spring Security |
| 13 | **search-service** | Platform | Search indexing & query | 5 REST | Public + ADMIN | InMemory, Spring Security |
| 14 | **support-service** | Engagement | Customer support tickets | 7 REST | Public | InMemory, Spring Security |
| 15 | **training-service** | Engagement | Training programs | 3 REST | Public + ADMIN | InMemory, Spring Security |

## Infrastructure Services (1)

| # | Service | Module | Role | Tech |
|---|---------|--------|------|------|
| 16 | **gateway-service** | Platform | API gateway, routing, auth, rate-limiting | Spring Cloud Gateway, Redis, JWT |

## AI Service (pre-existing, separate scope)

| # | Service | Status |
|---|---------|--------|
| 17 | **ai-service** | 68 pre-existing compile errors in AI modules (governance, approval, automation, compliance, policy, risk) |

## Test Coverage

| Service | Tests | Coverage Layer |
|---------|-------|---------------|
| cart-service | 20 | Controller, Service, Repository, App context |
| content-service | 17 | Controller, Service, Repository, App context |
| risk-service | 15 | Controller, Service, Repository, App context |
| search-service | 28 | Controller, Service, Repository, App context |
| support-service | 23 | Controller, Service, Repository, App context |
| gateway-service | 27 | Filter, Config, Security, Proxy, QA, Integration |
| identity-service | 3 | Controller, Service, App context |
| catalog-service | 2 | Service, Repository |
| admin-service | 1 | Controller |
| analytics-service | 1 | Controller |
| notification-service | 1 | Controller |
| inventory-service | 1 | Service |
| order-service | 1 | Service |
| payment-service | 1 | Service |
| fulfillment-service | 1 | Service |
| training-service | 1 | Service |
| **Total** | **143** | |
