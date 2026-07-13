# Menu System

## Overview

The Menu System provides five menu types for different interaction patterns: dropdown menus, context menus, overflow menus, user menus, and action menus. All menus share the same `MenuItem` component and adhere to WAI-ARIA menu patterns.

## Menu Types

### DropdownMenu

Standard dropdown triggered by a button or clickable element.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `trigger` | `ReactNode` | — | Element that opens the menu |
| `items` | `MenuItem[]` | — | Menu items |
| `align` | `'start' \| 'end'` | `'start'` | Dropdown alignment |
| `nestedSubmenus` | `boolean` | `true` | Enable nested submenus |
| `className` | `string` | — | Additional CSS classes |

### ContextMenu

Right-click context menu.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `items` | `MenuItem[]` | — | Menu items |
| `onContext` | `(e: MouseEvent) => void` | — | Custom context handler |
| `className` | `string` | — | Additional CSS classes |

### OverflowMenu

Kebab (three dots) menu for compact action overflow.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `items` | `MenuItem[]` | — | Menu items |
| `className` | `string` | — | Additional CSS classes |
| `direction` | `'horizontal' \| 'vertical'` | `'vertical'` | Kebab dots orientation |

### UserMenu

Avatar-triggered menu for user profile, settings, and sign out.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `user` | `UserData` | — | User information |
| `items` | `MenuItem[]` | — | Menu items |
| `avatar` | `ReactNode` | — | Custom avatar override |

### ActionMenu

Vertical action list, typically used in sidebars or panels.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `items` | `MenuItem[]` | — | Action items |
| `variant` | `'default' \| 'compact'` | `'default'` | Size variant |
| `className` | `string` | — | Additional CSS classes |

## MenuItem Component

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `label` | `string` | — | Display text |
| `icon` | `ReactNode` | — | Leading icon |
| `shortcut` | `string` | — | Keyboard shortcut text |
| `disabled` | `boolean` | `false` | Disabled state |
| `danger` | `boolean` | `false` | Destructive action styling |
| `divider` | `boolean` | `false` | Render as section divider |
| `children` | `MenuItem[]` | — | Nested submenu items |
| `onClick` | `() => void` | — | Click handler |
| `checked` | `boolean` | — | Checked state (for checkable items) |
| `type` | `'default' \| 'checkbox' \| 'radio'` | `'default'` | Item type |

## Keyboard Navigation

| Key | Action |
|-----|--------|
| `Arrow Up` / `Arrow Down` | Navigate items |
| `Arrow Right` | Open submenu |
| `Arrow Left` | Close submenu |
| `Enter` / `Space` | Activate item |
| `Escape` | Close menu |
| `Home` | First item |
| `End` | Last item |

## ARIA Roles

| Role | Usage |
|------|-------|
| `role="menu"` | Menu container |
| `role="menuitem"` | Menu item |
| `role="menuitemcheckbox"` | Checkable menu item |
| `role="menuitemradio"` | Radio menu item |
| `role="menubar"` | Horizontal menu bar |
| `aria-haspopup="true"` | Element with submenu |
| `aria-expanded` | Open/close state |
| `aria-disabled` | Disabled item |

## Design Tokens Used

| Token | Usage |
|-------|-------|
| `--color-menu-bg` | Menu background |
| `--color-menu-text` | Text color |
| `--color-menu-hover` | Hover background |
| `--color-menu-danger` | Danger item color |
| `--color-menu-divider` | Divider color |
| `--color-menu-disabled` | Disabled item color |
| `--spacing-menu-item` | Item padding |
| `--radius-menu` | Menu border radius |
| `--elevation-menu` | Menu shadow |
| `--font-size-menu` | Item font size |
| `--font-size-menu-shortcut` | Shortcut font size |
