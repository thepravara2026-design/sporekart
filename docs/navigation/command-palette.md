# Command Palette

## Overview

The CommandPalette provides a global search overlay for navigating the application, executing commands, and accessing recent/favorite pages. It is triggered by `Ctrl/Cmd + K` or a dedicated button.

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `open` | `boolean` | — | Visible state |
| `onClose` | `() => void` | — | Close callback |
| `groups` | `CommandGroup[]` | — | Command groups |
| `placeholder` | `string` | `'Search pages, commands, and actions...'` | Search placeholder |
| `emptyMessage` | `string` | `'No results found'` | Empty state message |
| `recentItems` | `CommandItem[]` | — | Recently used items |
| `keyboardShortcut` | `string` | `'Ctrl+K'` | Shortcut display text |
| `maxDisplayedItems` | `number` | `10` | Max results per group |
| `className` | `string` | — | Additional CSS classes |

## CommandItem Interface

```ts
interface CommandItem {
  id: string;
  label: string;
  description?: string;
  icon?: ReactNode;
  shortcut?: string;
  group?: string;
  href?: string;
  action?: () => void;
  keywords?: string[];
}
```

## CommandGroup Interface

```ts
interface CommandGroup {
  id: string;
  label: string;
  items: CommandItem[];
}
```

## Keyboard Shortcut

The command palette is opened globally with `Ctrl/Cmd + K`. The shortcut is registered on mount and cleaned up on unmount. Custom shortcuts can be configured.

## Search/Filter Behavior

- Filters items across all groups by matching `label`, `description`, and `keywords`
- Case-insensitive matching
- Results grouped by their `group` property
- Recent items shown when search query is empty
- Max results per group controlled by `maxDisplayedItems`

## Keyboard Navigation

| Key | Action |
|-----|--------|
| `Ctrl/Cmd + K` | Open palette |
| `Escape` | Close palette |
| `Arrow Up` / `Arrow Down` | Navigate results |
| `Enter` | Execute selected item |
| `Tab` | Cycle through groups |

## Groups and Items

Commands are organized into groups for logical separation:

```tsx
const groups: CommandGroup[] = [
  {
    id: 'pages',
    label: 'Pages',
    items: [
      { id: 'dashboard', label: 'Dashboard', href: '/dashboard', icon: <DashboardIcon /> },
      { id: 'orders', label: 'Orders', href: '/orders', icon: <OrdersIcon /> },
    ],
  },
  {
    id: 'actions',
    label: 'Actions',
    items: [
      { id: 'new-order', label: 'Create New Order', action: () => openNewOrder(), shortcut: 'Ctrl+N' },
    ],
  },
];
```

## No Results State

When no items match the search query, the palette displays the `emptyMessage` with optional illustration.

## Accessibility

| Attribute | Usage |
|-----------|-------|
| `role="dialog"` | Dialog role |
| `aria-modal="true"` | Modal state |
| `aria-labelledby` | Referenced by palette title |
| `role="listbox"` | Results container |
| `role="option"` | Individual result |
| `aria-selected` | Current selection |
| `aria-activedescendant` | Active option ID |
| `aria-placeholder` | Search input placeholder |

## Design Tokens Used

| Token | Usage |
|-------|-------|
| `--color-palette-bg` | Overlay background |
| `--color-palette-surface` | Dialog surface |
| `--color-palette-text` | Text color |
| `--color-palette-highlight` | Highlighted search text |
| `--color-palette-selected` | Selected item background |
| `--color-palette-hover` | Hover background |
| `--color-palette-muted` | Secondary/description text |
| `--spacing-palette` | Internal spacing |
| `--radius-palette` | Border radius |
| `--elevation-palette` | Dialog shadow |
