# Future Integrations (Warehouse)

This module is a **foundation** in Mock Mode. The following integrations are intentionally deferred
to later sprints (Part 3+) and are represented as placeholders/empty states today.

## Planned connectors
- **WMS (Warehouse Management System):** real-time bin/quantity sync, task orchestration, wave picking.
- **ERP:** inbound PO → receiving, outbound SO → dispatch, finance reconciliation.
- **3PL / Carrier:** rate shopping, label generation, tracking webhooks.
- **IoT / Sensors:** temperature, humidity, weight and location telemetry for cold storage and bins.
- **Scanning:** barcode/QR (bin, shelf, rack, pallet) capture for put-away, picking and cycle counts.

## Data readiness
The current domain model already supports the seams these integrations need:
- `Warehouse`, `Zone`, `Rack`, `Shelf`, `Bin` and `StorageNode` define the physical graph.
- `Warehouse.type` includes `virtual` (logical aggregation) and `transit` (in-motion stock).
- `TemperatureType` (ambient/cold/frozen/controlled) supports cold-chain telemetry.
- `Warehouse.status` lifecycle (planned → active → maintenance → decommissioned) supports onboarding/offboarding flows.

## Operational workflows (deferred)
Receiving, put-away, picking, replenishment, transfers, adjustments, cycle counting and validation
are referenced by quick actions (`operations_future` permission) but not yet implemented.

## Integration contract (recommended)
When Part 3 begins, replace `warehouseMockService` with a real client behind the same hook surface
(`useWarehouses`, `useWarehouseDashboard`, `useWarehouseMutations`, …) so UI components require no
changes. Prefer an event/sync layer (e.g. Supabase realtime or a message bus) over polling for
live inventory accuracy.

## Out of scope (this part)
- Any backend, database, API or transaction code.
- Authentication/authorization provider changes (kept local/Mock).
