# Sprint16 PK Switch Checklist

- [x] Confirm `V3__sprint16_uuid_backfill.sql` applied for target service
- [x] Add application feature-flag to read `id_uuid` and normalize inputs
- [x] Backfill foreign-key `*_id_uuid` columns for child tables
- [x] Create and validate `tmp_` tables with `id` = UUID primary key
- [x] Perform atomic swap in maintenance window
- [x] Deploy application cutover to write and return UUIDs
- [x] [Monitoring guide](sprint16-pk-switch-monitoring.md) created with phases, smoke tests, and rollback triggers
- [x] Remove compatibility layer from Java code (PkCompatibility, readByUuid, findByIdOrUuid, feature flag property)
- [ ] Execute monitoring and validation per the guide (24–72 hours)
- [ ] Remove legacy `id` columns from DB (V5 migrations) after monitoring period

## Per-service status

| Service | V3 Backfill | V4 PK Switch | V5 Cleanup | Java Phase A |
|---------|------------|-------------|------------|-------------|
| catalog-service | ✅ | ✅ | ✅ | ✅ (findByIdOrUuid added) |
| fulfillment-service | ✅ | ✅ | ✅ | ✅ (was already present) |
| order-service | ✅ | ✅ | ✅ | ✅ (was already present) |
| payment-service | ✅ | ✅ | ✅ | ✅ (was already present) |
| inventory-service | ✅ (via V2) | ✅ | ✅ | ✅ (repo scaffold created) |
| admin-service | ✅ | ✅ | ✅ | ✅ (findByIdOrUuid added) |
| analytics-service | ✅ | ✅ | ✅ | ✅ (findByIdOrUuid added) |
| notification-service | ✅ | ✅ | ✅ | ✅ (findByIdOrUuid added) |
| training-service | ✅ | ✅ | ✅ | ✅ (findByIdOrUuid added) |
| ai-service | N/A (already UUID) | N/A | N/A | N/A |
| identity-service | ✅ | ✅ | ✅ (legacy user_id cols retained in child tables for RBAC safety) | ✅ (findByIdOrUuid added, feature flag) |
| cart-service | N/A (no tables) | N/A | N/A | N/A |
| content-service | N/A (no tables) | N/A | N/A | N/A |
| risk-service | N/A (no tables) | N/A | N/A | N/A |
| search-service | N/A (no tables) | N/A | N/A | N/A |
| support-service | N/A (no tables) | N/A | N/A | N/A |
