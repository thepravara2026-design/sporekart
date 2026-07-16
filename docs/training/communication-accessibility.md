# Enterprise Communication Platform — Accessibility Report

**Sprint 26 · Part 10.** WCAG-style accessibility review of the mock-mode Communication
feature. Status reflects the current implementation.

## 1. Semantic Landmarks and Roles

| Element | Where | Markup |
| --- | --- | --- |
| Section navigation | `CommunicationWorkspaceRoute` | `<nav aria-label="Communication sections">` |
| Search region | `CommToolbar` | `role="search"` on container; `<input type="search">` with `aria-label` |
| Tab bars | `SectionTabs`, Notifications read filter, Delivery state filter | `role="tablist"` + `role="tab"` + `aria-selected` |
| Panels | Each page section | `id="panel-<key>"` + `aria-labelledby="tab-<key>"` |
| Status regions | `CommEmptyState` | `role="status"` |
| Field group | `AudienceSelector` | `<fieldset>`/`<legend>` |
| Toggle buttons | Marks/blocks/audience/pinned | `aria-pressed` |
| Live preview | `RichTextEditor` preview | `aria-live="polite"` |
| Labeled controls | Selects/inputs | `aria-label` or wrapped `<label>` |
| Decorative icons | All `Icon` usages | `aria-hidden` (registry-only icons carry no text) |

`SectionTabs` wires `aria-controls={`panel-${tab.key}`}` and `id={`tab-${tab.key}`}` so tabs and
panels are associated. `AnnouncementCard` sets `aria-label={title}` on the `<article>`; pinned/
featured icons use `aria-label` ("Pinned"/"Featured") while purely decorative spans use
`aria-hidden`.

## 2. Keyboard Operability

- All actions are native `<button>` / `<input>` / `<select>` elements — fully keyboard operable.
- Tab order follows DOM order: nav → search → filters → list → pagination.
- Toggle buttons expose pressed state via `aria-pressed` and respond to Enter/Space natively.
- The rich-text `textarea` is a labeled, focusable control; toolbar buttons precede it in order.
- Dialog (design-system) traps focus and closes on Escape per its own implementation.

## 3. Focus Considerations

- Focus styles rely on the design-system/element defaults plus token-based active backgrounds
  (`--color-bg-primary-weak`). No custom `:focus-visible` ring is defined in feature code; this
  is a recommended enhancement (see §5).
- Disabled future controls (`FutureChannelCard`, future audience scopes) are skipped by the tab
  order automatically via the `disabled` attribute.

## 4. Checklist (WCAG 2.1 AA, current status)

| Criterion | Practice | Status | Notes |
| --- | --- | --- | --- |
| 1.1.1 Non-text content | Icons `aria-hidden`; meaningful controls labeled | Pass | Decorative icons hidden; buttons labeled |
| 1.3.1 Info & relationships | `fieldset/legend`, `role=tablist`, labeled fields | Pass | Structure conveyed semantically |
| 1.3.2 Meaningful sequence | DOM order matches visual order | Pass | Flex layouts preserve order |
| 1.4.1 Use of color | Status uses text + tone tokens, not color alone | Pass | Badges include text labels |
| 1.4.3 Contrast | Tokens (`--color-*`) chosen for AA | Pass (assumed) | Depends on token theme |
| 1.4.11 Non-text contrast | UI component borders use token colors | Pass | Dashed/solid borders differentiate future |
| 2.1.1 Keyboard | All controls native | Pass | No custom widgets block keys |
| 2.4.6 Headings/labels | Page `<h1>` + card `<h3>`; labeled fields | Pass | Headings present and descriptive |
| 2.4.7 Focus visible | Native focus; no custom ring | Partial | Recommend explicit `:focus-visible` |
| 3.1.1 Language of page | Inherits app `lang` | Pass | No per-feature override needed |
| 3.2.1 On focus / 3.2.2 On input | No focus/input side effects | Pass | Toggles only change state |
| 4.1.2 Name/role/value | `aria-pressed`, `aria-selected`, `aria-label` | Pass | State exposed to AT |
| 4.1.3 Status messages | Live preview `aria-live="polite"` | Pass | Editor preview announced |

## 5. Recommendations for Future Enhancement

- Add a visible `:focus-visible` outline token so keyboard focus is unmistakable.
- For the History `<table>`, add `<caption>` and `scope` on header cells for stronger table
  semantics.
- Consider `aria-live` on the pagination "Showing X–Y of Z" region for screen-reader feedback.
- Provide an accessible name for the editor toolbar groups (marks vs blocks) via `aria-label`.
- Ensure the design-system `Dialog` restores focus to the triggering element on close.
