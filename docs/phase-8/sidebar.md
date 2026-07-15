# Admin Sidebar

## Implementation

The admin sidebar uses the design system's `Sidebar` component with the following configuration:

```tsx
<Sidebar
  items={ADMIN_SIDEBAR_ITEMS}     // Config-driven items
  variant="primary"               // Primary variant
  collapsed={sidebarCollapsed}    // Controlled collapse
  onCollapse={setSidebarCollapsed}
  onNavigate={handleSidebarNavigate}
  activeId={activeId}            // Active item highlighting
  header={...}                    // Brand logo + name
  footer={...}                    // Version info
  responsive                      // Mobile drawer mode
  open={sidebarOpen}              // Mobile drawer state
  onClose={handleSidebarClose}
/>
```

## Features

### Collapsible
- Button in sidebar header to toggle between expanded (264px) and collapsed (72px icon-only)
- Smooth CSS transition
- Maintains active state across modes

### Mobile Drawer
- Activated when viewport < 768px
- Sidebar slides in from left as overlay
- Semi-transparent backdrop
- Close on navigation or backdrop click
- Hamburger button in header to open

### Nested Navigation
- Items support `children: SidebarItemData[]` for hierarchical nav
- Expandable/collapsible sub-items
- Automatic keyboard navigation (ArrowDown, ArrowUp, Enter, Home, End)

### Pin/Favorites Support
- Pin button appears on hover
- Track pinned items via `pinnedIds` and `onTogglePin`
- Visual indicator for pinned items

### Accessibility
- `role="navigation"` with `aria-label`
- Menuitem roles with `aria-current` for active state
- Full keyboard navigation
- Focus management
- Reduced motion support via CSS variables
