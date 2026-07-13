# Navigation Strategy — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1B): Enterprise Web Experience — IA & Navigation Blueprint

> **Status:** Documentation. Defines the navigation philosophy and the navigation
> types used across the product. Reuses the Experience Principles (Part 1A).

---

## 1. Navigation Philosophy

Navigation must be **Predictable, Minimal, Context-aware, Search-friendly,
Keyboard accessible, Responsive, Role-aware, Future-proof**.

- **Predictable** — the sidebar, header, and breadcrumbs use one consistent
  grammar in every workspace.
- **Minimal** — one primary action per view; secondary actions recede.
- **Context-aware** — the active workspace, role, and record scope the chrome.
- **Search-friendly** — global search + command palette are always one keystroke away.
- **Keyboard accessible** — every target reachable without a mouse.
- **Responsive** — same mental model at every breakpoint.
- **Role-aware** — the navigation tree is filtered by role on load.
- **Future-proof** — adding a page is a config change, not a restructure.

**Click budget:** any primary task is reachable in ≤ 3 navigations from the
landing state of the active workspace.

---

## 2. Navigation Types

| Type | Where | When used | Example |
|------|-------|-----------|---------|
| **Global Navigation** | Persistent sidebar (desktop) / bottom or hamburger (mobile) | Moving between workspaces | Switching Public → Orders |
| **Workspace Navigation** | Sidebar section list (children of active workspace) | Moving within a workspace | Orders → Order detail |
| **Context Navigation** | In-content links, related-record links | Jumping across workspaces with context | Customer order links into Orders detail |
| **Breadcrumb Navigation** | Breadcrumb bar, below header | Orienting + one-step return upward | Orders › ORD-123 → Orders |
| **Action Navigation** | Primary/secondary buttons, floating action | Performing the task on the current page | "Fulfill" on an order |
| **Footer Navigation** | Footer (public + lightweight legal) | Low-priority, cross-cutting links | Terms, Privacy, Status |
| **Command Palette Navigation** | Cmd/Ctrl+K overlay | Power users jumping anywhere fast | "Go to Governance › Policies" |
| **Quick Actions** | Command palette + header shortcut | Frequent tasks regardless of location | New order, New product, Ask AI |

### 2.1 Global Navigation
The left sidebar lists the 12 workspaces. Order is fixed and grouped:
**Discover** (Public, Customer), **Operate** (Orders, Products, Training),
**Intelligence** (AI, Governance, Analytics), **Platform** (Admin, CMS, Support,
Settings). Grouping is visual only — it does not add a nesting level.

### 2.2 Workspace Navigation
Under the active workspace, the sidebar shows its child sections. Only direct
children (depth 2). Detail routes are reached by selecting a record in a list,
never by a permanent sidebar item (keeps the tree flat).

### 2.3 Context Navigation
Cross-workspace links carry the user and their context (e.g., a support ticket
links to the exact order). These are rendered as standard links with clear
labels, never as silent redirects.

### 2.4 Breadcrumb Navigation
Always present except on the workspace root itself (where it collapses to the
workspace name). See `breadcrumb-guidelines.md`.

### 2.5 Action Navigation
One primary action per view, visually dominant (Principle 1). The prototype shows
empty action slots in each placeholder panel so the pattern is visible.

### 2.6 Footer Navigation
Present on Public pages and as a slim global footer. Contains legal, status, and
help links. Not a primary navigation surface.

### 2.7 Command Palette Navigation
Triggered by `Cmd/Ctrl+K`. Provides fuzzy jump to any workspace, section, or
recent/favorite item, plus quick actions. Architecture only this sprint; the
prototype wires the open/close shell and lists static commands.

### 2.8 Quick Actions
A small set of high-frequency tasks available from the header (+) and the command
palette, independent of the current workspace.

---

## 3. Default Landing by Role (post-auth redirect)

| Role | Lands on |
|------|----------|
| Guest | `/` |
| Customer / Farmer | `/account` |
| Grower | `/catalog` |
| Trainer | `/training` |
| Distributor | `/orders` |
| Support | `/support` |
| Administrator | `/admin` |
| Business Owner | `/analytics` |
| Governance Manager | `/governance` |

---

## 4. Navigation State & Return

- **Where am I?** Active workspace highlighted in sidebar; workspace name in
  breadcrumb root; page title in content header.
- **Where can I go?** Visible sidebar + breadcrumb + command palette.
- **What can I do?** Primary action slot + quick actions.
- **How do I return?** Breadcrumb up-level + persistent sidebar; browser back
  always works (no wizard traps).
- **Which workspace?** Workspace name + icon pinned in header and sidebar.
