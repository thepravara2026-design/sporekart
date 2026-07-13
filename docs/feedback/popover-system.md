# Popover System

## Overview

Popovers are floating panels that appear on click or hover, containing interactive content — menus, filters, forms, or information. Unlike tooltips, popovers are interactive, can contain complex content, and are triggered by explicit user action.

### Component Hierarchy

```
Popover
├── InformationPopover
├── ActionPopover
├── ContextPopover
├── InteractivePopover
└── NestedPopover
```

---

## Variants

| Variant | Trigger | Content | Dismiss |
|---------|---------|---------|---------|
| **InformationPopover** | Click / hover (configurable) | Read-only info, help text, details | Click outside, Escape |
| **ActionPopover** | Click | Action items, menu, links | Click outside, Escape, item selection |
| **ContextPopover** | Right-click (contextmenu) | Context-sensitive actions | Click outside, Escape, selection |
| **InteractivePopover** | Click | Forms, filters, date pickers | Click outside, Escape, explicit close |
| **NestedPopover** | Click | Sub-menus within a popover | Parent dismiss, Escape |

### When to Use Each

| Variant | Use Case |
|---------|----------|
| **InformationPopover** | Help icons, "What's this?" explanations, field descriptions |
| **ActionPopover** | "More actions" menus, bulk action selectors, share menus |
| **ContextPopover** | Right-click menus in data grids, file explorers, canvases |
| **InteractivePopover** | Quick-fill forms, column filters, date range selectors |
| **NestedPopover** | Multi-level menus, cascading selections |

---

## Positioning and Auto-Flip

```typescript
type PopoverPosition =
  | 'top' | 'top-start' | 'top-end'
  | 'bottom' | 'bottom-start' | 'bottom-end'
  | 'left' | 'left-start' | 'left-end'
  | 'right' | 'right-start' | 'right-end';
```

| Behavior | Implementation |
|----------|---------------|
| **Auto-flip** | Flips to opposite side if overflow detected |
| **Shift** | Shifts along the axis to stay within viewport |
| **Boundary** | Configurable boundary element (default: `viewport`) |
| **Margin** | Gap between trigger and popover: 8px |
| **Max height** | 40vh or 400px, whichever is smaller |

---

## Dismiss Behavior

| Action | Default Behavior |
|--------|-----------------|
| Click outside | Dismisses (configurable via `closeOnOutsideClick`) |
| Escape key | Dismisses |
| Click action item | Dismisses (configurable via `closeOnSelect`) |
| Scroll outside | Dismisses (configurable via `closeOnScroll`) |
| Resize | Re-positions, does not dismiss |

---

## Props

### Base Popover Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `open` | `boolean` | — | Controlled open state |
| `defaultOpen` | `boolean` | `false` | Uncontrolled initial state |
| `onOpenChange` | `(open: boolean) => void` | — | Open state change handler |
| `trigger` | `ReactNode` | — | Trigger element |
| `children` | `ReactNode` | — | Popover content |
| `position` | `PopoverPosition` | `'bottom-start'` | Preferred position |
| `align` | `'start' \| 'center' \| 'end'` | `'center'` | Alignment |
| `closeOnOutsideClick` | `boolean` | `true` | Dismiss on outside click |
| `closeOnEscape` | `boolean` | `true` | Dismiss on Escape |
| `closeOnSelect` | `boolean` | `true` | Dismiss on item select |
| `closeOnScroll` | `boolean` | `false` | Dismiss on scroll |
| `openOnHover` | `boolean` | `false` | Open on hover (for InformationPopover) |
| `hoverDelay` | `number` | `300` | Hover open delay (ms) |
| `matchTriggerWidth` | `boolean` | `false` | Match popover width to trigger |
| `maxWidth` | `number \| string` | `320` | Max popover width |
| `maxHeight` | `number \| string` | `400` | Max popover height |
| `zIndex` | `number` | token | Custom z-index |
| `portalTarget` | `HTMLElement` | `document.body` | Portal render target |
| `ariaLabel` | `string` | — | ARIA label for popover panel |

### ContextPopover Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `onContextMenu` | `(event: MouseEvent) => void` | — | Context menu event handler |
| `position` | — | `'right-start'` | Default position for context menus |

### NestedPopover Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `depth` | `number` | `0` | Nesting level (controls offset and z-index) |
| `parentOpen` | `boolean` | — | Parent popover open state |
| `onParentClose` | `() => void` | — | Close parent on Escape chain |

---

## Keyboard Navigation

| Key | Action |
|-----|--------|
| **Escape** | Closes current popover. Nested: closes innermost first. |
| **Tab** | Moves focus to next focusable element within popover. Closes on focus leaving. |
| **Shift+Tab** | Reverse Tab within popover. |
| **Arrow keys** | Navigation within action lists (up/down), nested menu open/close (left/right). |
| **Enter/Space** | Selects focused action item. |
| **Arrow Left** | (Nested) Closes sub-popover and returns focus to parent item. |
| **Arrow Right** | (Nested) Opens sub-popover from parent item. |

---

## Examples

### ActionPopover for "More actions" menu

```tsx
import { ActionPopover } from '@sporekart/ui';

function RowActions({ item }: { item: DataItem }) {
  return (
    <ActionPopover
      trigger={<IconButton aria-label="More actions"><MoreIcon /></IconButton>}
      position="bottom-end"
    >
      <MenuList>
        <MenuItem onClick={() => edit(item)}>Edit</MenuItem>
        <MenuItem onClick={() => duplicate(item)}>Duplicate</MenuItem>
        <MenuSeparator />
        <MenuItem onClick={() => archive(item)} variant="danger">Archive</MenuItem>
      </MenuList>
    </ActionPopover>
  );
}
```

### InformationPopover for help

```tsx
import { InformationPopover } from '@sporekart/ui';

function TaxField() {
  return (
    <Field>
      <Label>
        Tax ID
        <InformationPopover
          trigger={<HelpIcon aria-label="What is this?" />}
          position="right"
          openOnHover
        >
          <p>
            Your Tax Identification Number (TIN) is a 9-digit number
            assigned by the tax authority.
          </p>
        </InformationPopover>
      </Label>
      <Input placeholder="Enter Tax ID" />
    </Field>
  );
}
```

### InteractivePopover for column filter

```tsx
import { InteractivePopover } from '@sporekart/ui';

function ColumnFilter({ column }: { column: Column }) {
  return (
    <InteractivePopover
      trigger={<FilterIcon aria-label={`Filter ${column.name}`} />}
      position="bottom-start"
      closeOnSelect={false}
    >
      <FilterForm>
        <Select label="Operator" options={operators} />
        <Input label="Value" type={column.type} />
        <Button onClick={applyFilter}>Apply</Button>
      </FilterForm>
    </InteractivePopover>
  );
}
```

### ContextPopover for right-click

```tsx
import { ContextPopover } from '@sporekart/ui';

function FileRow({ file }: { file: FileItem }) {
  return (
    <ContextPopover
      trigger={ // ContextPopover wraps the trigger element
        <TableRow>
          <td>{file.name}</td>
          <td>{file.size}</td>
        </TableRow>
      }
      onContextMenu={(e) => console.log('context at', e.clientX, e.clientY)}
    >
      <MenuList>
        <MenuItem onClick={() => open(file)}>Open</MenuItem>
        <MenuItem onClick={() => download(file)}>Download</MenuItem>
        <MenuItem onClick={() => rename(file)}>Rename</MenuItem>
        <MenuSeparator />
        <MenuItem onClick={() => delete(file)} variant="danger">Delete</MenuItem>
      </MenuList>
    </ContextPopover>
  );
}
```

### NestedPopover for multi-level menus

```tsx
import { ActionPopover, NestedPopover } from '@sporekart/ui';

function AddMenu() {
  return (
    <ActionPopover trigger={<Button>Add New</Button>}>
      <MenuList>
        <MenuItem>Document</MenuItem>
        <MenuItem>Spreadsheet</MenuItem>
        <NestedPopover
          trigger={<MenuItem rightIcon={<ChevronRight />}>More</MenuItem>}
          position="right-start"
        >
          <MenuList>
            <MenuItem>Presentation</MenuItem>
            <MenuItem>Form</MenuItem>
            <MenuItem>Diagram</MenuItem>
          </MenuList>
        </NestedPopover>
      </MenuList>
    </ActionPopover>
  );
}
```

---

## Best Practices

- **Trigger clarity**: The trigger element should clearly indicate that clicking will show a popover (chevron icon, "More" text).
- **Don't nest too deep**: Limit nested popovers to 2 levels max. Deeper nesting harms usability and accessibility.
- **Interactive content**: Use InteractivePopover for forms. Don't put forms in ActionPopover — the close-on-select behavior will interfere.
- **Context menus**: Only use ContextPopover for actions relevant to the clicked element. Include standard actions (cut, copy, paste) if applicable.
- **Position preference**: Default to `bottom-start` for menus and `right` for info popovers. Let auto-flip handle edge cases.
- **Height overflow**: Long lists inside popovers should scroll internally (`overflow-y: auto` with max-height).
- **Focus management**: On open, focus the first menu item or the close button. On close, return focus to the trigger.
- **Avoid hover menus**: Use `openOnHover` sparingly — it causes frustration on touch devices and for users with motor disabilities.
