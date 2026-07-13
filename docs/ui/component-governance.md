# Component Governance — SporeKart Enterprise Web Application

## 1. Overview

This document defines the governance rules for the Enterprise Design System component library. Every component follows a strict lifecycle: **Propose → Design → Build → Review → Freeze → Version → Deprecate**.

---

## 2. Component Lifecycle

### 2.1 Lifecycle States

| State | Description | Duration | Authority |
|-------|-------------|----------|-----------|
| **Proposed** | DCR submitted; not yet approved | Until approval | Principal DS Architect |
| **In Design** | Wireframes → Hi-fi → Prototype | 1–3 sprints | Principal UX/Visual Architect |
| **In Development** | Code implementation + tests | 1–2 sprints | Component Owner |
| **In Review** | Gates 1–5 | 1–2 sprints | Gate Reviewers |
| **Frozen** | Approved; no changes without DCR | Indefinite | CDO |
| **Deprecated** | Announced; migration period | 6–12 months | Principal DS Architect |
| **Removed** | Deleted from codebase | Immediate | Principal DS Architect |

### 2.2 Frozen Components (Locked)

Once a component reaches **Frozen**, these changes require DCR:
- Props API (add/remove/rename)
- Visual appearance (colors, spacing, radii)
- Behavior (keyboard, focus, animation)
- Slot structure / composition
- Default props

**Allowed without DCR:**
- Bug fixes (no API change)
- Documentation updates
- Test improvements
- Internal refactoring (no external change)

---

## 3. Component Governance Rules

### 3.1 Component API Standards

| Rule | Requirement |
|------|-------------|
| **Props Interface** | Exported as `ComponentNameProps`; documented with JSDoc |
| **Variants** | Use `variant` prop (primary, secondary, destructive, ghost, link) |
| **Sizes** | Use `size` prop (sm, md, lg); default `md` |
| **States** | `disabled`, `loading`, `error` via props; not CSS classes |
| **Forward Ref** | `forwardRef` for all interactive components |
| **Polymorphic** | `as` / `component` prop for element type (Button → `<a>`) |
| **Forwarded Props** | Spread `...rest` to underlying element (except controlled props) |

### 3.2 Component Composition Rules

| Rule | Requirement |
|------|-------------|
| **Single Responsibility** | One component = one clear purpose |
| **Composition over Configuration** | Slots > props for content variation |
| **Primitive First** | Build from primitives (Box, Text, Flex) not HTML |
| **No Style Props** | No `style`, `className` overrides; use tokens |
| **Accessibility Built-in** | ARIA, keyboard, focus built-in; not optional |

### 3.3 Component Structure

```
src/components/[ComponentName]/
├── ComponentName.tsx          # Main component
├── ComponentName.props.ts     # Props interface + types
├── ComponentName.stories.tsx  # Storybook stories (all variants)
├── ComponentName.test.tsx     # Unit + a11y tests
├── ComponentName.docs.mdx     # Documentation (auto-generated)
├── index.ts                   # Public exports
└── variants.ts                # Variant definitions (if complex)
```

---

## 4. Component Categories & Governance

### 4.1 Primitive Components (Layer 0)

| Component | Frozen | Dependencies | Variants |
|-----------|--------|--------------|----------|
| Box | ✅ | — | — |
| Flex | ✅ | Box | direction, gap, align, justify |
| Grid | ✅ | Box | columns, gap |
| Text | ✅ | — | size, weight, color, align |
| Heading | ✅ | Text | level (1–6) |
| Spacer | ✅ | Box | size |
| Divider | ✅ | Box | orientation, weight |
| VisuallyHidden | ✅ | — | — |

**Governance:** Zero dependencies; maximum reuse; never deprecated

---

### 4.2 Core Interactive Components (Layer 1)

| Component | Frozen | Dependencies | States | Variants |
|-----------|--------|--------------|--------|----------|
| Button | ✅ | Text, Box | default, hover, active, focus, disabled, loading | primary, secondary, destructive, ghost, link |
| IconButton | ✅ | Button | same | same |
| Link | ✅ | Text | default, hover, focus, visited | — |
| Input | ✅ | Box, Text | default, focus, error, disabled, filled | text, email, password, number, tel, url, search |
| Textarea | ✅ | Input | same | — |
| Select | ✅ | Input, Popover | same | single, multiple |
| Checkbox | ✅ | Box | default, hover, focus, disabled, indeterminate | — |
| Radio | ✅ | Box | same | — |
| RadioGroup | ✅ | Radio | — | — |
| Switch | ✅ | Box | default, focus, disabled | — |
| Label | ✅ | Text | — | required, optional |

**Governance:** All form controls use shared validation hooks; consistent a11y patterns

---

### 4.3 Composite Components (Layer 2)

| Component | Frozen | Dependencies | Slots |
|-----------|--------|--------------|-------|
| Card | ✅ | Box, Flex, Text, Divider | header, body, footer, media |
| Modal | ✅ | Box, Flex, Portal, FocusTrap | header, body, footer |
| Drawer | ✅ | Modal | same |
| Popover | ✅ | Box, Portal, FocusTrap | trigger, content |
| Tooltip | ✅ | Popover | — |
| Dropdown | ✅ | Popover, Button, List | trigger, items |
| Tabs | ✅ | Flex, Button, Box | tabs, panels |
| Accordion | ✅ | Box, Button, Collapse | items |
| Table | ✅ | Box, Flex, Text, Th, Td, Tr | columns, rows, actions |
| DataGrid | ✅ | Table, VirtualList | columns, rows, selection, sort, filter |
| Pagination | ✅ | Flex, Button | — |
| Breadcrumbs | ✅ | Flex, Link, Separator | items |
| Stepper | ✅ | Flex, Circle, Text, Line | steps |
| Avatar | ✅ | Box, Image, Text | src, fallback, size, status |
| Badge | ✅ | Box, Text | variant, size |
| Tag | ✅ | Badge | removable, closable |
| Chip | ✅ | Tag | — |
| Progress | ✅ | Box | value, max, variant |
| Skeleton | ✅ | Box | variant (text, circular, rectangular) |

---

### 4.4 Layout & Navigation Components (Layer 3)

| Component | Frozen | Dependencies |
|-----------|--------|--------------|
| Header | ✅ | Box, Flex, Button, Avatar, Dropdown |
| Sidebar | ✅ | Box, Flex, NavLink, Collapse |
| Footer | ✅ | Box, Flex, Link |
| NavLink | ✅ | Link |
| NavList | ✅ | NavLink |
| Breadcrumb | ✅ | Breadcrumbs, Link, Separator |
| Pagination | ✅ | Button, Icon |
| Tabs | ✅ | (see Layer 2) |
| Stepper | ✅ | (see Layer 2) |

---

### 4.5 Feedback Components (Layer 4)

| Component | Frozen | Dependencies |
|-----------|--------|--------------|
| Toast | ✅ | Portal, Box, Flex, Icon, Button |
| ToastStack | ✅ | Toast |
| Banner | ✅ | Box, Flex, Icon, Link |
| Alert | ✅ | Box, Flex, Icon, Text |
| Dialog | ✅ | Modal |
| ConfirmDialog | ✅ | Dialog |
| EmptyState | ✅ | Box, Flex, Illustration, Text, Button |
| LoadingOverlay | ✅ | Box, Flex, Spinner |

---

## 5. Component Quality Standards

### 5.1 Mandatory for Every Frozen Component

| Requirement | Tool / Method |
|-------------|---------------|
| **Props Interface** | Exported `ComponentNameProps` with JSDoc |
| **Default Props** | Defined in component; no required props without defaults |
| **Storybook Stories** | All variants + states + a11y addon |
| **Unit Tests** | ≥ 80% coverage (props, events, states) |
| **A11y Tests** | axe-core + keyboard + screen reader |
| **Visual Regression** | Chromatic baseline (all variants) |
| **Props Documentation** | Auto-generated from JSDoc + manual examples |
| **Migration Guide** | If breaking change in future |

### 5.2 Component Documentation Template

```markdown
# ComponentName

## Purpose
[One sentence: what problem does this solve?]

## When to Use
[Scenarios where this component is the right choice]

## When Not to Use
[Scenarios where a different component is better]

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| variant | 'primary' \| 'secondary' \| 'destructive' \| 'ghost' \| 'link' | 'primary' | Visual style |
| size | 'sm' \| 'md' \| 'lg' | 'md' | Size variant |
| disabled | boolean | false | Disables interaction |
| loading | boolean | false | Shows spinner; disables |
| as | React.ElementType | 'button' | Polymorphic element |

## Variants

| Variant | Use Case |
|---------|----------|
| primary | Primary action (one per view) |
| secondary | Secondary actions |
| destructive | Irreversible/dangerous |
| ghost | Low-emphasis actions |
| link | Navigation-style actions |

## States

| State | Visual |
|-------|--------|
| Default | [screenshot] |
| Hover | [screenshot] |
| Active | [screenshot] |
| Focus | [screenshot + focus ring] |
| Disabled | [screenshot] |
| Loading | [screenshot + spinner] |

## Accessibility
- [ ] Keyboard: Tab to focus; Enter/Space to activate
- [ ] ARIA: role="button"; aria-disabled; aria-busy
- [ ] Focus Ring: Visible, 3px, offset 2px
- [ ] Screen Reader: Announces state changes

## Usage Examples

### Basic
```tsx
<Button onClick={handleClick}>Save</Button>
```

### With Loading
```tsx
<Button loading onClick={save}>Saving…</Button>
```

### Destructive
```tsx
<Button variant="destructive" onClick={delete}>Delete</Button>
```

## Related Components
- [Link] — for navigation
- [IconButton] — for icon-only actions
- [Link] — for destructive actions in forms
```

---

## 6. Component Versioning

### 5.1 Semantic Versioning for Components

| Change Type | Version Bump | Example |
|-------------|--------------|---------|
| **Breaking** | Major | Prop renamed/removed; slot removed; default behavior change |
| **Feature** | Minor | New variant; new prop; new slot; new sub-component |
| **Fix** | Patch | Bug fix; visual tweak; a11y improvement; doc update |

### 5.2 Deprecation Warnings

```tsx
// In component props
/** @deprecated Use `variant="destructive"` instead. Will be removed in v3.0. */
dangerous?: boolean;

// Console warning (development only)
if (process.env.NODE_ENV !== 'production' && props.dangerous) {
  console.warn('[Button] `dangerous` prop is deprecated. Use `variant="destructive"` instead.');
}
```

---

## 6. Component Registry

### 6.1 Registry Format (JSON)

```json
{
  "name": "Button",
  "version": "1.2.0",
  "status": "frozen",
  "category": "core-interactive",
  "owner": "frontend-team",
  "frozenSince": "2026-07-13",
  "repo": "@sporekart/ui",
  "docs": "https://ui.sporekart.dev/components/button",
  "stories": "https://storybook.sporekart.dev/?path=/story/components-button--default",
  "bundleSize": { "js": "2.1KB", "css": "1.3KB" },
  "dependencies": ["Text", "Box", "Icon"],
  "peerDependencies": ["react", "react-dom"],
  "exports": {
    "Button": "./Button",
    "ButtonProps": "./Button.props"
  }
}
```

### 5.3 Registry Location

| Location | Format | Purpose |
|----------|--------|---------|
| `packages/ui/registry.json` | JSON | Single source of truth |
| `docs/ui/component-registry.md` | Markdown | Human-readable index |
| `@sporekart/ui/registry` | npm package | Programmatic access |

---

## 6. Component Addition Process

### 6.1 New Component Checklist

| Step | Owner | Deliverable |
|------|-------|-------------|
| 1. DCR Approved | CDO + Principal DS Architect | DCR log entry |
| 2. Design Complete | Principal Visual Designer | Figma (all variants, states, 6 breakpoints) |
| 3. Spec Documented | Component Owner | Spec doc (props, variants, states, a11y) |
| 4. Implementation | Component Owner | Code + tests + stories + docs |
| 4. Gates 1–5 Pass | Reviewers | Approval log |
| 5. Registry Entry | Principal DS Architect | `registry.json` updated |
| 6. Release | Release Manager | Published to npm |

### 5.2 Minimum Viable Component (MVC)

Before first review, a new component must have:
- [ ] Props interface + JSDoc
- [ ] All variants implemented
- [ ] All states implemented (default, hover, focus, disabled, loading)
- [ ] Storybook stories (all variants + a11y addon)
- [ ] Unit tests (≥ 80% coverage)
- [ ] A11y tests (axe + keyboard + screen reader)
- [ ] Visual regression baseline (Chromatic)
- [ ] Props documentation (auto-generated + examples)

---

## 7. Sub-Component Governance

Some components have **sub-components** (e.g., `Select.Trigger`, `Select.Options`, `Select.Option`).

| Rule | Requirement |
|------|-------------|
| **Exported Together** | Sub-components exported from main component |
| **Shared Context** | Use React Context for state (not props drilling) |
| **Independent Testable** | Each sub-component testable in isolation |
| **Documented Together** | Single docs page with sub-component sections |

---

## 6. Component Health Metrics

| Metric | Target | Measurement |
|--------|--------|-------------|
| **Bundle Size** | ≤ budget per component | Bundle analyzer |
| **Test Coverage** | ≥ 80% | Vitest coverage |
| **A11y Score** | 0 violations | axe-core |
| **Visual Regressions** | 0 | Chromatic |
| **Usage** | Tracked per component | Bundle analyzer + code search |
| **Deprecation Age** | ≤ 12 months | Registry timestamp |

---

## 7. Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | Sprint 19 Part 1E | Initial governance |

---

**Authority:** Principal Design System Architect  
**Review Cycle:** Quarterly (component audit); Annual (governance)