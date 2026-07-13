# Navigation Accessibility

## WCAG 2.2 AA Compliance Summary

All navigation components target WCAG 2.2 AA compliance. The following success criteria are covered:

| Criteria | Description | Covered By |
|----------|-------------|------------|
| 1.1.1 | Non-text Content | Icon aria-labels, alt text on brand logos |
| 1.3.1 | Info and Relationships | Semantic HTML (`nav`, `ol`, `ul`, `header`, `aside`) |
| 1.3.2 | Meaningful Sequence | DOM order matches visual order |
| 1.4.1 | Use of Color | Active states use icon + text, not color alone |
| 1.4.3 | Contrast (Minimum) | All text meets 4.5:1 ratio |
| 1.4.10 | Reflow | Responsive at 320px without loss of content |
| 1.4.12 | Text Spacing | No loss when text spacing increased |
| 2.1.1 | Keyboard | All components fully keyboard operable |
| 2.1.2 | No Keyboard Trap | Focus never trapped |
| 2.4.1 | Bypass Blocks | Skip link to main content |
| 2.4.3 | Focus Order | Logical tab order |
| 2.4.4 | Link Purpose (In Context) | Link text describes destination |
| 2.4.7 | Focus Visible | Visible focus ring on all interactive elements |
| 2.4.11 | Focus Not Obscured (AA) | Sticky headers/sidebars don't hide focus |
| 2.5.8 | Target Size (AA) | All touch targets at least 24x24px |
| 3.2.1 | On Focus | No context change on focus |
| 3.2.2 | On Input | No context change on input |
| 3.3.2 | Labels or Instructions | ARIA labels on icon-only controls |
| 4.1.2 | Name, Role, Value | ARIA roles, states, and properties |
| 4.1.3 | Status Messages | Live regions for dynamic updates |

## Keyboard Navigation Patterns

### AppShell & Layout
| Key | Action |
|-----|--------|
| `Tab` | Navigate through header, skip link, sidebar, main content |
| `Ctrl/Cmd + K` | Open command palette |
| `Escape` | Close mobile sidebar overlay |

### Sidebar
| Key | Action |
|-----|--------|
| `Arrow Up/Down` | Navigate between items |
| `Arrow Right` | Expand collapsed group |
| `Arrow Left` | Collapse expanded group |
| `Enter/Space` | Activate item / toggle group |
| `Escape` | Close mobile overlay |
| `Tab` | Move focus out of sidebar |

### Header
| Key | Action |
|-----|--------|
| `Tab` | Navigate brand, nav items, actions |
| `Enter/Space` | Activate item |
| `Escape` | Close open dropdown |

### Tabs
| Key | Action |
|-----|--------|
| `Arrow Left/Right` | Navigate tabs (horizontal) |
| `Arrow Up/Down` | Navigate tabs (vertical) |
| `Home` | First tab |
| `End` | Last tab |
| `Delete` | Close tab (closable only) |
| `Enter/Space` | Activate tab |

### Dropdown/Context/OverflowMenu
| Key | Action |
|-----|--------|
| `Arrow Up/Down` | Navigate items |
| `Arrow Right` | Open submenu |
| `Arrow Left` | Close submenu |
| `Enter/Space` | Activate item |
| `Escape` | Close menu |

### Command Palette
| Key | Action |
|-----|--------|
| `Ctrl/Cmd + K` | Open palette |
| `Arrow Up/Down` | Navigate results |
| `Enter` | Execute selected |
| `Escape` | Close palette |

### Pagination
| Key | Action |
|-----|--------|
| `Arrow Left/Right` | Previous/Next page |
| `Home` | First page |
| `End` | Last page |
| `Enter` | Activate page |

### Stepper
| Key | Action |
|-----|--------|
| `Tab` | Focus step (if clickable) |
| `Enter/Space` | Activate step |

### Breadcrumb
| Key | Action |
|-----|--------|
| `Tab` | Navigate links |
| `Enter` | Activate link |
| `Escape` | Close collapsed dropdown |

## ARIA Reference

### Roles

| Role | Component |
|------|-----------|
| `banner` | Header |
| `navigation` | Sidebar, TopNav, Breadcrumb, Pagination |
| `main` | ContentContainer |
| `contentinfo` | PageFooter |
| `tablist` | TabList |
| `tab` | Tab |
| `tabpanel` | TabPanel |
| `menu` | DropdownMenu, ContextMenu |
| `menubar` | Horizontal menu bar |
| `menuitem` | Menu item |
| `dialog` | CommandPalette |
| `listbox` | Command results |
| `option` | Command result item |
| `search` | Search input |

### States & Properties

| Attribute | Component | Usage |
|-----------|-----------|-------|
| `aria-current="page"` | Breadcrumb, Sidebar, TopNav | Current page indicator |
| `aria-current="step"` | Stepper | Current step |
| `aria-selected` | Tab, Command item | Selected state |
| `aria-expanded` | Dropdown, Sidebar group, Accordion | Open/close state |
| `aria-haspopup` | Menu trigger, Dropdown | Indicates popup |
| `aria-controls` | Tab, Menu trigger | References controlled element |
| `aria-labelledby` | TabPanel, Dialog | Referenced label |
| `aria-label` | Icon-only buttons | Accessible name |
| `aria-describedby` | Controls with description | Extended description |
| `aria-disabled` | Disabled items | Disabled state |
| `aria-hidden` | Separators, decorative icons | Hide from AT |
| `aria-modal` | CommandPalette | Modal state |
| `aria-activedescendant` | CommandPalette | Active option |
| `aria-placeholder` | Search input | Placeholder text |
| `aria-live="polite"` | Dynamic updates | Status announcements |
| `role="alert"` | Error states | Error announcements |

## Focus Management

- **Trap focus** inside modals (command palette, mobile sidebar overlay)
- **Return focus** to trigger element when modal/menu closes
- **Skip link**: First focusable element on page, links to `#main-content`
- **Visible focus**: `outline: 2px solid --color-focus` on all interactive elements
- **Focus order**: Logical top-to-bottom, left-to-right
- **No focus loss**: When removing an element, focus moves to next logical element

## Screen Reader Support

- All icons have `aria-hidden="true"` when decorative, or `aria-label` when informative
- Status changes announced via `aria-live` regions
- Dynamic content (lazy loaded tab panels) announced with `role="status"`
- Responsive layout changes announced (e.g., "Navigation collapsed")

## Responsive Accessibility

- **Mobile menu**: Announce when sidebar overlay opens/closes: "Navigation menu opened", "Navigation menu closed"
- **Breakpoint changes**: No context change without user action; resize preserves state
- **Touch targets**: Minimum 24x24px with adequate spacing
- **Orientation**: No loss of functionality in portrait or landscape

## Testing Procedures

| Test | Method | Tool |
|------|--------|------|
| Keyboard navigation | Manual tab through all interactive elements | Browser |
| Screen reader | Navigate with NVDA/JAWS/VoiceOver | NVDA, VoiceOver |
| Contrast check | Measure text against background | axe DevTools, WAVE |
| Focus visibility | Check focus ring on all interactive | Manual inspection |
| ARIA validation | DOM inspection | axe DevTools |
| Reflow | Resize to 320px width | Browser DevTools |
| Text spacing | Apply 200% spacing | Bookmarklet |
| Touch target | Measure all interactive targets | Manual inspection |
