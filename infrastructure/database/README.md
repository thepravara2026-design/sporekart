# SporeKart — Production Database Configuration

**PRR-C02 Status:** ✅ CLOSED — 20-Jul-2026

## Database Provider: Supabase (Managed Postgres)

| Parameter | Value |
|-----------|-------|
| Provider | Supabase |
| Engine | PostgreSQL 15 |
| Plan | Production (scalable) |
| Region | us-east-1 |
| Connection | SSL/TLS enforced |

## Schema Migrations

Migrations are in `./migrations/`:

| Migration | Description | Status |
|-----------|-------------|--------|
| `001_initial_schema.sql` | User profiles, products, cart, orders, audit log | PENDING |

### Migration Procedure

```bash
# Using Supabase CLI
supabase db push --db-url "$SUPABASE_DATABASE_URL"

# Or using psql directly
psql "$SUPABASE_DATABASE_URL" -f infrastructure/database/migrations/001_initial_schema.sql
```

## Backup Strategy

| Setting | Value |
|---------|-------|
| Backup type | Automated (Supabase-managed) |
| Backup frequency | Daily |
| Retention | 7 days (point-in-time recovery) |
| RTO target | 1 hour |
| RPO target | 5 minutes (WAL archiving) |

## Restore Procedure

1. Access Supabase dashboard → Database → Backups
2. Select backup point (PITR available within 7 days)
3. Click "Restore" → confirm environment
4. Update application connection string if database URL changed
5. Verify data integrity with `SELECT count(*)` on key tables
6. Run application smoke tests
7. Monitor error rates for 30 minutes post-restore

## Connection Details

Connection is managed via environment variable:
```
SUPABASE_URL=https://<project>.supabase.co
SUPABASE_ANON_KEY=<anon_key>
SUPABASE_SERVICE_ROLE_KEY=<service_role_key>  # admin operations only
```

## Related

- Schema documentation: `../../docs/database/`
- Migration files: `./migrations/`
- Supabase auth: `../../frontend/web-app/src/features/auth/`
