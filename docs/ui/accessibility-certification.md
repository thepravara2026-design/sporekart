# Accessibility Certification — SporeKart Enterprise Web Application

## 1. Conformance Target

| Standard | Level | Scope |
|----------|-------|-------|
| **WCAG 2.2** | **AA** | All web content |
| **EN 301 549** | — | EU public procurement alignment |
| **Section 508** | — | US federal alignment |
| **ARIA 1.2** | — | All custom components |

**Non-negotiable:** No AA exceptions. AAA targeted for: contrast (text), focus appearance, target size, motion reduction.

---

## 2. Certification Requirements

### 2.1 Per-Component Certification

Every component in `@sporekart/ui` must pass:

| Check | Tool | Threshold |
|-------|------|-----------|
| **Automated** | axe-core (JSDOM) | 0 violations (AA) |
| **Keyboard** | Manual test script | 100% reachable |
| **Screen Reader** | NVDA + Chrome; VoiceOver + Safari | 100% announced |
| **Focus Visible** | Visual inspection + `:focus-visible` test | 100% |
| **Color Contrast** | axe + manual | 4.5:1 text; 3:1 UI |
| **Reduced Motion** | `prefers-reduced-motion` media query | All animations disabled |
| **Zoom 200%** | Browser zoom test | No horizontal scroll; no overlap |

### 2.2 Per-Page Certification

Every page must pass:

| Check | Tool | Threshold |
|-------|------|-----------|
| **Automated** | Lighthouse + axe-core | Score 100; 0 AA violations |
| **Keyboard Navigation** | Manual (Tab/Shift+Tab/Arrows) | 100% tasks completable |
| **Screen Reader** | NVDA + Chrome | All content announced |
| **Focus Order** | Tab through page | Matches visual order |
| **Landmarks** | axe + manual | banner, nav, main, contentinfo present |
| **Heading Hierarchy** | axe + manual | H1→H2→H3 no skips |
| **Zoom 200%** | Browser zoom | No horizontal scroll; no content loss |
| **Reduced Motion** | `prefers-reduced-motion: reduce` | Animations disabled |

---

## 3. Component Accessibility Standards

### 3.1 Required Patterns

| Component | ARIA Pattern | Keyboard | Screen Reader |
|-----------|--------------|----------|---------------|
| **Button** | `<button>` | Enter/Space | Name = text content |
| **Link** | `<a href>` | Enter | Descriptive text |
| **Input** | `<input>` + `<label for>` | Tab | Label + error announced |
| **Select** | `<select>` / Combobox | Arrows, Enter, Esc | Options announced |
| **Checkbox** | `<input type="checkbox">` | Space | Checked state announced |
| **Radio** | `<input type="radio">` + `<fieldset>` + `<legend>` | Arrows | Group + selection announced |
| **Switch** | `role="switch"` + `aria-checked` | Space | On/Off announced |
| **Tabs** | `role="tablist"` + `role="tab"` + `role="tabpanel"` | Arrows, Home, End | Active tab announced |
| **Modal** | `role="dialog"` + `aria-modal="true"` | Trap; Esc closes | Title announced |
| **Drawer** | `role="dialog"` + `aria-modal="true"` | Trap; Esc closes | Title announced |
| **Dropdown** | `role="menu"` + `role="menuitem"` | Arrows, Enter, Esc | Items announced |
| **Tooltip** | `role="tooltip"` + `aria-describedby` | Hover/Focus | Content announced |
| **Toast** | `role="status"` (info) / `role="alert"` (error) | — | Auto-announced |
| **Table** | `<table>` + `<caption>` + `<th scope>` | Arrows (grid) | Headers announced |
| **DataGrid** | `role="grid"` + `row`/`gridcell` | Arrows, Home/End, Ctrl+A | Row/col headers announced |

### 3.2 Mandatory Attributes

| Element | Required |
|---------|----------|
| `<button>` | `type="button"` (default) |
| `<input>` | `id` + `<label for>`; `aria-invalid`, `aria-describedby` |
| `<select>` | `id` + `<label for>` |
| `<dialog>` | `role="dialog" aria-modal="true" aria-labelledby` |
| `<table>` | `<caption>`; `<th scope="col\|row">` |
| `<img>` | `alt` (empty if decorative) |
| `<svg>` (icon) | `aria-hidden="true"` + `<title>` if standalone |

### 3.3 Focus Management

| Scenario | Behavior |
|----------|----------|
| Route change (SPA) | Focus `#main` (tabIndex=-1) |
| Modal open | Focus first focusable (or close button) |
| Modal close | Return to trigger |
| Drawer open | Focus first focusable in drawer |
| Drawer close | Return to trigger |
| Toast appear | No focus steal; `aria-live="polite"` |
| Error on submit | Focus first `aria-invalid="true"` |
| Expand/collapse | Focus stays on trigger; `aria-expanded` toggled |

---

## 4. Testing Protocol

### 4.1 Automated Testing (CI)

```yaml
# .github/workflows/a11y.yml
test:a11y:
  - run: npm run test:a11y:auto  # axe-core + JSDOM
  - run: npm run test:a11y:stories  # Storybook + axe
```

**Threshold:** 0 violations (AA) in CI

### 4.2 Manual Testing Checklist (Per Component)

| Test | Method | Pass Criteria |
|------|--------|---------------|
| **Tab Navigation** | Tab through all interactive elements | All reachable; order logical |
| **Shift+Tab** | Reverse tab | All reachable; reverse order |
| **Arrow Keys** | In composite widgets (tabs, menus, grids) | Navigation works |
| **Enter/Space** | Activate buttons, links, toggles | Action triggered |
| **Escape** | Close modals, drawers, dropdowns, toasts | Closes; focus returns |
| **Home/End** | Lists, grids, tabs | First/last item |
| **Page Up/Down** | Long lists, tables | Page scroll |
| **Screen Reader (NVDA)** | Navigate with arrow keys | All content announced; no verbose |
| **Screen Reader (VoiceOver)** | Navigate with rotor | All content announced |
| **Focus Visible** | Tab through | 3px solid ring visible on all |
| **Color Contrast** | axe + manual | 4.5:1 text; 3:1 UI |
| **Zoom 200%** | Ctrl/Cmd + "+" | No horizontal scroll; no overlap |

### 4.3 Screen Reader Test Matrix

| Component | NVDA + Chrome | VoiceOver + Safari | JAWS + Edge |
|-----------|---------------|-------------------|-------------|
| Button | ✅ | ✅ | ✅ |
| Input + Label | ✅ | ✅ | ✅ |
| Select/Combobox | ✅ | ✅ | ✅ |
| Checkbox/Radio | ✅ | ✅ | ✅ |
| Modal | ✅ | ✅ | ✅ |
| Tabs | ✅ | ✅ | ✅ |
| Table/Grid | ✅ | ✅ | ✅ |
| Toast | ✅ | ✅ | ✅ |
| Tooltip | ✅ | ✅ | ✅ |

**All must pass.**

---

## 5. Reduced Motion

### 5.1 Implementation

```css
@media (prefers-reduced-motion: reduce) {
  *,
  *::before,
  *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
    scroll-behavior: auto !important;
  }
}
```

### 5.2 Components Affected

| Animation | Reduced Behavior |
|-----------|------------------|
| Route transition | Instant (no fade/slide) |
| Skeleton shimmer | Static base color |
| Modal fade/scale | Instant |
| Drawer slide | Instant |
| Toast slide | Instant |
| Accordion height | Instant |
| Tooltip fade | Instant |
| Spinner | Static (or CSS-only slow spin) |

---

## 6. Color Contrast Requirements

| Element | Minimum Ratio | Target (AAA) |
|---------|---------------|--------------|
| Body text | 4.5:1 | 7:1 |
| Large text (≥18px or ≥14px bold) | 3:1 | 4.5:1 |
| UI components (borders, icons) | 3:1 | 4.5:1 |
| Focus ring | 3:1 vs adjacent | 4.5:1 |
| Error/success text | 4.5:1 | 7:1 |
| Placeholder text | 4.5:1 | 7:1 |

**Verification:** axe-core + manual sampling (Color Contrast Analyzer)

---

## 6. Keyboard Shortcuts

| Shortcut | Action | Context |
|----------|--------|---------|
| `Cmd/Ctrl + K` | Open Command Palette | Global |
| `Cmd/Ctrl + /` | Focus Search | Global |
| `Esc` | Close modal/drawer/dropdown/toast | Context |
| `Tab` | Next focusable | Global |
| `Shift + Tab` | Previous focusable | Global |
| `Enter` | Activate button/link | Focus |
| `Space` | Toggle checkbox; activate button | Focus |
| `Arrow Keys` | Navigate menus, tabs, grids | Widget |
| `Home` | First item (list/grid/tab) | Widget |
| `End` | Last item | Widget |
| `Page Up/Down` | Scroll page/list | Global/Widget |

**No single-key shortcuts** without modifier (except in Command Palette context).

---

## 7. Screen Reader Testing Scripts

### 7.1 NVDA Test Script (Per Component)

```text
1. Navigate to component
2. Verify name announced (label/button text)
3. Verify role announced (button, link, textbox, etc.)
4. Verify state announced (checked, selected, expanded, disabled)
5. Interact (Enter/Space/Arrows)
5. Verify state change announced
6. Verify no extra verbose announcements
```

### 7.2 VoiceOver Test Script (Per Component)

```text
1. Navigate with rotor to component
2. Swipe right → verify name + role + state
3. Double-tap → activate
6. Swipe → verify state change announced
```

---

## 7. Certification Process

### 7.1 Component Certification

| Step | Owner | Artifact |
|------|-------|----------|
| 1. Automated Pass | Component Owner | CI log (axe 0 violations) |
| 2. Keyboard Test | Component Owner | Test log (signed) |
| 3. NVDA Test | Component Owner | Test log (signed) |
| 4. VoiceOver Test | Component Owner | Test log (signed) |
| 5. Contrast Check | Component Owner | CCA report |
| 6. Reduced Motion | Component Owner | Test log |
| 7. Gate 4 Sign-off | Principal A11y Architect | Approval log entry |

### 7.2 Page Certification

| Step | Owner | Artifact |
|------|-------|----------|
| 1. Lighthouse CI | CI | Report (≥95 perf, 100 a11y) |
| 2. axe-core Auto | CI | Report (0 violations) |
| 3. Keyboard Test | QA | Test log |
| 4. NVDA Full Page | QA | Test log |
| 5. VoiceOver Full Page | QA | Test log |
| 6. Zoom 200% | QA | Screenshots |
| 7. Reduced Motion | QA | Test log |
| 7. Gate 4 Sign-off | Principal A11y Architect | Approval log entry |

---

## 8. Certification Records

### 8.1 Component Certification Log

`/docs/a11y/certification/components/[ComponentName].md`

```markdown
# A11y Certification: Button

**Version:** 1.2.0
**Date:** 2026-07-13
**Design System Version:** 1.2.0

## Automated
- axe-core: ✅ 0 violations

## Manual Testing
| Test | Tester | Date | Status |
|------|--------|------|--------|
| Keyboard | [Name] | YYYY-MM-DD | ✅ |
| NVDA | [Name] | YYYY-MM-DD | ✅ |
| VoiceOver | [Name] | YYYY-MM-DD | ✅ |
| Contrast | [Name] | YYYY-MM-DD | ✅ |
| Reduced Motion | [Name] | YYYY-MM-DD | ✅ |

## Sign-off
**Principal Accessibility Architect:** [Name] — ✅ Approved
**Date:** YYYY-MM-DD
```

---

## 9. Version History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | Sprint 19 Part 1E | Enterprise Accessibility Team | Initial certification |

---

**Authority:** Principal Accessibility Architect  
**Review Cycle:** Quarterly (aligned with WCAG updates)  
**Effective:** Sprint 19 Part 1E certification

**Zero Exceptions Policy:** No AA exceptions permitted. Any component failing certification blocks release.