# Index Review

## Index Coverage

| Table | Query Pattern | Index | Status |
|-------|--------------|-------|--------|
| carts | Find by customer | idx_carts_customer_id | Created |
| cart_items | Find by cart | idx_cart_items_cart_id | Created |
| reviews | Find by product | idx_reviews_product_id | Created |
| reviews | Find by customer | idx_reviews_customer_id | Created |
| reviews | Filter by status | idx_reviews_status | Created |
| risk_assessments | Find by entity type + id | idx_risk_entity | Created |
| risk_assessments | Filter by risk level | idx_risk_level | Created |
| risk_assessments | Filter by status | idx_risk_status | Created |
| search_documents | Find by entity | idx_search_entity | Created |
| search_documents | Filter by entity type | idx_search_entity_type | Created |
| support_tickets | Find by customer | idx_tickets_customer_id | Created |
| support_tickets | Filter by status | idx_tickets_status | Created |
| support_tickets | Filter by assignee | idx_tickets_assigned_to | Created |
| products | Unique lookup | sku (UNIQUE), slug (UNIQUE) | Existing |
| users | Auth lookup | email (UNIQUE) | Existing |

## Missing Indexes to Consider

- `payments`: order_id (for findByOrderId queries) — added via UNIQUE constraint
- `admin_support_tickets`: status (for filtering)
- `orders`: customer_id (for listing by customer)
- `shipments`: customer_id (for listing by customer)
- `notification_messages`: status, channel (for filtering)

## Index Maintenance

- All indexes are created as part of Flyway migrations
- PostgreSQL automatically maintains indexes on INSERT/UPDATE/DELETE
- Regular `ANALYZE` is performed by PostgreSQL auto-vacuum
- Monitor for unused indexes via `pg_stat_user_indexes`
