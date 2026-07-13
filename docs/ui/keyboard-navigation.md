# Keyboard Navigation Standards — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1C): UX Standards & Accessibility Foundation

> **Status:** Mandatory application-wide behavior. Every interactive element reachable and operable by keyboard alone. Reuses Part 1B layout landmarks.

---

## 1. Global Shortcut Map

| Shortcut | Action | Context | Notes |
|----------|--------|---------|-------|
| `Cmd/Ctrl + K` | Open Command Palette | Global | Header button announces shortcut |
| `Cmd/Ctrl + /` | Focus Global Search | Global | Alternative trigger |
| `Esc` | Close dialog / palette / drawer / toast / popover | Global | Always works |
| `Tab` | Next focusable | Global | — |
| `Shift + Tab` | Previous focusable | Global | — |
| `Enter` | Activate button / link / submit form | Focused element | — |
| `Space` | Toggle checkbox / activate button | Focused element | — |
| `Arrow Keys` | Navigate within component | Menus, tabs, tables, palette, date picker | — |
| `Home` | First item (list, menu, table row) | Component | — |
| `End` | Last item | Component | — |
| `Page Up` | Scroll up / previous page | Table, list | — |
| `Page Down` | Scroll down / next page | Table, list | — |
| `?` | Show keyboard help (future) | Global | — |

**No single-key shortcuts** without modifier (except in Command Palette context).

---

## 2. Focus Management Rules

### 2.1 Page Load / Route Change (SPA)
1. Route changes
2. `#main` receives focus (`tabIndex=-1`, `focus()`)
3. Screen reader announces new page title
4. Skip link available as first tab stop

### 2.2 Modal / Dialog Open
1. Focus moves to first focusable in dialog (or close button)
2. Focus trapped (Tab cycles within)
3. `Esc` closes, focus returns to trigger
4. Background `aria-hidden="true"` or `<dialog>`

### 2.3 Command Palette Open
1. Focus on search input
2. `ArrowDown` → first result
3. `ArrowUp/Down` navigate results
4. `Enter` → execute, close, focus returns to trigger
5. `Esc` → close, focus returns to trigger

### 2.4 Sidebar Drawer (Mobile)
1. Open → focus first focusable in drawer
2. Trap within drawer
3. `Esc` / backdrop click → close, focus hamburger

### 2.5 Toast Appear
- **No focus steal** — `aria-live="polite"` announces
- User may `Tab` to toast if needed (focusable close button)

### 2.6 Error on Submit
- Focus first `input[aria-invalid="true"]`
- Error message linked via `aria-describedby`

---

## 3. Component Keyboard Patterns

### 3.1 Navigation Menus (Sidebar, Header Profile, User Menu)
| Key | Behavior |
|-----|----------|
| `Tab` | Enter menu → first item |
| `ArrowDown/Up` | Next/previous item |
| `ArrowRight` | Expand submenu / open dropdown |
| `ArrowLeft` | Collapse submenu / close dropdown |
| `Enter/Space` | Activate link / toggle expand |
| `Esc` | Close dropdown / collapse submenu |

### 3.2 Tabs
| Key | Behavior |
|-----|----------|
| `Tab` | Into tab panel (skip tab list if auto-activate) |
| `ArrowLeft/Right` | Previous/next tab |
| `Home` | First tab |
| `End` | Last tab |
| `Enter/Space` | Activate tab (if manual) |

### 3.3 Data Table (Sortable, Selectable, Row Actions)
| Key | Behavior |
|-----|----------|
| `Tab` | Into table → first header / first cell |
| `Arrow Keys` | Navigate cells (Excel-like) |
| `Enter/Space` | Toggle row selection / activate row action |
| `Shift + Arrow` | Extend selection |
| `Home` | First cell in row |
| `End` | Last cell in row |
| `Page Up/Down` | Scroll by viewport |
| `Ctrl/Cmd + A` | Select all (if supported) |
| `Esc` | Clear selection |

### 3.4 Command Palette
| Key | Behavior |
|-----|----------|
| `Cmd/Ctrl+K` | Open, focus search |
| `ArrowDown/Up` | Next/previous result |
| `Enter` | Execute selected |
| `Esc` | Close, restore trigger focus |
| `Tab` | Into result list (if focus on input) |

### 3.5 Date Picker / Calendar
| Key | Behavior |
|-----|----------|
| `ArrowLeft/Right` | Previous/next day |
| `ArrowUp/Down` | Previous/next week |
| `PageUp/PageDown` | Previous/next month |
| `Home` | First day of month |
| `End` | Last day of month |
| `Enter/Space` | Select date |
| `Esc` | Close, focus trigger |

### 3.6 Combobox / Autocomplete
| Key | Behavior |
|-----|----------|
| `ArrowDown` | Open list / next option |
| `ArrowUp` | Previous option |
| `Enter` | Select option |
| `Esc` | Close list |
| `Home/End` | First/last option |
| `Tab` | Select highlighted, close, move next |

### 3.7 Toast Stack
| Key | Behavior |
|-----|----------|
| `Tab` | Enter toast stack (if focusable close) |
| `Esc` | Dismiss focused toast |
| `Delete` | Dismiss focused toast |

---

## 4. Skip Link

```html
<a href="#main" class="skip-link">Skip to content</a>
```
- First focusable element on page
- Visible on focus (`top: 8px`)
- Targets `<main id="main" tabIndex="-1">`

---

## 5. Focus Visibility

**All interactive elements MUST have visible focus:**
```css
:focus-visible {
  outline: 3px solid var(--color-focus-ring);
  outline-offset: 2px;
}
```
- Never `outline: none` without replacement
- Focus ring color: `--color-focus-ring` (meets 3:1 vs adjacent)

---

## 6. Tab Order Principles

1. **Match visual order** (left→right, top→bottom)
2. **Landmarks first:** Skip link → Header → Sidebar → Main → Footer
3. **Within component:** Label → Input → Helper → Error → Actions
4. **No `tabindex > 0`** (only `0` or `-1`)

---

## 7. Testing Protocol

| Test | Method |
|------|--------|
| **Tab through entire page** | Manual — verify all interactive reached, order logical |
| **Tab through modal** | Manual — trap works, `Esc` restores |
| **Arrow navigate table** | Manual — all cells reachable, actions activatable |
| **Palette open/close** | Manual — focus restore verified |
| **Drawer open/close** | Manual (mobile) — focus restore verified |
| **Screen reader + keyboard** | NVDA/VoiceOver — all announced, no silent failures |
| **Zoom 200% + keyboard** | Manual — no overlap, targets ≥ 44×44px |

---

## 8. Prototype Keyboard Demo

Route: `/demo/keyboard` — Interactive playground:
- Focus order visualizer (highlights tab stops)
- Component keyboard pattern sandbox (tabs, table, combobox, date picker, palette)
- Shortcut cheat sheet (copyable)
- `prefers-reduced-motion` toggle