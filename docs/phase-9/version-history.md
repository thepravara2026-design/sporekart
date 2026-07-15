# Version History

## Purpose
Give administrators a safe, auditable view of how a product has evolved, entirely in Mock Mode.

## Data Model
```ts
interface ProductVersion {
  id: string;
  version: number;
  status: ProductLifecycleState;
  modifiedBy: string;
  createdAt: string;
  reason?: string;
  summary?: string;
  data: ProductWizardData;   // full snapshot
}
```
`mockVersions` (3 entries) lives in `editing/mockEditData.ts`.

## UI — `VersionHistory.tsx`
- Sorted newest-first list with version #, date, modified-by, status `StatusBadge`, reason + summary.
- **Preview**: inline `dl` preview card (name, SKU, status, price, MRP, meta title, summary) toggled via `previewVersion(id)`.
- **Restore** (`can('restore')`): loads the version's `data` into the working draft and logs `restored`.
- **Duplicate** (`can('update')`): appends a copy version and logs `duplicated`.
- Implemented with `React.memo` for performance.

## Wiring
`useProductEditState` supplies `versions`, `previewVersionId`, `previewVersion`, `restoreVersion`, `duplicateVersion`. All actions are mock — no persistence.

## Routes
- In-workspace: `history` section (also embeds the Compare selector).
- Standalone: `/preview/products/history`.

## Future Backend Integration
Replace `mockVersions` with a paginated version API; `restoreVersion` becomes a PATCH/POST to a version endpoint; add diff-delays and author avatars. The component already renders full snapshots, so field-level diffs can be layered on top of `CompareView`.
