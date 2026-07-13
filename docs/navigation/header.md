# Header

## Overview

The Header component is the top navigation bar of the Application Shell. It houses the brand/logo, primary navigation links, and action items. The header supports multiple variants for different contexts and responsive behaviors.

## Variants

| Variant | Description |
|---------|-------------|
| `primary` | Full header with brand, nav, and actions; used on most pages |
| `secondary` | Reduced header for inner pages, no nav section |
| `compact` | Slim version for dense workspaces or secondary apps |
| `transparent` | No background; overlays content; used on marketing pages |
| `sticky` | Always visible at top when scrolling |

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `variant` | `'primary' \| 'secondary' \| 'compact' \| 'transparent' \| 'sticky'` | `'primary'` | Header variant |
| `brand` | `HeaderBrandProps` | — | Brand/logo configuration |
| `nav` | `HeaderNavItem[]` | — | Navigation links |
| `actions` | `ReactNode` | — | Right-aligned actions |
| `className` | `string` | — | Additional CSS classes |
| `onMenuToggle` | `() => void` | — | Mobile menu toggle handler |

## HeaderBrand

The brand section containing logo, app name, and optional tagline.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `logo` | `string \| ReactNode` | — | Logo image URL or component |
| `name` | `string` | — | Application name |
| `tagline` | `string` | — | Optional tagline |
| `href` | `string` | `/` | Link target |
| `onClick` | `() => void` | — | Click handler |

## HeaderNav

Navigation links within the header.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `items` | `HeaderNavItem[]` | — | Navigation items |
| `activePath` | `string` | — | Current active route |
| `variant` | `'horizontal' \| 'dropdown'` | `'horizontal'` | Layout variant |

### HeaderNavItem

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `label` | `string` | — | Display text |
| `href` | `string` | — | Link target |
| `icon` | `ReactNode` | — | Leading icon |
| `children` | `HeaderNavItem[]` | — | Dropdown children |
| `badge` | `string \| number` | — | Notification badge |
| `disabled` | `boolean` | `false` | Disabled state |

## HeaderActions

Right-aligned action area for user menu, notifications, settings, etc.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `children` | `ReactNode` | — | Action components |
| `className` | `string` | — | Additional CSS classes |

## States

### Default

Standard header with brand, navigation, and actions.

### Scrolled

When the page is scrolled past a threshold, the header may add shadow/elevation.

| Property | Default State | Scrolled State |
|----------|--------------|----------------|
| `box-shadow` | `none` | `--elevation-level-1` |
| `background` | `--color-surface` | `--color-surface` (opaque) |
| `border-bottom` | `1px solid --color-border` | `1px solid --color-border` |

### Mobile

On screens below `md` breakpoint, navigation collapses into a hamburger menu.

## Responsive Behavior

| Breakpoint | Behavior |
|------------|----------|
| `xs`, `sm` | Mobile: hamburger menu, compact brand, stacked actions |
| `md+` | Desktop: horizontal nav, full brand, inline actions |

## Design Tokens Used

| Token | Usage |
|-------|-------|
| `--layout-header-height` | Header height |
| `--layout-header-height-compact` | Compact header height |
| `--color-surface` | Header background |
| `--color-border` | Header bottom border |
| `--elevation-level-1` | Scrolled shadow |
| `--spacing-header-x` | Horizontal padding |
| `--spacing-header-y` | Vertical padding |
| `--font-size-header-brand` | Brand text size |
| `--font-weight-header-nav` | Nav link weight |
