# Admin Navigation

## Navigation Configuration

All admin navigation is configuration-driven via `src/admin/config/adminNavigation.tsx`.

### Sidebar Items (`ADMIN_SIDEBAR_ITEMS`)
Typed as `SidebarItemData[]` with support for:
- `id` — unique identifier
- `label` — display text
- `href` — route path
- `icon` — ReactNode (uses `Icon` component)
- `children` — nested sub-items (for future use)
- `badge` — notification count
- `disabled` — disable state

### Top Nav Items (`ADMIN_TOP_NAV`)
Typed as `TopNavItem[]` with same property pattern.

### Breadcrumb Builder (`buildAdminBreadcrumbs`)
Generates `Crumb[]` from the current pathname with:
- Root crumb: "Admin" linking to `/admin/dashboard`
- Path-segment-based crumbs
- Support for dynamic titles via a label map

### Active ID Resolver (`getAdminActiveId`)
Extracts the active section from the pathname for sidebar highlighting.

## Role-Aware Navigation

The navigation config is separate from the existing `Role` system. Role-based filtering will be added in Sprint 23 Part 2 when real RBAC is integrated. The sidebar currently renders all items for the active administrator role.

## Future Navigation Features
- Search/filter in sidebar
- Pinned/favorite items
- Recent pages section
- Command palette integration
- Nested/collapsible groups for complex hierarchies
