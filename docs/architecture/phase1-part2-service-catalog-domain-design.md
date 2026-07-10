# SporeKart Phase 1 Part 2 — Service Catalog and Bounded Context Design

- Version: 1.0
- Status: Review complete
- Owner: Enterprise Architecture Review Board
- Review Date: 2026-07-10
- Purpose: Freeze the service catalog, bounded contexts, and ownership model for implementation planning.
- Scope: Domain decomposition, service responsibilities, data ownership, and dependency boundaries.
- References: [phase1-part1-architecture-audit.md](phase1-part1-architecture-audit.md), [phase1-part3-spring-boot-enterprise-architecture.md](phase1-part3-spring-boot-enterprise-architecture.md)
- Approval Status: Reviewed; implementation guidance only

## 1. Purpose and Scope

This document freezes the service catalog, domain ownership boundaries, bounded contexts, and cross-service responsibilities for the SporeKart platform. It is intentionally an architecture specification document and does not implement software, generate code, or define runtime APIs.

This design is based on the reviewed architecture materials and the mandatory technology baseline. Where the product requirement set is incomplete, those gaps are explicitly recorded as architecture risks rather than treated as resolved requirements.

## 2. Architecture Freeze Statement

The following design decisions are now frozen for the next implementation phase:

- The platform will be organized around domain-owned services.
- Each service owns its own persistence boundary.
- Cross-service collaboration will use synchronous calls only where strong consistency is required and events for asynchronous workflows.
- Order orchestration remains a first-class domain responsibility owned by the Order Service.
- Supabase is restricted to managed PostgreSQL and storage-related capabilities only.
- Authentication and business authorization remain the responsibility of Spring Security in the backend services.

## 3. Domain Catalog

### 3.1 Core domains

| Domain | Purpose | Business value | Criticality | Owner | Dependencies | Growth considerations | Scalability profile |
|---|---|---|---|---|---|---|---|
| Commerce | Product discovery, carting, ordering, fulfillment coordination | Revenue generation | Critical | Commerce domain team | Identity, Inventory, Payment, Fulfillment | High | High-growth, write-heavy in checkout and order flow |
| Identity and access | Account lifecycle, authentication, role management, session handling | Security and trust | Critical | Identity domain team | None directly; shared dependency | High | Read-heavy and cache-friendly |
| Payments and refunds | Payment orchestration, refund lifecycle, financial reconciliation | Revenue protection and correctness | Critical | Payments domain team | Order, Identity | High | Correctness-sensitive and low-volume but high criticality |
| Fulfillment | Shipment management, carrier abstraction, delivery tracking | Customer experience | High | Fulfillment domain team | Order, Payment | Medium | Bursty and event-driven |
| Learning | Training courses, batches, enrollments, certificates | Growth and engagement | High | Learning domain team | Payment, Identity, Notification | Medium | Moderate and mostly read-heavy |

### 3.2 Supporting domains

| Domain | Purpose | Business value | Criticality | Owner | Dependencies | Growth considerations | Scalability profile |
|---|---|---|---|---|---|---|---|
| Content and publishing | Editorial and marketing content | Brand and conversion | Medium | Content domain team | Search | Medium | Mostly read-heavy |
| Notification | Delivery of transactional and promotional notifications | Retention and service communication | High | Platform/communications team | Identity, Event bus | High | Asynchronous and scalable |
| Search and discovery | Indexing and retrieval for products, content, and training | Conversion and usability | High | Search domain team | Catalog, Content, Training | High | Read-heavy and index-driven |
| Risk and fraud | Order and payment risk evaluation | Loss prevention | High | Risk domain team | Order, Payment | Medium | Low-latency and event-driven |
| Support operations | Ticketing, customer escalation, issue handling | Customer trust | Medium | Support domain team | Order, Fulfillment | Medium | Low-volume but operationally important |

### 3.3 Generic domains

| Domain | Purpose | Business value | Criticality | Owner | Dependencies | Growth considerations | Scalability profile |
|---|---|---|---|---|---|---|---|
| Observability and operations | Monitoring, tracing, health, dashboards | Platform reliability | Critical | Platform team | All services | High | Centralized and operational |
| Platform infrastructure | Messaging, cache, deployment, environment operations | Delivery velocity | Critical | Platform team | All services | High | Shared platform concern |

## 4. Subdomain Catalog

| Domain | Subdomain | Capability | Ownership | Data ownership | Transaction boundary |
|---|---|---|---|---|---|
| Identity | Account lifecycle | Registration, profile, role assignment | Identity Service | User and session data | Strong consistency for account updates |
| Identity | Authentication and sessions | Login, token handling, OTP resolution | Identity Service | Credential state and session state | Strong consistency |
| Commerce | Catalog management | Product and pricing data | Catalog Service | Product and variant data | Strong consistency for writes |
| Commerce | Inventory control | Stock, reservation, allocation | Inventory Service | Stock and reservation records | Strong consistency for hot path |
| Commerce | Basket and checkout | Cart lifecycle and checkout initiation | Cart and Order Services | Cart state and order state | Mixed consistency |
| Commerce | Order management | Order lifecycle and state machine | Order Service | Order and saga state | Strong consistency within the aggregate |
| Commerce | Payment orchestration | Payment intent and settlement handling | Payment Service | Payment and refund data | Strong consistency for financial actions |
| Commerce | Fulfillment coordination | Shipment and carrier management | Fulfillment Service | Shipment and tracking data | Event-driven with local consistency |
| Learning | Course administration | Courses, batches, instructors | Training Service | Training and batch data | Strong consistency for batch updates |
| Learning | Enrollment and certification | Enrollments, progress, certificates | Training Service | Enrollment and certification state | Strong consistency within local aggregate |
| Content | Editorial publishing | Blogs, stories, metadata | Content Service | Content records | Strong consistency for write path |
| Communication | Notification dispatch | Delivery workflow and preference handling | Notification Service | Notification history and preferences | Event-driven |
| Intelligence | Search indexing | Search document generation and updates | Search Service | Search index documents | Eventual consistency |
| Intelligence | Analytics ingestion | Event streams and dashboards | Analytics Service | Analytics event data | Eventual consistency |
| Risk | Fraud scoring | Scoring and rule evaluation | Risk Service | Risk factors and flags | Near-real-time with event-driven processing |
| Support | Case management | Ticket creation and resolution | Support Service | Ticket and case data | Strong consistency for ticket lifecycle |

## 5. Bounded Context Catalog

| Bounded context | Purpose | Responsibilities | Owned data | Owned events | Consumed events | Dependencies | External systems | Business rules | Aggregate roots | State ownership |
|---|---|---|---|---|---|---|---|---|---|---|
| Identity | Identity and access | Account, auth, role, session, OTP | Users, roles, sessions, OTP requests | user.registered, user.role_changed, user.profile_updated | certificate.issued | None foundational; shared dependency | OTP providers, optional email/SMS providers | Password policy, role assignment, session validity | User, Session | User identity state |
| Catalog | Product catalog | Product, variant, category, pricing | Products, categories, variants | product.created, product.updated, product.price_changed | None | Search, Inventory | Product data source integrations if any | Product, Category | Catalog state |
| Inventory | Stock and reservation | Stock, warehouse, reservation, allocation | Stock, reservations, inventory logs | inventory.reserved, inventory.committed, inventory.commit_failed, inventory.restocked | product.created, order.payment_verified, refund.completed | Catalog, Order | Warehouse integrations if present | Reservation TTL, stock commit rules | InventoryItem, Reservation | Inventory availability |
| Cart | Cart lifecycle | Add/remove/update/checkout initiation | Cart state and cart items | cart.checkout_initiated | product.price_changed | Inventory, Catalog | None | Cart, CartItem | Cart session state |
| Order | Order lifecycle and saga orchestration | Order creation, state transitions, compensation | Orders, order items, saga state, idempotency keys | order.created, order.payment_verified, order.approved, order.rejected, return.requested, return.approved, quality_check.requested | inventory.reserved, payment.verified, inventory.commit_failed, pickup.completed, quality_check.passed, refund.completed | Inventory, Payment, Fulfillment, Identity | Payment provider callbacks | Cancellation window, return window, saga compensation | Order, OrderItem, SagaState | Order state |
| Payment | Payments and refunds | Payment intent, verification, refunds | Payments, refunds, webhook events | payment.verified, payment.failed, refund.initiated, refund.completed, refund.failed | order.created, quality_check.passed, inventory.commit_failed | Order, Identity | PSPs and backup PSPs | Idempotency, refund eligibility, reconciliation | Payment, Refund | Financial state |
| Fulfillment | Shipment and tracking | Shipment creation, pickup, tracking, carrier selection | Shipments, tracking events, carriers, serviceability | shipment.created, shipment.dispatched, shipment.delivered, pickup.completed, shipment.ndr | order.approved, return.approved | Order, Payment | Carrier providers | Serviceability, pickup behavior, tracking policy | Shipment | Fulfillment state |
| Training | Learning operations | Courses, batches, enrollment, completion | Trainings, batches, enrollments, certificates | enrollment.confirmed, enrollment.completed, certificate.issued, waitlist.promoted | payment.verified, payment.failed | Payment, Identity, Notification | Training platform integrations if any | TrainingBatch, Enrollment, Certificate | Training lifecycle |
| Content | Content publishing | Blogs, stories, SEO metadata | Blogs, stories | content.published | None | Search | CMS or content tooling if any | Publication workflow and status rules | Blog, Story | Content lifecycle |
| Notification | Communication dispatch | Templates, preferences, delivery logs | Notification templates, delivery logs, preferences | notification.sent, notification.failed | Domain events from multiple services | Identity, Event bus | Email/SMS/WhatsApp providers | Delivery policy, preference honoring | NotificationTrigger, NotificationLog | Delivery lifecycle |
| Search | Search indexing | Build and serve denormalized search documents | Search index documents | None | product.created, product.updated, content.published, enrollment.confirmed | Catalog, Content, Training | Search engine infrastructure | Index freshness and relevance rules | SearchDocument | Search index state |
| Analytics | Event analytics | Ingest events and produce business intelligence | Analytics events and snapshots | None | All domain events | Event bus | Observability backends if used | Retention and event shape rules | AnalyticsEvent | Event data |
| Risk | Fraud and abuse scoring | Evaluate risk signals and flags | Risk scores and abuse flags | risk.flagged | order.created, return.requested, refund.completed | Order, Payment | Risk data sources if any | Scoring thresholds and abuse policy | RiskScore | Risk state |
| Support | Customer issue handling | Tickets and worker workflows | Tickets and messages | ticket.created, ticket.resolved | order.created, refund.failed, shipment.ndr | Order, Fulfillment | Support tooling integrations if any | Ticket | Support case state |

## 6. Complete Service Catalog

| Service | Mission | Business purpose | Business responsibilities | Non-responsibilities | Owned domain | Owned database schema | Owned aggregate roots | Owned events | Consumed events | Dependencies | Scaling strategy | Availability requirement | Latency budget | Failure isolation | Security level | Deployment strategy | Versioning strategy | Observability requirements | Future expansion strategy |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| Identity Service | Secure identity and access control | Own identity state and authorization primitives | User lifecycle, sessions, OTP, roles | Business catalog and commerce operations | Identity and access | Users, sessions, OTP, roles | User, Session | user.registered, user.role_changed, user.profile_updated | certificate.issued | None foundational | Read-heavy; cache sessions and roles | High | < 150 ms for reads | High | Critical | Independent deployment with shared platform services | Versioned through backward-compatible auth contracts | Central logs, metrics, auth audit trails | Support multi-tenant and B2B role expansion |
| Catalog Service | Manage product catalog data | Own product and pricing information | Catalog, categories, variants, price metadata | Inventory and order state | Catalog | Products, categories, variants | Product, Category | product.created, product.updated, product.price_changed | None | Search, Inventory | Read-heavy and cache-friendly | High | < 200 ms for reads | High | Medium | Independent deployment | Versioned catalog contracts | Metrics for catalog reads and mutation rates | Support multi-vendor and localization |
| Inventory Service | Own stock and reservation state | Prevent overselling and manage stock availability | Stock, warehouse allocation, reservations | Order lifecycle decisions | Inventory | Stock, reservations, inventory logs | InventoryItem, Reservation | inventory.reserved, inventory.committed, inventory.commit_failed, inventory.restocked | product.created, order.payment_verified, refund.completed | Catalog, Order | Write-heavy; strong consistency and locking | Very high | < 100 ms for reservation path | High | High | Independent deployment | Strict contract versioning | Metrics for reservation success and contention | Support multiple warehouses and offline fulfillment |
| Cart Service | Manage ephemeral cart state | Hold cart context and pricing intent | Cart items, cart TTL, checkout initiation | Financial settlement and order finalization | Cart | Cart state and items | Cart, CartItem | cart.checkout_initiated | product.price_changed | Inventory, Catalog | Stateless and elastic | Medium | < 250 ms for cart operations | High | Medium | Independent deployment | Short-lived contract versioning | Cart request metrics and TTL telemetry | Support guest and multi-device sessions |
| Order Service | Own the commerce order lifecycle | Coordinate order state and saga execution | Order creation, order state machine, saga state, compensation | Payment settlement processing and warehouse execution | Order | Orders, items, saga state, idempotency keys | Order, OrderItem, SagaState | order.created, order.payment_verified, order.approved, order.rejected, return.requested, return.approved, quality_check.requested | inventory.reserved, payment.verified, inventory.commit_failed, pickup.completed, quality_check.passed, refund.completed | Inventory, Payment, Fulfillment, Identity | Write-heavy and stateful | Very high | < 2 s for checkout confirmation path | High | High | Independent deployment | Strongly versioned saga contracts | Order metrics, saga state monitoring, audit trails | Support B2B orders, subscriptions, and multi-warehouse routing |
| Payment Service | Own payment and refund state | Ensure payment correctness and financial reconciliation | PSP orchestration, payment intent, refunds | Inventory or order state decisions | Payments and refunds | Payments, refunds, webhook events | Payment, Refund | payment.verified, payment.failed, refund.initiated, refund.completed, refund.failed | order.created, quality_check.passed, inventory.commit_failed | Order, Identity | Low-volume but correctness-sensitive | Very high | < 500 ms for orchestration steps | High | Critical | Independent deployment | Strict financial contract versioning | Payment audit logs, idempotency metrics, reconciliation alerts | Support multiple providers and region-specific settlement |
| Fulfillment Service | Own shipment execution | Coordinate shipping and carrier workflows | Shipment lifecycle, tracking, pickup scheduling | Pricing and order creation | Fulfillment | Shipments, tracking events, carrier config | Shipment | shipment.created, shipment.dispatched, shipment.delivered, pickup.completed, shipment.ndr | order.approved, return.approved | Order | Bursty and elastic | High | < 500 ms for command acceptance | High | Medium | Independent deployment | Versioned carrier integration contracts | Shipment metrics and webhook traceability | Support multi-vendor and offline shipping |
| Training Service | Own learning lifecycle | Manage learning experiences and certificates | Training catalog, batches, enrollments, certificates | Core commerce checkout | Learning | Trainings, batches, enrollments, certificates | TrainingBatch, Enrollment, Certificate | enrollment.confirmed, enrollment.completed, certificate.issued, waitlist.promoted | payment.verified, payment.failed | Payment, Identity, Notification | Moderate and mostly read-heavy | High | < 300 ms for enrollment confirmation | High | Medium | Independent deployment | Versioned learning contracts | Learning analytics and enrollment health metrics | Support franchises and multi-tenant cohorts |
| Content Service | Own editorial content | Publish and manage content assets | Blogs, stories, metadata, publication state | Transactional commerce logic | Content | Blogs, stories | Blog, Story | content.published | None | Search | Read-heavy | Medium | < 200 ms for read paths | High | Medium | Independent deployment | Content lifecycle versioning | Publish and retrieval metrics | Support localization and multi-language content |
| Notification Service | Own notification delivery | Ensure communication is delivered reliably | Templates, preferences, delivery attempts | Core business ownership of commerce state | Communication | Notification templates, logs, preferences | NotificationTrigger, NotificationLog | notification.sent, notification.failed | Domain events from all services | Identity, Event bus | Asynchronous and scalable | High | < 1 s enqueue acceptance | High | Medium | Independent deployment | Event-driven contract versioning | Delivery success metrics and retry dashboards | Support multiple channels and segmentation |
| Search Service | Own search index | Support fast discovery of catalog and training content | Document indexing, updates, search readiness | Transactional state | Search and discovery | Search index documents | SearchDocument | None | product.created, product.updated, content.published, enrollment.confirmed | Catalog, Content, Training | Read-heavy and elastic | High | < 200 ms for read paths | High | Medium | Independent deployment | Versioned index contract rules | Index freshness and query metrics | Support multi-language and faceted search |
| Analytics Service | Own analytics event data | Support business and operational analytics | Event ingestion, aggregation, dashboards | Transactional execution | Analytics | Analytics events, snapshots | AnalyticsEvent | None | All domain events | Event bus | Append-heavy and horizontally scalable | Medium | Near-real-time ingestion | High | Low | Independent deployment | Versioned event ingestion contract | Ingestion volume, lag, and dashboard readiness | Support AI-driven insights and expansion |
| Risk Service | Own fraud and abuse evaluation | Reduce fraud and abuse | Scoring, flagging, rule evaluation | Transaction settlement logic | Risk | Risk scores and abuse flags | RiskScore | risk.flagged | order.created, return.requested, refund.completed | Order, Payment | Low-volume but low-latency | High | < 150 ms for hot path checks | High | High | Independent deployment | Versioned scoring rules and payloads | Risk score and false-positive metrics | Support multi-region and adaptive rules |
| Support Service | Own case management | Support customers and operations | Ticketing, escalations, issue resolution | Financial settlement | Support operations | Tickets, ticket messages | Ticket | ticket.created, ticket.resolved | order.created, refund.failed, shipment.ndr | Order, Fulfillment | Low-volume and reliable | Medium | < 500 ms for case submission | High | Medium | Independent deployment | Versioned case workflow contracts | Ticket volume and resolution metrics | Support multi-channel and SLA-driven operations |

## 7. Service Responsibility Matrix

| Capability | Primary service | Secondary service(s) | Notes |
|---|---|---|---|
| Authentication and credential state | Identity Service | None | Must be authoritative |
| Product catalog and pricing | Catalog Service | Search Service | Search consumes catalog events |
| Inventory reservation and stock deduction | Inventory Service | Order Service | Reservation and commit are owned by Inventory |
| Cart state | Cart Service | Inventory Service | Cart references inventory availability |
| Checkout orchestration | Order Service | Inventory Service, Payment Service | Order owns the saga |
| Payment authorization and refund execution | Payment Service | Order Service | Payment owns financial state |
| Shipment and tracking | Fulfillment Service | Order Service | Fulfillment owns delivery lifecycle |
| Training enrollments and certificates | Training Service | Payment Service, Notification Service | Training owns learning lifecycle |
| Content publishing | Content Service | Search Service | Search consumes content updates |
| Notifications | Notification Service | All event-producing services | Notification is a consumer of domain events |
| Search indexing | Search Service | Catalog, Content, Training | Search is read-model oriented |
| Analytics ingestion | Analytics Service | All services | Analytics consumes events |
| Risk scoring | Risk Service | Order Service, Payment Service | Risk is decision-oriented and asynchronous where appropriate |
| Support cases | Support Service | Order Service, Fulfillment Service | Support consumes operational events |

## 8. Ownership Matrix

| Service | Business owner | Technical owner | Domain owner | Change control owner |
|---|---|---|---|---|
| Identity Service | Identity domain lead | Backend platform lead | Identity domain | Platform architecture board |
| Catalog Service | Commerce product lead | Catalog engineering lead | Commerce domain | Domain architecture board |
| Inventory Service | Operations and commerce lead | Inventory engineering lead | Commerce domain | Domain architecture board |
| Cart Service | Commerce experience lead | Cart engineering lead | Commerce domain | Domain architecture board |
| Order Service | Commerce operations lead | Order engineering lead | Commerce domain | Domain architecture board |
| Payment Service | Finance and payments lead | Payments engineering lead | Payments domain | Security and finance review |
| Fulfillment Service | Fulfillment operations lead | Fulfillment engineering lead | Fulfillment domain | Operations review |
| Training Service | Learning product lead | Training engineering lead | Learning domain | Learning domain board |
| Content Service | Content and marketing lead | Content engineering lead | Content domain | Content review board |
| Notification Service | Communications lead | Notifications engineering lead | Communication domain | Platform operations review |
| Search Service | Discovery lead | Search engineering lead | Search domain | Product and platform review |
| Analytics Service | Data and insights lead | Analytics engineering lead | Analytics domain | Data governance review |
| Risk Service | Risk and fraud lead | Risk engineering lead | Risk domain | Security review |
| Support Service | Support operations lead | Support engineering lead | Support domain | Operations review |

## 9. Business Capability Matrix

| Business capability | Service | Capability owner | Notes |
|---|---|---|---|
| Account management | Identity Service | Identity domain | Authoritative |
| Product browsing and catalog management | Catalog Service | Commerce domain | Authoritative |
| Stock availability | Inventory Service | Commerce domain | Authoritative |
| Cart management | Cart Service | Commerce domain | Authoritative |
| Checkout and order creation | Order Service | Commerce domain | Authoritative |
| Payment execution and refund handling | Payment Service | Payments domain | Authoritative |
| Shipping and carrier operations | Fulfillment Service | Fulfillment domain | Authoritative |
| Training enrollment and certification | Training Service | Learning domain | Authoritative |
| Content publishing | Content Service | Content domain | Authoritative |
| Communication delivery | Notification Service | Communication domain | Authoritative |
| Search and discovery | Search Service | Discovery domain | Read-model oriented |
| Analytics and reporting | Analytics Service | Analytics domain | Event-consumer oriented |
| Fraud and abuse control | Risk Service | Risk domain | Decision-oriented |
| Customer support operations | Support Service | Support domain | Case-oriented |

## 10. Database Ownership Matrix

| Service | Database schema | Tables or collections owned | Views or read models owned | Write model | Read model | Transaction ownership | Migration ownership | Retention policy | Backup strategy |
|---|---|---|---|---|---|---|---|---|---|
| Identity Service | Relational | Users, sessions, roles, OTP requests | Session and role projection views | Strong consistency | Cached role and profile views | Authoritative for identity changes | Service-owned Flyway migration set | Business-defined retention | Managed PostgreSQL backup and PITR |
| Catalog Service | Relational | Products, categories, variants | Search projection view if needed | Strong consistency | Cached product views | Authoritative catalog writes | Service-owned Flyway migration set | Product data retention policy | Managed PostgreSQL backup and PITR |
| Inventory Service | Relational | Stock, reservations, inventory logs | Reservation and stock projection views | Strong consistency | Stock availability projection | Authoritative reservation state | Service-owned Flyway migration set | Inventory event retention | Managed PostgreSQL backup and PITR |
| Cart Service | Redis-backed ephemeral storage | Cart state and cart items | None mandatory | Ephemeral write model | Session-based read model | Ephemeral state ownership | Service-owned configuration and key policy | TTL-based retention | Redis persistence and backup policy |
| Order Service | Relational | Orders, order items, saga state, idempotency keys | Order history read model | Strong consistency | Order summary and history projection | Authoritative order lifecycle | Service-owned Flyway migration set | Order retention policy | Managed PostgreSQL backup and PITR |
| Payment Service | Relational | Payments, refunds, webhook events | Reconciliation read models | Strong consistency | Payment status read model | Authoritative financial state | Service-owned Flyway migration set | Financial retention policy | Managed PostgreSQL backup and PITR |
| Fulfillment Service | Relational | Shipments, tracking events, carrier config | Delivery status read model | Strong consistency per shipment lifecycle | Shipment history projection | Authoritative fulfillment state | Service-owned Flyway migration set | Shipment retention policy | Managed PostgreSQL backup and PITR |
| Training Service | Relational | Trainings, batches, enrollments, certificates | Enrollment summary and certificate views | Strong consistency | Training enrollment read model | Authoritative learning state | Service-owned Flyway migration set | Learning data retention policy | Managed PostgreSQL backup and PITR |
| Content Service | Relational | Blogs, stories | Published content views | Strong consistency | Cached content view | Authoritative content state | Service-owned Flyway migration set | Content retention policy | Managed PostgreSQL backup and PITR |
| Notification Service | Relational plus queue-backed delivery | Templates, logs, preferences | Delivery status views | Strong consistency for metadata | Delivery projection | Authoritative notification metadata | Service-owned Flyway migration set | Delivery log retention policy | Managed PostgreSQL backup and PITR |
| Search Service | OpenSearch | Search documents | Search index views | Event-driven write model | Search read model | Eventual consistency | Service-owned index mapping management | Index retention policy | OpenSearch backup strategy |
| Analytics Service | Columnar/event store | Analytics events and snapshots | Dashboard projection models | Append-oriented | Aggregate read models | Eventual consistency | Service-owned ingestion and retention strategy | Event retention policy | Backup and archiving policy |
| Risk Service | Relational | Risk scores and abuse flags | Risk scoring read models | Strong consistency for scoring state | Risk summary read model | Authoritative risk state | Service-owned Flyway migration set | Risk data retention policy | Managed PostgreSQL backup and PITR |
| Support Service | Relational | Tickets and messages | Support case views | Strong consistency | Ticket summary views | Authoritative support state | Service-owned Flyway migration set | Ticket retention policy | Managed PostgreSQL backup and PITR |

## 11. Aggregate Catalog

| Service | Aggregate roots | Entities | Value objects | Domain services | Factories | Repositories | Specifications | Policies | State machine |
|---|---|---|---|---|---|---|---|---|---|
| Identity | User, Session | User profile, session, OTP request | Address, role scope | Authentication policy service | Identity factory | Identity repository | Password and OTP policies | Session validity, role assignment | Authentication/session lifecycle |
| Catalog | Product, Category | Product variant, category | Price snapshot, product identifier | Catalog validation service | Catalog factory | Catalog repository | Product validity, pricing rules | Category and pricing policy | Catalog lifecycle |
| Inventory | InventoryItem, Reservation | Stock record, warehouse allocation | Quantity, reservation TTL | Inventory allocation service | Inventory factory | Inventory repository | Stock availability, reservation policy | Reservation TTL and stock commit policy | Reservation lifecycle |
| Cart | Cart, CartItem | Cart session, line item | Quantity, price hint | Cart pricing service | Cart factory | Cart repository | Cart validity | TTL and price refresh policy | Cart lifecycle |
| Order | Order, OrderItem, SagaState | Order status history, idempotency key | Money, status, correlation ID | Saga orchestration service | Order factory | Order repository | Order transition policy | Cancellation and return windows | Order state machine |
| Payment | Payment, Refund | Webhook event, refund audit | Money, payment status | Payment orchestration service | Payment factory | Payment repository | Refund eligibility, idempotency | Financial reconciliation policy | Payment/refund lifecycle |
| Fulfillment | Shipment | Tracking event, carrier provider | Shipping address, tracking state | Carrier selection service | Fulfillment factory | Fulfillment repository | Serviceability policy | Pickup and shipment policy | Shipment lifecycle |
| Training | TrainingBatch, Enrollment, Certificate | Training, instructor, waitlist entry | Enrollment status, certificate identifier | Enrollment policy service | Training factory | Training repository | Seat availability and waitlist policy | Cancellation and completion policy | Enrollment lifecycle |
| Content | Blog, Story | Content version, metadata | Publication status | Content publishing service | Content factory | Content repository | Publication policy | Editorial governance policy | Content lifecycle |
| Notification | NotificationTrigger, NotificationLog | Delivery preference, recipient | Channel, delivery status | Notification dispatch service | Notification factory | Notification repository | Delivery eligibility policy | Preference and retry policy | Delivery lifecycle |
| Search | SearchDocument | Index document | Search facets, ranking metadata | Indexing service | Search factory | Search repository | Relevance policy | Freshness and rebuild policy | Index lifecycle |
| Analytics | AnalyticsEvent | Event snapshot, funnel snapshot | Event metadata | Analytics ingestion service | Analytics factory | Analytics repository | Event shape policy | Retention and privacy policy | Ingestion lifecycle |
| Risk | RiskScore | Abuse flag, evidence | Risk factors, score | Risk evaluation service | Risk factory | Risk repository | Scoring policy | Fraud policy | Risk evaluation lifecycle |
| Support | Ticket | Ticket message | Priority, status | Case routing service | Support factory | Support repository | Ticket triage policy | SLA and escalation policy | Ticket lifecycle |

## 12. Domain Event Catalog

| Event | Producer | Consumers | Trigger | Business meaning | Payload summary | Version | Ordering requirement | Priority | Failure handling | Monitoring |
|---|---|---|---|---|---|---|---|---|---|---|
| user.registered | Identity Service | Notification, Support | New account created | Identity exists for commerce and learning | user id, profile snapshot | v1 | Not strict | High | Retry and dead-letter handling | Authentication and signup metrics |
| user.role_changed | Identity Service | Notification, Support | Role update | Access profile changed | user id, role | v1 | Not strict | Medium | Retry | Audit log monitoring |
| product.created | Catalog Service | Inventory, Search | New catalog item created | Product is available for sales | product id, price, stock metadata | v1 | Not strict | High | Retry | Catalog update metrics |
| product.updated | Catalog Service | Inventory, Search, Cart | Product changed | Catalog content revised | product id, changed fields | v1 | Not strict | Medium | Retry | Catalog change metrics |
| product.price_changed | Catalog Service | Cart, Search | Pricing updated | Price changed in catalog | product id, price | v1 | Not strict | High | Retry | Pricing and cart metrics |
| cart.checkout_initiated | Cart Service | Order Service | Checkout started | Shopping cart is entering order flow | cart id, user id, items | v1 | Not strict | High | Retry | Checkout funnel metrics |
| inventory.reserved | Inventory Service | Order Service | Reservation created | Inventory is temporarily held | reservation id, product id, quantity | v1 | Strict for checkout order flow | High | Retry and compensation | Reservation monitoring |
| inventory.committed | Inventory Service | Order Service | Reservation converted to stock deduction | Sale completed against inventory | reservation id, order id | v1 | Strict | High | Retry | Inventory commit metrics |
| inventory.commit_failed | Inventory Service | Order Service, Payment Service | Reservation commit failed | Inventory could not be finalized | reservation id, reason | v1 | Strict | High | Compensate and trigger refund path | Inventory failure monitoring |
| inventory.restocked | Inventory Service | Order Service | Refund or cancellation triggered restock | Stock restored | reservation id, order id | v1 | Not strict | Medium | Retry | Restock metrics |
| order.created | Order Service | Payment Service, Notification Service | Order created | Order accepted into lifecycle | order id, user id, totals | v1 | Strict | High | Retry and saga state tracking | Order creation metrics |
| order.payment_verified | Order Service | Inventory Service | Payment verification received | Order can proceed in lifecycle | order id, payment id | v1 | Strict | High | Retry | Payment-to-order propagation |
| order.approved | Order Service | Fulfillment Service, Notification Service | Order approved for shipment | Customer order is ready for fulfillment | order id, shipment context | v1 | Strict | High | Retry | Order approval metrics |
| order.rejected | Order Service | Notification Service, Support Service | Order rejected by workflow | Order lifecycle ended in failure | order id, reason | v1 | Strict | High | Retry | Rejection handling |
| payment.verified | Payment Service | Order Service, Training Service | Payment succeeded | Financial step succeeded | payment id, order id | v1 | Strict | High | Retry | Payment success metrics |
| payment.failed | Payment Service | Order Service, Training Service | Payment failed | Payment path failed | payment id, reason | v1 | Strict | High | Retry and compensation | Payment failure metrics |
| refund.initiated | Payment Service | Notification Service | Refund started | Refund lifecycle began | refund id, order id | v1 | Strict | High | Retry | Refund metrics |
| refund.completed | Payment Service | Inventory Service, Order Service, Notification Service | Refund finalized | Stock and customer state can be updated | refund id, order id | v1 | Strict | High | Retry | Refund completion metrics |
| enrollment.confirmed | Training Service | Notification Service | Training enrollment accepted | Enrollment confirmed | enrollment id, batch id | v1 | Strict | High | Retry | Enrollment metrics |
| enrollment.completed | Training Service | Notification Service, Catalog Service | Learning completed | Completion event available | enrollment id, certificate id | v1 | Not strict | Medium | Retry | Course completion metrics |
| certificate.issued | Training Service | Identity Service, Notification Service | Certificate created | Learner achieved completion | certificate id, user id | v1 | Not strict | Medium | Retry | Certification metrics |
| content.published | Content Service | Search Service | Content published | New content becomes discoverable | content id, slug | v1 | Not strict | Medium | Retry | Content publishing metrics |
| notification.sent | Notification Service | Analytics Service | Notification delivered | Delivery succeeded | notification id, channel | v1 | Not strict | Medium | Retry | Delivery success metrics |
| notification.failed | Notification Service | Analytics Service, Support Service | Notification could not be delivered | Delivery failed | notification id, reason | v1 | Not strict | Medium | Retry and dead-letter handling | Notification failure metrics |
| risk.flagged | Risk Service | Order Service, Support Service | Risk threshold exceeded | Review or block needed | risk id, subject id, score | v1 | Strict for decision path | High | Retry and review queue management | Risk metrics |
| ticket.created | Support Service | Notification Service, Order Service | New support case created | Case handling started | ticket id, subject | v1 | Not strict | Medium | Retry | Support ticket metrics |
| ticket.resolved | Support Service | Notification Service | Case resolved | Customer issue closed | ticket id, status | v1 | Not strict | Medium | Retry | Resolution metrics |

## 13. Dependency Matrix

| Service | Upstream services | Downstream services | Required synchronous APIs | Required events | Optional dependencies | Infrastructure dependencies |
|---|---|---|---|---|---|---|
| Identity Service | None | All services | Session validation, profile lookup | None | Email/SMS providers | PostgreSQL, Redis, Spring Security |
| Catalog Service | None | Inventory, Search, Cart | Product lookup | None | External catalog data feeds if needed | PostgreSQL, Redis |
| Inventory Service | Catalog Service | Order, Cart | Reservation and stock check | product.created, order.payment_verified, refund.completed | Warehouse systems | PostgreSQL, Kafka, Redis |
| Cart Service | Catalog Service, Inventory Service | Order Service | Price and availability checks | product.price_changed | None | Redis, Kafka |
| Order Service | Identity Service, Inventory Service, Payment Service, Fulfillment Service | Payment, Fulfillment, Notification, Support | Order creation and state updates | inventory.reserved, payment.verified, inventory.commit_failed, pickup.completed, quality_check.passed, refund.completed | None | PostgreSQL, Kafka, Redis |
| Payment Service | Order Service | Order Service, Training Service, Notification Service | Payment initiation and refund execution | order.created, quality_check.passed, inventory.commit_failed | PSP provider gateways | PostgreSQL, Kafka |
| Fulfillment Service | Order Service | Order Service, Notification Service | Shipment dispatch and tracking | order.approved, return.approved | Carrier providers | PostgreSQL, Kafka |
| Training Service | Payment Service, Identity Service | Notification Service, Catalog Service | Enrollment and certificate lifecycle | payment.verified, payment.failed | None | PostgreSQL, Kafka |
| Content Service | None | Search Service | Content publication operations | None | CMS integrations if any | PostgreSQL, Redis |
| Notification Service | Identity Service | Analytics Service, Support Service | Preference and delivery state updates | Domain events from all services | Email/SMS/WhatsApp providers | PostgreSQL, Kafka |
| Search Service | Catalog Service, Content Service, Training Service | None | Indexing and query APIs | product.created, product.updated, content.published, enrollment.confirmed | None | OpenSearch, Kafka |
| Analytics Service | All services | None | Event ingestion endpoints if any | All domain events | None | Kafka, analytics store |
| Risk Service | Order Service, Payment Service | Order Service, Support Service | Risk evaluation request/response | order.created, return.requested, refund.completed | None | PostgreSQL, Kafka, Redis |
| Support Service | Order Service, Fulfillment Service | Notification Service | Ticket submission and update | order.created, refund.failed, shipment.ndr | None | PostgreSQL, Kafka |

### Dependency validation findings

- No circular service dependencies are required by the current architecture.
- Shared database ownership is explicitly avoided.
- Shared ownership risks remain highest in Identity and Order because they are cross-cutting and central to many workflows.
- Event contracts must be treated as first-class architecture artifacts to prevent hidden coupling.

## 14. Communication Strategy

### 14.1 Synchronous communication

Use synchronous REST-style communication for:
- Identity and session validation
- Inventory reservation and stock check
- Order creation and state transitions that require immediate confirmation
- Payment initiation where the caller needs an immediate response
- Read requests that need low latency and strong consistency

### 14.2 Asynchronous communication

Use Kafka-based event communication for:
- Order lifecycle updates
- Inventory commit and compensation events
- Payment and refund state transitions
- Notification dispatch
- Search indexing
- Analytics ingestion
- Risk evaluation and support alerts

### 14.3 Reliability policies

- Retry policy: bounded retries with exponential backoff.
- Circuit breaker: required around downstream services with external dependencies.
- Timeout strategy: explicit per-operation timeouts and clear timeout budgets.
- Fallback strategy: degrade gracefully to pending state and queue work rather than failing silently.
- Idempotency strategy: required for all state-changing requests and event consumers.
- Correlation ID strategy: every request and event must carry a correlation ID.
- Saga boundaries: Order Service owns checkout and return/refund orchestration; Payment and Inventory participate through their own local state transitions and compensations.

## 15. Spring Boot Module Specification

The following is a platform-level module specification for the Spring Boot-based implementation. It is architectural only and not implementation code.

| Service | Maven artifact | Java package root | Layer structure | Controller layer | Application layer | Domain layer | Infrastructure layer | Persistence layer | Security layer | Configuration layer | Messaging layer | Validation layer | Exception layer | Mapper layer | DTO layer | Health layer |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| Identity Service | sporekart-identity-service | com.sporekart.identity | api, application, domain, infrastructure | Authentication and account endpoints | Use-case orchestration | User and session domain model | Security adapters and identity integrations | Repository adapters and persistence | Spring Security filters and authorization | Security and environment config | Event publisher integration | Input validation rules | Domain and API exceptions | Mapping between domain and transport models | Request and response DTOs | Health and readiness endpoint |
| Catalog Service | sporekart-catalog-service | com.sporekart.catalog | api, application, domain, infrastructure | Catalog management endpoints | Product and variant use cases | Product and category domain model | Repository and external integrations | Persistence adapters | Role-based access rules | Config and persistence config | Catalog event publishing | Validation rules | Domain and API exceptions | Mapping layer | Catalog DTOs | Health and readiness endpoint |
| Inventory Service | sporekart-inventory-service | com.sporekart.inventory | api, application, domain, infrastructure | Inventory and reservation endpoints | Reservation and allocation use cases | Inventory and reservation domain model | Warehouse integration adapters | Persistence adapters | Security rules for inventory operations | Config and persistence config | Event publishing and consumer hooks | Validation rules | Domain and API exceptions | Mapping layer | Inventory DTOs | Health and readiness endpoint |
| Cart Service | sporekart-cart-service | com.sporekart.cart | api, application, domain, infrastructure | Cart endpoints | Cart lifecycle use cases | Cart domain model | Redis adapters and session adapters | Cache and persistence adapters | Security rules for session and cart access | Config and caching config | Event publication | Validation rules | Domain and API exceptions | Mapping layer | Cart DTOs | Health and readiness endpoint |
| Order Service | sporekart-order-service | com.sporekart.order | api, application, domain, infrastructure | Order and saga endpoints | Saga orchestration and state transitions | Order and saga domain model | Event and external integration adapters | Persistence adapters | Security and RBAC enforcement | Config and persistence config | Event publisher and consumer integration | Validation rules | Domain and API exceptions | Mapping layer | Order DTOs | Health and readiness endpoint |
| Payment Service | sporekart-payment-service | com.sporekart.payment | api, application, domain, infrastructure | Payment and refund endpoints | Payment orchestration use cases | Payment and refund domain model | PSP integration adapters | Persistence adapters | Security rules for financial operations | Config and persistence config | Event publishing and consumer hooks | Validation rules | Domain and API exceptions | Mapping layer | Payment DTOs | Health and readiness endpoint |
| Fulfillment Service | sporekart-fulfillment-service | com.sporekart.fulfillment | api, application, domain, infrastructure | Shipment and tracking endpoints | Fulfillment use cases | Shipment domain model | Carrier integration adapters | Persistence adapters | Security rules for operations | Config and persistence config | Event publishing and consumer hooks | Validation rules | Domain and API exceptions | Mapping layer | Fulfillment DTOs | Health and readiness endpoint |
| Training Service | sporekart-training-service | com.sporekart.training | api, application, domain, infrastructure | Training and enrollment endpoints | Training lifecycle use cases | Training, enrollment, certificate domain model | Persistence adapters and notification hooks | Persistence adapters | Security rules for learners and trainers | Config and persistence config | Event publishing and consumer hooks | Validation rules | Domain and API exceptions | Mapping layer | Training DTOs | Health and readiness endpoint |
| Content Service | sporekart-content-service | com.sporekart.content | api, application, domain, infrastructure | Content endpoints | Content lifecycle use cases | Content domain model | Content repository adapters | Persistence adapters | Security rules for publishing | Config and persistence config | Event publishing | Validation rules | Domain and API exceptions | Mapping layer | Content DTOs | Health and readiness endpoint |
| Notification Service | sporekart-notification-service | com.sporekart.notification | api, application, domain, infrastructure | Notification endpoints | Notification dispatch use cases | Notification domain model | Provider integration adapters | Persistence adapters | Security rules for delivery configuration | Config and provider config | Message consumer and publisher integration | Validation rules | Domain and API exceptions | Mapping layer | Notification DTOs | Health and readiness endpoint |
| Search Service | sporekart-search-service | com.sporekart.search | api, application, domain, infrastructure | Search endpoints | Indexing and search use cases | Search domain model | Search engine adapters | Indexing adapters | Security rules for internal and external queries | Config and search config | Event consumer integration | Validation rules | Domain and API exceptions | Mapping layer | Search DTOs | Health and readiness endpoint |
| Analytics Service | sporekart-analytics-service | com.sporekart.analytics | api, application, domain, infrastructure | Analytics endpoints | Ingestion and aggregation use cases | Analytics domain model | Stream ingestion adapters | Event store adapters | Security rules for internal analytics access | Config and ingestion config | Event consumer integration | Validation rules | Domain and API exceptions | Mapping layer | Analytics DTOs | Health and readiness endpoint |
| Risk Service | sporekart-risk-service | com.sporekart.risk | api, application, domain, infrastructure | Risk evaluation endpoints | Risk scoring use cases | Risk domain model | Rule engine or scoring adapters | Persistence adapters | Security rules for risk operations | Config and scoring config | Event consumer and publisher integration | Validation rules | Domain and API exceptions | Mapping layer | Risk DTOs | Health and readiness endpoint |
| Support Service | sporekart-support-service | com.sporekart.support | api, application, domain, infrastructure | Ticket endpoints | Ticket lifecycle use cases | Ticket domain model | Integration adapters and persistence | Persistence adapters | Security rules for case handling | Config and persistence config | Event consumer and publisher integration | Validation rules | Domain and API exceptions | Mapping layer | Support DTOs | Health and readiness endpoint |

### Module naming standards

- Maven artifact names use the pattern sporekart-<service-name>.
- Java package roots use the pattern com.sporekart.<domain>.
- Service-specific modules must remain independent from each other.
- Shared platform concerns should be isolated into shared modules rather than duplicated across services.

### Dependency rules

- Domain layer must not depend on infrastructure implementations.
- Application layer may depend on domain and infrastructure abstractions.
- Infrastructure layer may depend on domain contracts and external integrations.
- Persistence and messaging adapters must remain behind explicit interfaces.
- Cross-service dependencies must flow through published contracts and event definitions.

## 16. Security Ownership Report

| Service | Authentication requirement | Authorization requirement | RBAC scope | Sensitive data | PII handling | Encryption requirement | Audit requirement | Compliance requirement | Threat model | Security risk level | Spring Security integration strategy |
|---|---|---|---|---|---|---|---|---|---|---|---|
| Identity Service | Mandatory | Mandatory | User, admin, support, internal roles | Credentials, OTP data, session state | High | Encryption at rest and in transit | Mandatory | High | Credential abuse, session hijack | Critical | Central auth filter, token validation, RBAC enforcement |
| Catalog Service | Optional for read flows | Role-based access for management | Admin and catalog editor roles | Pricing and product metadata | Medium | In transit and at rest | Required for admin mutations | Medium | Data tampering | Medium | Method-level security for admin operations |
| Inventory Service | Mandatory for write flows | Role-based and internal-service authorization | Inventory admin and operations roles | Stock and reservation data | Medium | Encryption at rest and in transit | Mandatory | Medium | Oversell and data integrity abuse | High | Service-to-service auth and scoped internal access |
| Cart Service | Optional for guest and signed-in flows | User-scoped access | User and guest scope | Cart state and session data | Medium | Encryption in transit and at rest for persistent state | Recommended | Medium | Cart tampering | Medium | Scope-based access and user ownership checks |
| Order Service | Mandatory | Role-based and workflow-owned | Customer, admin, support roles | Order and payment references | High | Encryption at rest and in transit | Mandatory | High | Order tampering and fraud | High | Fine-grained access to order operations |
| Payment Service | Mandatory | Strict role-based and internal-service authorization | Finance, ops, internal service roles | Financial and refund data | High | Encryption at rest and in transit | Mandatory | High | Fraud and payment abuse | Critical | Strong authentication and audit logging |
| Fulfillment Service | Mandatory | Role-based and internal-service authorization | Ops, admin, carrier integration roles | Shipment and customer address data | High | Encryption at rest and in transit | Mandatory | Medium | Shipment tampering | High | Internal service auth and restricted admin endpoints |
| Training Service | Mandatory | Learner, instructor, admin roles | Learner, instructor, admin | Enrollment and certificate state | High | Encryption at rest and in transit | Mandatory | Medium | Enrollment abuse | Medium | RBAC and identity-aware access |
| Content Service | Mandatory for admin actions | Admin and publisher roles | Admin and content editor roles | Editorial and published content | Medium | Encryption at rest and in transit | Recommended | Medium | Content tampering | Medium | Role-based management controls |
| Notification Service | Mandatory | Internal and user-scoped operations | Internal platform and user roles | Delivery metadata and preferences | Medium | Encryption at rest and in transit | Mandatory | Medium | Notification abuse | Medium | Internal auth and user-level access checks |
| Search Service | Optional for public read | Internal management roles | Admin and internal roles | Search index content metadata | Medium | Encryption at rest and in transit | Recommended | Medium | Index tampering | Medium | Internal auth and role confinement |
| Analytics Service | Mandatory for internal access | Restricted internal roles | Internal analytics roles | Event and user interaction metadata | High | Encryption at rest and in transit | Mandatory | Medium | Data leakage | High | Strict role-based access and audit logging |
| Risk Service | Mandatory | Internal and restricted roles | Risk ops and internal roles | Risk data and fraud evidence | High | Encryption at rest and in transit | Mandatory | High | Abuse and data leakage | High | Strict internal auth and audit trails |
| Support Service | Mandatory | Support, admin, and internal roles | Support and internal roles | Customer case data | High | Encryption at rest and in transit | Mandatory | High | Ticket data leakage | High | RBAC and audit logging |

## 17. Observability Report

| Service | Logging ownership | Metrics ownership | Tracing ownership | Alert ownership | Health checks | Audit logs | Business metrics | Technical metrics | SLIs | SLOs | Error budget |
|---|---|---|---|---|---|---|---|---|---|---|---|
| All services | Service team | Service team | Platform + service team | Platform + service team | Service health and readiness | Service team | Domain-specific success metrics | Latency, error rate, saturation | Availability and latency | Service-specific targets to be approved | To be approved with SLOs |

### Observability expectations per service

- Every service exposes health and readiness endpoints.
- Correlation IDs must be propagated across requests and events.
- OpenTelemetry tracing is required end to end for checkout and refund flows.
- Prometheus metrics and Grafana dashboards are required at the service level.
- Audit trails are required for identity, payments, order approvals, refund actions, and support workflows.

## 18. Scaling Strategy

| Service | Traffic characteristics | CPU usage profile | Memory profile | Database load | Caching need | Horizontal scaling | Vertical scaling | Statelessness | Session management | Availability target | Recovery objective | Recovery time objective |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| Identity | High read volume, moderate write | Moderate | Moderate | Moderate | High | Yes | Moderate | Mostly stateless | Externalized session state | 99.9% | Fast | < 15 min |
| Catalog | High read volume | Moderate | Moderate | Moderate | High | Yes | Moderate | Stateless | None | 99.9% | Fast | < 15 min |
| Inventory | High write and lock contention | High | High | High | Medium | Yes | High | Stateless | None | 99.95% | Fast | < 10 min |
| Cart | High ephemeral traffic | Moderate | Moderate | Low to moderate | High | Yes | Moderate | Stateless | Session or token-based | 99.9% | Fast | < 10 min |
| Order | High write and orchestration | High | High | High | Medium | Yes | High | Stateless for orchestration | None | 99.95% | Fast | < 10 min |
| Payment | Low but critical volume | Moderate | Moderate | Moderate | Low | Yes | Moderate | Stateless | None | 99.99% | Strong | < 5 min |
| Fulfillment | Bursty | Moderate | Moderate | Moderate | Medium | Yes | Moderate | Stateless | None | 99.9% | Fast | < 15 min |
| Training | Moderate | Moderate | Moderate | Moderate | Medium | Yes | Moderate | Stateless | None | 99.9% | Fast | < 15 min |
| Content | Read-heavy | Moderate | Moderate | Moderate | High | Yes | Moderate | Stateless | None | 99.9% | Fast | < 15 min |
| Notification | Burst and async | Moderate | Moderate | Moderate | Medium | Yes | Moderate | Stateless | None | 99.9% | Fast | < 15 min |
| Search | Read-heavy and index-driven | High | High | High | High | Yes | High | Stateless | None | 99.9% | Fast | < 15 min |
| Analytics | Append-heavy | High | High | High | Low | Yes | High | Stateless | None | 99.9% | Fast | < 20 min |
| Risk | Low-latency and event-driven | Moderate | Moderate | Moderate | Medium | Yes | Moderate | Stateless | None | 99.9% | Fast | < 10 min |
| Support | Low-volume | Low | Low | Moderate | Low | Yes | Low | Stateless | None | 99.9% | Fast | < 15 min |

## 19. Future Expansion Assessment

| Expansion area | Impact on current boundaries | Assessment |
|---|---|---|---|
| Marketplace support | Moderate | Current boundaries can support a marketplace model if vendor and seller roles are introduced in Identity and Order |
| Multi-vendor | Moderate | Requires richer Catalog and Order domain concepts but stays within existing service boundaries |
| Internationalization and localization | Low | Content, Catalog, and Identity can absorb this with localized content models |
| Multiple warehouses | Low to moderate | Inventory and Fulfillment boundaries support this well |
| Offline orders | Moderate | Order and Fulfillment can absorb offline capture via asynchronous ingestion |
| Subscriptions | Moderate | Order, Payment, and Training can support this with additional lifecycle rules |
| B2B | Moderate | Identity, Order, Catalog, and Pricing models need extension but remain within current boundaries |
| Franchise model | Moderate | Training, Support, and Order boundaries can support franchised operations with role-based policy extensions |
| Distributor model | Moderate | Fulfillment and Order boundaries need additional rules but remain coherent |
| Analytics expansion | Low | Analytics and Search are good candidates for growth without changing core service boundaries |
| AI features | Moderate | Event-driven data flows make AI integrations feasible without redesigning the service map |

## 20. Service Readiness Score

| Service | Readiness score | Notes |
|---|---|---|
| Identity Service | 82/100 | Strong boundary and clear ownership; auth architecture still needs formal approval |
| Catalog Service | 84/100 | Strong catalog ownership and clear domain fit |
| Inventory Service | 81/100 | Strong consistency boundary; requires explicit reservation and stock policy docs |
| Cart Service | 80/100 | Clear ownership and good fit for ephemeral state |
| Order Service | 78/100 | Central and complex; saga boundaries need formal contract enforcement |
| Payment Service | 76/100 | Correctness-sensitive; finance and audit policy must be documented |
| Fulfillment Service | 79/100 | Good boundary; carrier integration contract still needs governance |
| Training Service | 80/100 | Good fit for learning lifecycle ownership |
| Content Service | 83/100 | Clear ownership with low coupling |
| Notification Service | 81/100 | Good isolation; event contract governance is required |
| Search Service | 82/100 | Clear read-model role with event-driven updates |
| Analytics Service | 80/100 | Good domain separation; data retention policy still needed |
| Risk Service | 77/100 | Good boundary but scoring policy needs explicit governance |
| Support Service | 79/100 | Clear ownership; integration with operational workflows needs formalization |

Overall service architecture readiness: 80/100

## 21. Architecture Freeze Report

The service catalog and bounded context design are now frozen at the architecture level for the next implementation phase.

### Freeze decisions

- Service boundaries are defined and owned.
- Database ownership is explicit and non-overlapping.
- Cross-service communication is partitioned into synchronous and event-driven paths.
- Order remains the authoritative saga orchestrator.
- Supabase is limited to managed PostgreSQL and storage-related capabilities.
- Spring Security remains the required authorization framework for the backend.

### Change-control policy

Any proposal to alter a service boundary, merge responsibilities, or introduce shared ownership requires Architecture Review Board approval before implementation begins.

## 22. Phase 1 Part 2 Completion Checklist

- [x] Domain catalog created.
- [x] Subdomain catalog created.
- [x] Bounded context catalog created.
- [x] Complete service catalog created.
- [x] Service responsibility matrix created.
- [x] Ownership matrix created.
- [x] Business capability matrix created.
- [x] Database ownership matrix created.
- [x] Aggregate catalog created.
- [x] Domain event catalog created.
- [x] Dependency matrix created.
- [x] Communication strategy defined.
- [x] Spring Boot module specification defined.
- [x] Security ownership report created.
- [x] Observability report created.
- [x] Scaling strategy created.
- [x] Future expansion assessment created.
- [x] Service readiness score assigned.
- [x] Architecture freeze report created.

## 23. Phase 1 Part 3 Prerequisites

The following items must be completed before Phase 1 Part 3 can proceed:

1. Formal product requirements and acceptance criteria must be approved.
2. OpenAPI 3.1 and AsyncAPI contracts must be frozen after the architecture boundary review.
3. Security architecture and RBAC policy must be finalized.
4. Event versioning and compatibility rules must be approved.
5. Observability SLOs and alerting responsibilities must be approved.
6. Database migration and backup policy must be documented for each service-owned store.
