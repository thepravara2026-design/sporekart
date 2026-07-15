# Storage Hierarchy

Warehouses organise physical and logical storage as a strict 7-level tree:

```
Warehouse → Building → Floor → Zone → Rack → Shelf → Bin
```

## StorageNode
```ts
interface StorageNode {
  id: string;
  parentId: string | null;   // null = root warehouse node
  level: 'warehouse' | 'building' | 'floor' | 'zone' | 'rack' | 'shelf' | 'bin';
  code: string;
  name: string;
  status: StorageNodeStatus; // active | inactive | maintenance | planned | reserved
  description?: string;
  capacity?: number;
}
```

The mock service (`generateStorageNodes`) builds one demo hierarchy rooted at `SN-WH1` with two
buildings, two floors each, two zones, racks, shelves and bins per branch — 127 nodes — to exercise
the tree renderer.

## StorageHierarchy component
`components/StorageHierarchy.tsx` renders an expandable/collapsible tree:
- Builds a `childrenMap` (`parentId → nodes[]`) via `useMemo`.
- Roots (`parentId === null`) render first; each node recurses into its children.
- Levels 0–2 are expanded by default; deeper levels start collapsed.
- Each row shows the level icon, `code`, `name`, a level tag and a `StatusBadge`.

Used by:
- `WarehouseStoragePage` (full hierarchy + level counts)
- `WarehouseProfilePage` (hierarchy for the selected warehouse)

## Zone vs StorageNode
`Zone` is a distinct concept tied to a `warehouseId` and a `ZoneType`
(receiving, storage, packing, dispatch, returns, qc, damaged, cold, quarantine). Zones are listed
in `WarehouseZonesPage` and counted per type. The `Rack`/`Shelf`/`Bin` types exist for future
operational detail but are currently represented inside the `StorageNode` tree.
