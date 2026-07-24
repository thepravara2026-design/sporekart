# Data Integrity Report

## Constraints Summary

| Service | Table | Primary Key | Unique Constraints | Foreign Keys | Check Constraints |
|---------|-------|-------------|-------------------|-------------|-------------------|
| cart-service | carts | id | — | — | — |
| cart-service | cart_items | id | — | cart_id → carts(id) ON DELETE CASCADE | — |
| content-service | reviews | id | — | — | rating >= 1 AND rating <= 5 |
| risk-service | risk_assessments | id | — | — | risk_score >= 0 AND risk_score <= 100 |
| search-service | search_documents | id | entity_type + entity_id | — | — |
| support-service | support_tickets | id | — | — | — |
| catalog-service | products | id | sku, slug | — | — |
| notification-service | notification_messages | id | — | — | — |
| payment-service | payments | id | order_id | — | — |

## Referential Integrity

- Services are independent microservices — no cross-service FK constraints
- Within each service, FKs enforce parent-child relationships (e.g., cart_items → carts)
- ON DELETE CASCADE ensures child cleanup on parent deletion

## Business Rule Enforcement

| Rule | Enforcement Location |
|------|---------------------|
| Review rating 1-5 | Database CHECK constraint + domain model validation |
| Risk score 0-100 | Database CHECK constraint + domain model validation |
| Cart cannot checkout empty | Service-layer validation (Cart.checkout()) |
| Only open tickets can be assigned | Domain model (SupportTicket.assignTo()) |
| SKU uniqueness | Database UNIQUE constraint + ProductService validation |
| No duplicate registrations | Database UNIQUE constraint on email |

## Optimistic Locking

Currently not implemented at the entity level. To add version-based optimistic locking:
- Add `@Version` field to JPA entities
- Add corresponding `version INTEGER` column to Flyway migrations

## Nullability

- Primary key columns: NOT NULL
- All business-required fields: NOT NULL
- Optional fields (description, resolution, etc.): nullable
- Timestamps: created_at NOT NULL, updated_at allowed nullable for immutable entities
