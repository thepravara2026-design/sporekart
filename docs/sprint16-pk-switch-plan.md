# Sprint16 — PK Switch Plan (String/BIGSERIAL -> UUID)

Goal: safely migrate service primary keys from legacy `id` (VARCHAR/BIGSERIAL) to UUID primary keys, using the previously backfilled `id_uuid` column.

High-level steps (per-service):

1. Preconditions
   - `V3__sprint16_uuid_backfill.sql` has run and `id_uuid` is populated and unique.
   - Backups exist of the target DB/schema.
   - No active deployment writes to the target service during the PK switch window.

2. Phase A — Read-path roll-out (no schema swap)
   - Deploy application change that *reads* by `id_uuid` when present, and continues to accept `id` for write compatibility. Add feature flag gated behavior.
   - Add API compatibility layer that accepts either `id` or `id_uuid` and normalizes internally.
   - Run smoke tests.

3. Phase B — Foreign-key alignment
   - For any tables that reference the affected `id` as FK, add a new `*_id_uuid` column and backfill from join to parent table using `id_uuid` values.
   - Validate referential integrity (counts and null checks).

4. Phase C — PK swap (controlled)
   - Create a new table `tmp_<table>` with the same columns but `id_uuid` as PRIMARY KEY (and UUID type). Copy data mapping `id_uuid` → rows; preserve constraints as needed.
   - Rename original table to `old_<table>` and rename `tmp_<table>` to original name (atomic rename supported by DB). Alternatively, use transactional swap where supported.
   - Recreate indexes, constraints, and triggers.
   - Update sequences or BIGSERIALs as needed for other tables.

5. Phase D — Application cutover
   - Deploy application change that writes and returns `id_uuid` as canonical ID.
   - Validate end-to-end flows, integration tests, and consumers.

6. Phase E — Cleanup
   - After monitoring window, drop `old_<table>` or archive it to a separate schema.
   - Remove compatibility columns/feature flags.

Validation queries and checks (examples)
- Count mismatch check: `SELECT COUNT(*) FROM table WHERE id_uuid IS NULL;` should be 0.
- FK null check: `SELECT COUNT(*) FROM child WHERE parent_id_uuid IS NULL AND parent_id IS NOT NULL;` should be 0.

Rollback strategy
- If migration fails before cutover, restore from backup or revert the rename (if performed). Keep `old_<table>` until satisfied.

Notes & cautions
- Cross-service references and external integrations must be coordinated — document APIs that change returned ID formats.
- Identity/roles tables require special review because they influence RBAC; plan a separate migration for `identity-service`.
- Test all migrations in an isolated staging database before production.
