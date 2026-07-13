# Sidebar

## Overview

The Sidebar is the primary workspace-level navigation component. It displays hierarchical navigation items, supports collapsible groups, pinned items, and responsive overlay behavior on mobile.

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `items` | `SidebarItemData[]` | — | Navigation items |
| `variant` | `'primary' \| 'mini'` | `'primary'` | Sidebar display variant |
| `collapsed` | `boolean` | `false` | Collapsed state |
| `pinned` | `boolean` | `false` | Pin sidebar open |
| `onToggle` | `(collapsed: boolean) => void` | — | Collapse toggle callback |
| `onPin` | `(pinned: boolean) => void` | — | Pin toggle callback |
| `activePath` | `string` | — | Currently active route path |
| `className` | `string` | — | Additional CSS classes |

## SidebarItemData Interface

```ts
interface SidebarItemData {
  id: string;
  label: string;
  href?: string;
  icon?: ReactNode;
  badge?: string | number;
  disabled?: boolean;
  pinned?: boolean;
  children?: SidebarItemData[];
}
```

## Sub-Components

### SidebarNav

Renders the navigation tree from `items` array.

### SidebarGroup

A collapsible group of navigation items with a section title.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `title` | `string` | — | Group heading |
| `icon` | `ReactNode` | — | Group icon |
| `defaultExpanded` | `boolean` | `true` | Initial expand state |
| `children` | `ReactNode` | — | Child items |

### SidebarItem

A single navigation link.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `item` | `SidebarItemData` | — | Item data |
| `depth` | `number` | `0` | Nesting depth |
| `active` | `boolean` | — | Is current route |
| `onClick` | `() => void` | — | Click handler |

### SidebarToggle

Button to collapse/expand and pin the sidebar.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `collapsed` | `boolean` | — | Current state |
| `pinned` | `boolean` | — | Pinned state |
| `onToggle` | `() => void` | — | Toggle handler |
| `onPin` | `() => void` | — | Pin handler |

## Variants

### Primary

Full-width sidebar with labels, icons, groups, and nested items. Width: `--layout-sidebar-width` (default 280px).

### Mini

Collapsed sidebar showing only icons. Width: `--layout-sidebar-collapsed-width` (default 64px). Tooltips reveal labels on hover.

## Collapsible Behavior

- Collapse: Sidebar reduces to mini variant
- Hover on mini: Temporarily expands to show labels
- Pin: Locks sidebar in expanded state even after mouse leaves

## Nested Navigation

Supports up to 3 levels of nesting:

```
Workspace (Level 0)
├── Section (Level 1)
│   ├── Page (Level 2)
│   └── Detail (Level 3)
└── Section (Level 1)
```

## Pinned Items

Items with `pinned: true` appear at the top of the sidebar in a "Pinned" section, regardless of their position in the navigation tree.

## Responsive Behavior

| Breakpoint | Behavior |
|------------|----------|
| `xs`, `sm` | Overlay drawer: sidebar slides in from left, backdrop overlay |
| `md` | Mini by default, can expand |
| `lg+` | Full sidebar, collapsible via toggle |

## Keyboard Navigation

| Key | Action |
|-----|--------|
| `Tab` / `Shift+Tab` | Navigate between items |
| `Arrow Up` / `Arrow Down` | Move within list |
| `Arrow Right` | Expand collapsed group |
| `Arrow Left` | Collapse expanded group |
| `Enter` | Activate link |
| `Space` | Toggle group expand/collapse |
| `Escape` | Close mobile overlay |

## Design Tokens Used

| Token | Usage |
|-------|-------|
| `--layout-sidebar-width` | Expanded width (280px) |
| `--layout-sidebar-collapsed-width` | Collapsed width (64px) |
| `--color-sidebar-bg` | Background |
| `--color-sidebar-text` | Text color |
| `--color-sidebar-active` | Active item background |
| `--color-sidebar-hover` | Hover state background |
| `--color-sidebar-pinned` | Pinned section indicator |
| `--spacing-sidebar-item` | Item padding |
| `--radius-sidebar-item` | Item border radius |
| `--elevation-sidebar` | Sidebar shadow |
