# Inventory Workspace

`InventoryWorkspaceLayout` is the module-local shell that renders all inventory sections. It owns its own in-module navigation and does **not** modify the global admin sidebar.

## 1. Structure

```
<InventoryWorkspaceProvider>
  <InventoryWorkspaceLayout>            // renders header, breadcrumbs, search, banner, sidebar, content, footer
    (content swapped by activeSection)
  </InventoryWorkspaceLayout>
</InventoryWorkspaceProvider>
```

## 2. Header

- Left: module icon (`archive`) + title "Inventory" + subtitle.
- Right: a **role switcher** (`select`) bound to `setRole`, a **Settings** button (`setActiveSection('settings')`), and a **Help** button (`setActiveSection('help')`).

## 3. Breadcrumbs

`Admin / Inventory / <active section label>` — plain `nav[aria-label="Breadcrumb"]`, token-styled. No dependency on the global `Breadcrumb` component (kept self-contained for the foundation).

## 4. Search + Banner

- `SearchComponent` (bound to `searchQuery` / `setSearchQuery`, field toggles against `INVENTORY_SEARCH_FIELDS`).
- `WorkspaceBanner` (info) acknowledging sync state.

## 5. Sidebar / Navigation

- Desktop (≥1024px): a left vertical nav listing all 15 `INVENTORY_SECTIONS`; the active item gets `aria-current="page"` and primary styling.
- Mobile (<1024px): the same nav renders as a top horizontal scroll region (flex-direction switches via `useInventoryResponsive().isMobile`).

## 6. Content Region

`renderContent()` switches on `activeSection`:

- `overview` → `InventoryOverviewPage`
- `dashboard` → `InventoryDashboardPage`
- `settings` → `InventorySettingsPage`
- anything else → `SectionPlaceholder` (icon + label + description + "Extensible placeholder" badge)

When used in the **preview** app, the layout also accepts `children` and renders them instead of `renderContent()` (so the navigation preview can show static placeholder content at multiple widths).

## 7. Activity Feed + Footer

- A right-aligned `aside` labelled "Activity feed" (placeholder text "Activity feed coming soon").
- A footer with "SporeKart Inventory · Mock Mode" and a non-functional Profile control.

## 8. Pages

- `InventoryOverviewPage` — `SectionHeader` + `StatisticsGrid` (6 metrics) + Quick Actions grid + Sample Inventory `InventoryTable` + `RecentActivityCard`.
- `InventoryDashboardPage` → `InventoryDashboard` — `SectionHeader` + success banner + `StatisticsGrid` (metrics/health/status) + gated Analytics `SummaryCard` (`can('reports')`) + `RecentActivityCard`.
- `InventorySettingsPage` → `InventorySettings` — section nav + toggle groups + `units` list; gated by `PermissionPlaceholder permission="settings"`.
