# Tabs

## Overview

The Tabs component organizes content into switchable panels. It supports multiple variants including scrollable, vertical, and segmented tabs, as well as badges, closable tabs, and lazy content loading.

## Variants

| Variant | Description |
|---------|-------------|
| `standard` | Horizontal tab bar with underline indicator |
| `scrollable` | Horizontal with scroll buttons when overflow |
| `vertical` | Tabs stacked vertically on the left |
| `segmented` | Button-like tabs without underline |

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `tabs` | `Tab[]` | — | Tab definitions |
| `activeTab` | `string` | — | Currently active tab ID |
| `onChange` | `(tabId: string) => void` | — | Tab change callback |
| `variant` | `'standard' \| 'scrollable' \| 'vertical' \| 'segmented'` | `'standard'` | Visual variant |
| `lazy` | `boolean` | `true` | Lazy load tab panels |
| `destroyOnHide` | `boolean` | `false` | Unmount hidden panels |
| `className` | `string` | — | Additional CSS classes |

## Tab Interface

```ts
interface Tab {
  id: string;
  label: string;
  icon?: ReactNode;
  badge?: string | number;
  closable?: boolean;
  disabled?: boolean;
  content?: ReactNode;
}
```

## TabPanel

Individual content panel associated with a tab.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `tabId` | `string` | — | Matching tab ID |
| `children` | `ReactNode` | — | Panel content |
| `lazy` | `boolean` | — | Override lazy loading |
| `className` | `string` | — | Additional CSS classes |

## TabList

The tab bar container.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `tabs` | `Tab[]` | — | Tab definitions |
| `activeTab` | `string` | — | Active tab ID |
| `onChange` | `(id: string) => void` | — | Change handler |
| `variant` | `string` | — | Variant override |

## Badge on Tabs

Tabs display badges for notification counts. Badge color and size are configurable.

```tsx
const tabs: Tab[] = [
  { id: 'all', label: 'All', badge: 42 },
  { id: 'pending', label: 'Pending', badge: 12 },
  { id: 'completed', label: 'Completed' },
];
```

## Closable Tabs

Tabs with `closable: true` render a close icon. A close callback is triggered on click.

```tsx
const handleClose = (tabId: string) => {
  setTabs(prev => prev.filter(t => t.id !== tabId));
};
```

## Lazy Content Loading

By default, tab panels are lazily loaded — content is rendered only when the tab is first activated. The optional `destroyOnHide` prop unmounts panels when switching away.

## Keyboard Navigation

| Key | Action |
|-----|--------|
| `Arrow Right` | Next tab (horizontal) |
| `Arrow Left` | Previous tab (horizontal) |
| `Arrow Down` | Next tab (vertical) |
| `Arrow Up` | Previous tab (vertical) |
| `Home` | First tab |
| `End` | Last tab |
| `Delete` / `Backspace` | Close focused tab (if closable) |
| `Enter` / `Space` | Activate focused tab |

## ARIA Attributes

| Attribute | Usage |
|-----------|-------|
| `role="tablist"` | Tab container |
| `role="tab"` | Individual tab |
| `role="tabpanel"` | Content panel |
| `aria-selected` | Active tab state |
| `aria-controls` | Links tab to panel ID |
| `aria-labelledby` | Panel labeled by tab |
| `aria-disabled` | Disabled tab |

## Design Tokens Used

| Token | Usage |
|-------|-------|
| `--color-tab-active` | Active tab indicator |
| `--color-tab-hover` | Hover background |
| `--color-tab-disabled` | Disabled text |
| `--color-tab-badge` | Badge background |
| `--spacing-tab` | Tab padding |
| `--spacing-tab-gap` | Gap between tabs |
| `--font-size-tab` | Tab label size |
| `--font-weight-tab-active` | Active font weight |
| `--radius-tab` | Tab border radius |
