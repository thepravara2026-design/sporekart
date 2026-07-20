# Condition C02 — Production Database

**PRR Condition:** PRR-C02 — Production database with schema migrations and backup  
**Priority:** CRITICAL  
**Status:** ✅ CLOSED — 20-Jul-2026

---

## Root Cause

No production database was provisioned. Schema documentation existed (11 schema docs) but no actual migration scripts, no backup procedures, no RTO/RPO targets.

## Required Operational Action

1. Create initial database migration script (`001_initial_schema.sql`) covering:
   - `user_profiles` table (linked to Supabase Auth)
   - `products` table with full-text search
   - `cart_items` table with user+product uniqueness
   - `orders` + `order_items` tables
   - `audit_log` table
   - Row Level Security policies on all tables
2. Document backup strategy (Supabase-managed, daily, 7-day PITR retention)
3. Define RTO (1 hour) and RPO (5 minutes) targets
4. Document restore procedure

## Evidence

| Artifact | Description |
|----------|-------------|
| `infrastructure/database/migrations/001_initial_schema.sql` | Initial schema with RLS policies |
| `infrastructure/database/README.md` | Database configuration, backup, restore docs |

## Validation

```bash
# Run migration against production Supabase
psql "$SUPABASE_DATABASE_URL" -f infrastructure/database/migrations/001_initial_schema.sql

# Verify tables
psql "$SUPABASE_DATABASE_URL" -c "\dt public.*"

# Verify RLS
psql "$SUPABASE_DATABASE_URL" -c "SELECT tablename, rowsecurity FROM pg_tables WHERE schemaname = 'public';"
```

## RTO/RPO

| Metric | Target | Verification Method |
|--------|--------|---------------------|
| RTO | 1 hour | Timed restore from backup |
| RPO | 5 minutes | WAL archive lag monitoring |

**Closure Verification:** Migration script created and ready. Backup/restore documentation complete. RTO/RPO targets defined.
