# Accessibility Standards — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1C): UX Standards & Accessibility Foundation

> **Status:** WCAG 2.2 AA mandatory. Every component, page, and workflow must pass automated + manual a11y audit before Gate 3. Reuses Principle 5 (Accessibility by Default) from Part 1A.

---

## 1. Conformance Target

| Standard | Level | Scope |
|----------|-------|-------|
| WCAG 2.2 | AA | All web content |
| EN 301 549 | — | EU public procurement alignment |
| Section 508 | — | US federal alignment |
| ARIA 1.2 | — | All custom components |

**Non-negotiable:** No AA exceptions. AAA targeted for: contrast (text), focus appearance, target size, motion reduction.

---

## 2. Semantic HTML Requirements

| Element | Use Case | Required Attributes |
|---------|----------|---------------------|
| `<header>` | Global header | `role="banner"` (implicit) |
| `<nav>` | Sidebar, breadcrumb, footer | `aria-label="Workspaces"` / `aria-label="Breadcrumb"` |
| `<main>` | Content area | `id="main"`, `tabIndex=-1` for skip link |
| `<aside>` | Utility panel | `aria-label="Utilities"` |
| `<footer>` | Global footer | `role="contentinfo"` |
| `<section>` | Page regions | `aria-labelledby="heading-id"` |
| `<article>` | Self-contained content (card) | — |
| `<dialog>` | Modals, palette | `aria-modal="true"`, `role="dialog"` |
| `<button>` | All clickable actions | `type="button"` (default), `aria-pressed`, `aria-expanded` |
| `<a>` | Navigation only | `href` required; never `href="#"` |
| `<form>` | All input groups | `novalidate` (custom validation) |
| `<label>` | Every input | `for="input-id"` or wrapping |
| `<fieldset>` + `<legend>` | Radio/checkbox groups | — |
| `<table>` | Data grids | `<caption>`, `<thead>`, `<th scope="col">` |
| `<ul>`/`<ol>` | Lists, breadcrumbs | — |
| `<kbd>` | Keyboard shortcuts | — |

**Prohibited:** `<div onClick>`, `<span role="button">`, `href="#"`, `tabindex>0` (except 0/-1), `outline: none` without replacement.

---

## 3. Focus Management

| Scenario | Requirement |
|----------|-------------|
| Page load | Skip link first focusable; focus `#main` on activation |
| Route change (SPA) | Focus `#main` (or first heading) after paint; announce route via `aria-live` |
| Modal open | Trap focus; focus first focusable (or close button); restore on close |
| Palette open | Trap focus; focus search input; Escape closes, restores trigger |
| Toast appear | `aria-live="polite"`, no focus steal |
| Error on submit | Focus first invalid field; `aria-invalid="true"`, `aria-describedby="error-id"` |
| Expand/collapse | `aria-expanded` on trigger; focus stays on trigger |
| Table row navigation | Arrow keys move focus; Enter/Space activates row action |

**Focus Visible:** Always. Custom ring: `3px solid var(--focus-ring)`, `outline-offset: 2px`. Never `outline: none`.

---

## 4. ARIA Patterns (Component Reference)

### 4.1 Command Palette
```jsx
<div role="dialog" aria-modal="true" aria-label="Command palette">
  <input aria-label="Search commands" aria-autocomplete="list" aria-controls="cmd-list" />
  <ul id="cmd-list" role="listbox">
    <li role="option" aria-selected="true">...</li>
  </ul>
</div>
```

### 4.2 Sidebar Navigation
```jsx
<nav aria-label="Workspaces">
  <ul role="list">
    <li>
      <a href="/orders" aria-current="page">Orders</a>
      <ul role="group" aria-label="Orders sections">
        <li><a href="/orders/123">ORD-123</a></li>
      </ul>
    </li>
  </ul>
</nav>
```

### 4.3 Data Table (Sortable, Selectable, Row Actions)
```jsx
<table>
  <caption>Orders</caption>
  <thead>
    <tr>
      <th scope="col"><button aria-sort="ascending">Date</button></th>
      <th scope="col">Customer</th>
      <th scope="col">Actions</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>2026-07-12</td>
      <td>Acme Corp</td>
      <td><button aria-label="View ORD-123">View</button></td>
    </tr>
  </tbody>
</table>
```

### 4.4 Form Field with Validation
```jsx
<div>
  <label for="email">Email</label>
  <input
    id="email"
    type="email"
    aria-invalid="true"
    aria-describedby="email-error email-hint"
    aria-required="true"
  />
  <span id="email-hint">We'll never share your email</span>
  <span id="email-error" role="alert">Invalid email format</span>
</div>
```

### 4.5 Toast / Notification
```jsx
<div role="status" aria-live="polite" aria-atomic="true">
  Order ORD-123 confirmed
</div>
```

### 4.6 Empty State
```jsx
<section aria-labelledby="empty-title">
  <h2 id="empty-title">No orders yet</h2>
  <p>When you place orders, they'll appear here.</p>
  <button>Place your first order</button>
</section>
```

---

## 5. Color & Contrast

| Element | Minimum Ratio | Target (AAA) |
|---------|--------------|--------------|
| Body text | 4.5:1 | 7:1 |
| Large text (≥18px or ≥14px bold) | 3:1 | 4.5:1 |
| UI components (borders, icons) | 3:1 | 4.5:1 |
| Focus ring | 3:1 against adjacent | 4.5:1 |
| Error/success states | Not color-only | — |

**Palette (Part 1D tokens)** must provide: `--color-text`, `--color-text-muted`, `--color-border`, `--color-focus`, `--color-success`, `--color-warning`, `--color-error`, `--color-surface`, `--color-background`.

---

## 6. Text & Typography

- **Resize:** Text resizable to 200% without horizontal scroll or content loss.
- **Line height:** Body ≥ 1.5; headings ≥ 1.3.
- **Letter spacing:** ≥ 0.12em for caps; no negative tracking.
- **Font:** Single family, system fallback stack. No icon fonts for UI icons (use SVG).
- **Language:** `lang="en"` on `<html>`; `lang` on partial content changes.

---

## 7. Motion & Animation

| Rule | Implementation |
|------|----------------|
| Respect `prefers-reduced-motion` | All transitions/animations disabled or < 100ms |
| No auto-play > 5s | Carousels, videos pause; user control required |
| No flashing > 3Hz | — |
| Loading skeletons | No shimmer if reduced-motion; static placeholder |
| Parallax / scroll effects | Disabled if reduced-motion |

---

## 8. Keyboard Shortcuts (Global)

| Shortcut | Action | Announced? |
|----------|--------|------------|
| `Cmd/Ctrl + K` | Open command palette | Yes (header button has `aria-label`) |
| `Esc` | Close dialog, palette, drawer, toast | — |
| `Tab` / `Shift+Tab` | Next/previous focusable | — |
| `Arrow keys` | Navigate menus, tables, tabs, palette | — |
| `Enter` / `Space` | Activate button, link, option | — |
| `Home` / `End` | First/last in list, table | — |
| `?` | Show keyboard help (future) | — |

**No single-key shortcuts** without modifier (except in command palette context).

---

## 9. Screen Reader Testing Checklist (Per Page)

- [ ] Page title unique and descriptive
- [ ] Heading hierarchy (H1 → H2 → H3) no skips
- [ ] All images have `alt` (decorative: `alt=""`)
- [ ] All form fields have accessible name (label, aria-label, aria-labelledby)
- [ ] Error messages announced (`role="alert"` or `aria-live="assertive"`)
- [ ] Status updates announced (`role="status"` / `aria-live="polite"`)
- [ ] Landmarks present (banner, nav, main, contentinfo, complementary)
- [ ] Focus order matches visual order
- [ ] No keyboard traps (except intentional modals)
- [ ] ARIA attributes valid (use `axe-core` + manual NVDA/JAWS/VoiceOver)

---

## 10. CI / Automation Gates

| Tool | Runs On | Threshold |
|------|---------|-----------|
| `axe-core` (playwright) | PR, nightly | 0 violations (AA) |
| `eslint-plugin-jsx-a11y` | PR, build | Error on violation |
| `storybook-addon-a11y` | Component dev | 0 violations per story |
| Lighthouse CI | PR | Accessibility score = 100 |
| Manual audit | Gate 3, Gate 4 | NVDA + VoiceOver + keyboard only |

---

## 11. Accessibility Statement (Template)

> "SporeKart is committed to WCAG 2.2 AA. Known limitations: [none at launch]. Report barriers: accessibility@sporekart.com. Last audit: [date]."