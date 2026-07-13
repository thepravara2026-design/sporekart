# Application Layout Blueprint — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1B): Enterprise Web Experience — IA & Navigation Blueprint

> **Status:** Documentation. Defines the global layout regions and their
> responsibilities. The prototype in `frontend/web-app` implements this shell
> (header, sidebar, breadcrumb bar, content area, utility panel, command
> palette, footer, skip link).

---

## 1. Layout Regions

```
┌──────────────────────────────────────────────────────────────┐
│ Skip Link (a11y, visually hidden until focused)               │
├──────────────────────────────────────────────────────────────┤
│ GLOBAL HEADER  (brand · workspace · search · quick · profile) │
├──────────┬───────────────────────────────────────────────────┤
│ WORKSPACE│ BREADCRUMB BAR (workspace › section › detail)      │
│ SIDEBAR  ├───────────────────────────────────────────────────┤
│ (workspaces│ CONTENT AREA (page title + primary action + panel)│
│ + children)│                                                   │
│           │                                                   │
│           ├───────────────────────────────────────────────────┤
│           │ UTILITY PANEL (collapsible: notifications/AI/help) │
├──────────┴───────────────────────────────────────────────────┤
│ FOOTER (public + slim global legal/status/help)               │
└──────────────────────────────────────────────────────────────┘
        ┌────────────────────────────────────────────┐
        │ COMMAND PALETTE (Cmd/Ctrl+K overlay)        │  ← above all regions
        └────────────────────────────────────────────┘
```

### 1.1 Global Header
- **Responsibility:** Persistent identity + global controls.
- **Contents:** Brand mark, current workspace indicator, global search trigger
  (⌘K), Quick Actions (+), notifications bell, profile/menu, role switcher
  (review only), AI launcher.
- **Rules:** Sticky; never scrolls away. Height fixed. One row.

### 1.2 Workspace Sidebar
- **Responsibility:** Primary navigation between and within workspaces.
- **Contents:** Workspace groups (Discover / Operate / Intelligence / Platform),
  active workspace expanded to show child sections, active item highlighted.
- **Rules:** Fixed width on desktop; collapsible to icons; on mobile becomes a
  slide-in drawer. Maximum depth 2 (workspace + children).

### 1.3 Breadcrumb Bar
- **Responsibility:** Orientation + one-step upward return.
- **Contents:** Workspace › Section › Detail trail; last item is current page
  (not a link). Home icon links to `/` from any public context.
- **Rules:** Directly below header; spans content width; semantic `<nav aria-label="Breadcrumb">`.

### 1.4 Content Area
- **Responsibility:** The task surface.
- **Contents:** Page title, optional subtitle, one primary action slot, then the
  empty/placeholder panel for the current route.
- **Rules:** Scrollable independently of header/sidebar; respects max reading
  width for prose; never shifts layout on load.

### 1.5 Utility Panel
- **Responsibility:** Collapsible side surface for notifications, AI assistant
  thread, and help — without leaving the page.
- **Contents:** Tabs (Notifications / Assistant / Help). Hidden by default on
  mobile; toggled from header.
- **Rules:** Does not replace workspace navigation; overlays or docks without
  destroying context.

### 1.6 Notification Area
- **Responsibility:** Transient + persistent alerts.
- **Contents:** Bell in header (count badge) + toast region for live events.
- **Rules:** Never blocks content; respects `prefers-reduced-motion`.

### 1.7 Profile Menu
- **Responsibility:** Account, role, sign-out, settings entry.
- **Contents:** Avatar, name, role, links to `/settings`, sign-out.
- **Rules:** Closes on blur/Escape; fully keyboard operable.

### 1.8 Command Palette
- **Responsibility:** Fast, keyboard-first jump + quick actions.
- **Contents:** Fuzzy command list (go to workspace/section, recent, favorites,
  quick actions).
- **Rules:** Modal overlay; opens on Cmd/Ctrl+K; closes on Escape/selection;
  traps focus; restores focus on close.

### 1.9 Footer
- **Responsibility:** Low-priority cross-cutting links.
- **Contents:** Legal, Privacy, Status, Help, version.
- **Rules:** Present on Public; slim global variant elsewhere; not a primary nav.

---

## 2. Region → Landmark Mapping (accessibility)

| Region | HTML Landmark |
|--------|---------------|
| Skip link | link (first focusable) |
| Global Header | `<header>` + `<nav aria-label="Global">` |
| Workspace Sidebar | `<nav aria-label="Workspaces">` |
| Breadcrumb Bar | `<nav aria-label="Breadcrumb">` |
| Content Area | `<main id="main">` |
| Utility Panel | `<aside aria-label="Utilities">` |
| Notification Area | `aria-live="polite"` region |
| Profile Menu | `<nav aria-label="Account">` |
| Command Palette | `role="dialog" aria-modal="true"` |
| Footer | `<footer>` |

---

## 3. Layout Responsibilities Summary
- Header = identity + global controls.
- Sidebar = where can I go.
- Breadcrumb = where am I / how do I go back.
- Content = what do I do here.
- Utility/Notifications/Profile = ambient support.
- Command palette = power-user shortcut.
- Footer = legal/help safety net.
