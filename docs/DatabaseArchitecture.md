# Database Architecture

## Schema per Service

Each microservice manages its own private database schema. No cross-service foreign keys.

| Service | Schema | Tables |
|---------|--------|--------|
| cart-service | cart-service | carts, cart_items |
| content-service | content-service | reviews |
| risk-service | risk-service | risk_assessments |
| search-service | search-service | search_documents |
| support-service | support-service | support_tickets |
| admin-service | admin-service | admin_support_tickets, admin_approval_requests |
| analytics-service | analytics-service | dashboard_widgets, report_requests, seo_metadata |
| catalog-service | catalog-service | products, categories, brands |
| fulfillment-service | fulfillment-service | shipments, shipment_items |
| notification-service | notification-service | notification_messages |
| order-service | order-service | orders, order_items |
| payment-service | payment-service | payments |
| training-service | training-service | training_programs, grower_profiles |
| inventory-service | inventory-service | inventory_items |
| identity-service | identity-service | users, roles, permissions, user_roles |

## Key Tables

### Carts & Cart Items
```sql
carts (id PK, customer_id, status, created_at, updated_at)
cart_items (id PK, cart_id FK, product_id, sku, name, unit_price, quantity, subtotal)
```

### Reviews
```sql
reviews (id PK, product_id, customer_id, rating CHECK(1-5), title, content, status, moderated_by, moderated_at, created_at, updated_at)
```

### Risk Assessments
```sql
risk_assessments (id PK, entity_type, entity_id, risk_score CHECK(0-100), risk_level, factors, assessed_by, assessed_at, status, created_at, updated_at)
```

### Search Documents
```sql
search_documents (id PK, entity_type, entity_id, title, description, content, tags, metadata JSONB, score, indexed_at, updated_at)
```

### Support Tickets
```sql
support_tickets (id PK, customer_id, subject, description, category, priority, status, assigned_to, resolution, created_at, updated_at, resolved_at)
```

## Key Indexes

| Table | Index | Columns |
|-------|-------|---------|
| cart_items | idx_cart_items_cart_id | cart_id |
| carts | idx_carts_customer_id | customer_id |
| reviews | idx_reviews_product_id | product_id |
| reviews | idx_reviews_customer_id | customer_id |
| reviews | idx_reviews_status | status |
| risk_assessments | idx_risk_entity | entity_type, entity_id |
| risk_assessments | idx_risk_level | risk_level |
| risk_assessments | idx_risk_status | status |
| search_documents | idx_search_entity | entity_type, entity_id |
| search_documents | idx_search_entity_type | entity_type |
| support_tickets | idx_tickets_customer_id | customer_id |
| support_tickets | idx_tickets_status | status |
| support_tickets | idx_tickets_assigned_to | assigned_to |

## Migration Strategy

- All services use Flyway for schema versioning
- New tables are created via V1__{service}_foundation.sql
- Existing services have V1-V5 migrations (sprint16 refactoring)
- Production: `flyway.enabled=true`, `ddl-auto: validate`
- Test: `flyway.enabled=false`, `ddl-auto: create-drop` (Hibernate generates schema from entities)
