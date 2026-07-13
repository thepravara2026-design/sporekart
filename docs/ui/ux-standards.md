# UX Standards — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1C): UX Standards & Accessibility Foundation

> **Status:** Mandatory principles. Every screen, component, and workflow must justify decisions against these. Reuses the 10 Experience Principles from Part 1A as the foundation.

---

## 1. The 11 UX Principles (Extended)

| # | Principle | Definition | Application Rule |
|---|-----------|------------|------------------|
| 1 | **Recognition over Recall** | Users shouldn't have to remember information across steps. | Visible context, breadcrumbs, recent items, labeled actions. No hidden state. |
| 2 | **Progressive Disclosure** | Show only what's needed now; reveal complexity on demand. | One primary action per view; advanced options in overflow/menu; collapsible sections. |
| 3 | **Minimal Cognitive Load** | Reduce mental effort per task. | Chunk forms, smart defaults, inline help, clear hierarchy, no jargon. |
| 4 | **Consistency** | Same patterns, same language, same behavior everywhere. | Frozen design system (Part 1D); shared components; global keyboard map. |
| 5 | **Clear Hierarchy** | Eye knows what's first, second, third instantly. | Type scale, spacing scale, one primary action, visual weight = importance. |
| 6 | **Forgiveness** | Errors are recoverable; actions reversible where possible. | Undo toasts, confirmation for destructive, draft auto-save, clear back paths. |
| 7 | **Feedback** | Every action has immediate, appropriate response. | Loading states, optimistic UI, success toasts, error inline, focus moves. |
| 8 | **Discoverability** | Features findable without training. | Command palette, tooltips, empty state CTAs, contextual help, search. |
| 9 | **Trust** | Transparent, honest, secure by design. | Prices before commit, source citations, no dark patterns, clear permissions. |
| 10 | **Accessibility** | Usable by everyone without special modes. | WCAG 2.2 AA baseline (see accessibility-standards.md). |
| 11 | **Performance Perception** | Fast feels fast; slow feels honest. | Skeletons > spinners, optimistic UI, streaming, budgets (see performance-budgets.md). |

**Conflict Resolution:** Accessibility (10) & Trust (9) > Clarity (3) & Recognition (1) > Consistency (4) & Performance (11) > Progressive Disclosure (2) > Hierarchy (5) > Forgiveness (6) > Feedback (7) > Discoverability (8).

---

## 2. Application Rules (Per Principle)

### Recognition over Recall
- **Breadcrumbs** always visible (Part 1B).
- **Recent items** in command palette + workspace home.
- **Context preserved** on navigation (scroll, filters, form drafts).
- **Labels over icons** — icons always have text labels or tooltips.

### Progressive Disclosure
- **Default view:** Primary content + one primary action.
- **Secondary actions:** Overflow menu (⋮) or "More" button.
- **Advanced settings:** Collapsible section, default closed.
- **Power-user features:** Command palette, keyboard shortcuts.

### Minimal Cognitive Load
- **Forms:** One column, logical groups, smart defaults, inline validation.
- **Tables:** Sortable, filterable, paginated; column visibility toggle.
- **Empty states:** Explain → Guide → Act (see empty-state-guidelines.md).
- **Numbers:** Formatted with units, separators, precision appropriate to context.

### Consistency
- **Component library frozen** (Part 1D) — no ad-hoc components.
- **Naming:** Same term everywhere (e.g., "Order" not "Purchase" / "Transaction").
- **Icons:** Single icon set, semantic meaning documented.
- **Spacing:** 4px base scale only.
- **Motion:** 150–300ms, ease-out, respects reduced-motion.

### Clear Hierarchy
- **Type scale:** H1 (32), H2 (24), H3 (20), Body (16), Small (14), Micro (12).
- **Weight:** Primary action = 600; Secondary = 400; Disabled = 400 + opacity.
- **Color:** Semantic only (see Part 1D tokens).
- **Whitespace:** Groups related; separates unrelated.

### Forgiveness
- **Destructive actions:** Confirmation dialog with typed confirmation for bulk.
- **Undo:** Toast with "Undo" for 5s after delete/archive/send.
- **Drafts:** Auto-save every 2s (debounced); recover on return.
- **Back navigation:** Browser back always works; no wizard traps.

### Feedback
| Trigger | Response | Timing |
|---------|----------|--------|
| Button click | Ripple/press state + aria-busy | < 50ms |
| Navigation | Skeleton → content | Skeleton immediate |
| Form submit | Inline spinner on button + disabled | < 100ms |
| Success | Toast (green) + undo if applicable | 3–5s auto-dismiss |
| Error | Inline (red) + toast (critical) | Persistent until fix |
| Background sync | Subtle indicator in header | Non-blocking |

### Discoverability
- **Command palette:** `Cmd/Ctrl+K` — all navigations + quick actions.
- **Tooltips:** On icon-only buttons, truncated text, advanced features.
- **Empty states:** Primary CTA to create/import.
- **Contextual help:** `?` icon → popover with link to KB.
- **Search:** Global, fuzzy, scoped to workspace.

### Trust
- **Pricing:** Show total before payment step.
- **Data source:** "Source: Lab analysis, 2026-06-15" on recommendations.
- **Permissions:** "You can see this because you're a Distributor."
- **No dark patterns:** No pre-checked upsells, no hidden fees, no forced continuity.

### Performance Perception
- **Skeleton first** — never blank white flash.
- **Streaming** — SSR shell + Suspense boundaries for data.
- **Optimistic** — Mutations update UI before server response.
- **Budgets** — Enforced in CI (see performance-budgets.md).

---

## 3. Screen-Level Checklist (Every Page)

Before Gate 3, every page must confirm:
- [ ] One H1, logical heading hierarchy
- [ ] One primary action (visually dominant)
- [ ] Breadcrumbs match IA
- [ ] Loading skeleton matches final layout
- [ ] Empty state follows Explain-Guide-Act
- [ ] Error state inline + toast, recoverable
- [ ] Focus order matches visual order
- [ ] All interactive elements keyboard reachable
- [ ] Color not sole conveyor of meaning
- [ ] Text resizes to 200% without horizontal scroll
- [ ] Reduced-motion respected
- [ ] Performance budget met (Lighthouse CI)
- [ ] axe-core: 0 violations