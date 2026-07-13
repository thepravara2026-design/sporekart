# Component Freeze Policy — SporeKart Enterprise AI Platform
## Phase 5 / Sprint 19 (Part 1A): Enterprise Product Experience Foundation

> **Status:** Documentation Only. Once approved, components are frozen and become
> the Enterprise Design System. Future pages MUST reuse them.

---

## 1. Purpose

To guarantee a consistent, premium, accessible experience across every page and
every future platform (web, Android, iOS, React Native), we freeze approved UI
components. Frozen components are the single source of truth for interface
building blocks.

---

## 2. Frozen Component Categories

The following component families are in scope for freezing:

- **Buttons** — primary, secondary, tertiary, icon, loading, destructive.
- **Cards** — product, info, stat, list-item, dashboard.
- **Inputs** — text, select, search, date, number, toggle, checkbox, radio.
- **Navigation** — top bar, side nav, tabs, breadcrumbs, pagination.
- **Dialogs** — modal, confirmation, sheet, alert.
- **Tables** — data table, sortable, expandable, bulk-action.
- **Charts** — line, bar, donut, stat, sparkline (accessible by default).
- **Forms** — field groups, validation, submit, multi-step.
- **Layout** — page shell, grid, container, stack, divider, spacing primitives.

Each frozen component ships with: usage guidelines, accessibility notes, token
bindings, and responsive behavior.

---

## 3. Freeze Process

1. Component is designed and implemented following the design system and tokens.
2. Component passes **Gate 3 (Implementation Review)** and **Gate 4 (Final
   Approval)** via a page that uses it.
3. On approval, the component is marked **Frozen** in the design system
   registry.
4. Its API, tokens, and behavior are versioned and locked.
5. Future pages reference the frozen component; deviation requires a proposal
   (see Section 5).

---

## 4. Versioning the Design System

- The design system is versioned with semantic versioning (e.g., `v1.0.0`).
- **Major:** breaking change to a frozen component's API or behavior.
- **Minor:** new frozen component or non-breaking enhancement.
- **Patch:** bug fix, token correction, documentation.
- Every freeze/update is recorded with date, author, and rationale.
- Mobile platforms map the same components to native equivalents, preserving
  behavior and tokens.

---

## 5. Proposing a New Component

If a need cannot be met by frozen components:

1. Open a **Component Proposal** referencing the gap and the affected page brief.
2. Design reviews against philosophy and principles (no duplication of existing
   components).
3. If accepted, the component goes through the full 15-step workflow and the
   four gates.
4. On approval, it is added to the frozen set and the design system version is
   bumped (usually Minor).
5. Until frozen, the new component may not be reused by other pages.

> Reuse before create. Most "new" needs are solved by composing existing frozen
> components differently.

---

## 6. Enforcement

- Eng rejects PRs introducing unapproved components or diverging from frozen
  APIs (caught in Gate 3 and CI).
- Design System team owns the registry and audits compliance (`style-governance.md`).
- Exceptions require recorded approval from CPO + CXO + Design.
