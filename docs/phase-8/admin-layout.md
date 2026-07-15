# Admin Layout

## Structure

The admin layout uses the design system's `AuthenticatedLayout` which provides the sidebar + header + content area grid.

### Desktop Layout
```
┌─────────────────────────────────────────────────┐
│ Header (56px) · [Brand] [── TopNav ──]         │
├────────┬────────────────────────────────────────┤
│        │  PageHeader (title + description)      │
│Sidebar │  Breadcrumb                            │
│ 264px  │  ┌─────────────────────────────────┐   │
│ (can   │  │ Content Area (<Outlet />)       │   │
│ collapse│  │                                 │   │
│ to 72px)│  │                                 │   │
│        │  └─────────────────────────────────┘   │
│        │  PageFooter                            │
└────────┴────────────────────────────────────────┘
```

### Tablet Layout (768px–1024px)
- Sidebar collapses to icon-only (72px) by default
- Header shows compact navigation
- Content fills remaining width

### Mobile Layout (<768px)
- Sidebar becomes overlay drawer (triggered by hamburger)
- Full-width content
- Stacked layout for dashboard widgets

## Configuration

The layout is configured in `AdminLayout.tsx`:
- Sidebar collapse state managed via `useState`
- Mobile drawer open/close state
- Navigation handlers for sidebar and top nav
- Breadcrumb generation from pathname

## Responsive Breakpoints

| Breakpoint | Sidebar | Header | Content |
|---|---|---|---|
| >1024px | Expanded (264px), collapsible | Full nav | Multi-column |
| 768–1024px | Collapsed (72px) | Compact nav | Multi-column |
| <768px | Overlay drawer | Hamburger + brand | Single column |
