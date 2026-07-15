# Inventory Routing

## 1. Admin Mount (production)

`src/App.tsx` already wires the module into the admin tree:

```tsx
const AdminInventoryPage = lazy(() =>
  import('./admin/modules/inventory/InventoryPage').then((m) => ({ default: m.InventoryPage })),
);
// ...
<Route path="inventory" element={<AdminInventoryPage />} />
```

`InventoryPage` renders `InventoryWorkspaceProvider` + `InventoryWorkspaceLayout` with no children, so the layout drives section switching internally via `activeSection`.

## 2. Preview Mount

Added in this part:

```tsx
const InventoryPreviewApp = lazy(() =>
  import('./admin/modules/inventory/preview/InventoryPreviewApp').then((m) => ({ default: m.InventoryPreviewApp })),
);
// ...
<Route path="/preview/inventory/*" element={<InventoryPreviewApp />} />
```

`InventoryPreviewApp` is a self-contained, frontend-only exploration surface with viewport (desktop/tablet/mobile) and light/dark toggles, plus tabs:

| Tab | Component | What it shows |
|-----|-----------|---------------|
| dashboard | `InventoryPreviewDashboard` | `InventoryDashboard` inside `InventoryWorkspaceProvider` |
| workspace | `InventoryPreviewWorkspace` | `InventoryWorkspace` inside `InventoryWorkspaceProvider` |
| components | `InventoryPreviewComponents` | full component library showcase |
| navigation | `InventoryPreviewNavigation` | `InventoryWorkspaceLayout` at 3 widths |
| settings | `InventoryPreviewSettings` | `InventorySettings` inside `InventoryWorkspaceProvider` |

`InventoryPreviewApp` wraps everything in **both** `PermissionProvider` (for preview-only permission demos) and `InventoryWorkspaceProvider` (module state). Each sub-preview also wraps its content in `InventoryWorkspaceProvider` so it can be rendered standalone.

## 3. Lazy Loading / Code Splitting

Both the admin page and the preview app are `React.lazy` imports — the inventory module is not in the initial bundle. The preview tabs are static imports inside `InventoryPreviewApp`, so navigating preview tabs does not trigger additional route-level chunk loads.

## 4. Future API Mount

When Part 2 adds real routes (e.g. `/admin/inventory/items`, `/admin/inventory/warehouses`), they can be added as nested `<Route>` elements under the existing `inventory` route without changing the framework. The `INVENTORY_SECTIONS[].href` values currently point to `/admin/inventory` and should be updated to real paths as sections are implemented.
