# Migration Guide

## Flyway Configuration

All services use Flyway with PostgreSQL. Configuration in `application.yml`:

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
  jpa:
    hibernate:
      ddl-auto: validate
```

## Naming Convention

Files follow the Flyway standard format: `V{version}__{description}.sql`

| Pattern | Description |
|---------|-------------|
| V1__{service}_foundation.sql | Initial table creation |
| V2__sprint16_persistence_review.sql | Sprint 16 review changes |
| V3__sprint16_uuid_backfill.sql | UUID backfill for existing PKs |
| V4__sprint16_pk_switch_{service}.sql | Primary key migration to UUID |
| V5__sprint16_pk_cleanup.sql | Cleanup after PK migration |

## Migration Files per Service

| Service | Migration Files |
|---------|-----------------|
| admin-service | V1-V5 |
| analytics-service | V1-V5 |
| catalog-service | V1-V5 |
| fulfillment-service | V1-V5 |
| notification-service | V1-V5 |
| order-service | V1-V5 |
| payment-service | V1-V5 |
| training-service | V1-V5 |
| inventory-service | V1, V2, V4, V5 |
| identity-service | V1-V5 |
| cart-service | V1 (new) |
| content-service | V1 (new) |
| risk-service | V1 (new) |
| search-service | V1 (new) |
| support-service | V1 (new) |

## Production Safety

- All CREATE statements use `IF NOT EXISTS`
- All index creation uses IF NOT EXISTS pattern
- Migrations are ordered and versioned
- Do NOT modify existing migrations (create a new V{n+1} instead)
- Test migrations locally against H2 before deploying to PostgreSQL

## Rollback

Flyway does not support rollback for versioned migrations. To revert:

1. Create a new V{n+1}__undo_*.sql migration
2. Deploy the revert migration
3. Or restore from database backup
