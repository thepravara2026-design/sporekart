# Form Accessibility

## WCAG 2.2 AA Compliance

All form components target WCAG 2.2 AA compliance. Key success criteria addressed:

| SC | Requirement | Implementation |
|---|---|---|
| 1.1.1 | Non-text Content | Icons have `aria-label` or visible text |
| 1.3.1 | Info and Relationships | Semantic HTML, `<fieldset>`, `<legend>` |
| 1.3.2 | Meaningful Sequence | DOM order matches visual order |
| 1.4.1 | Use of Color | Error states also use icons, not just color |
| 1.4.3 | Contrast (Minimum) | 4.5:1 for text, 3:1 for large text |
| 1.4.4 | Resize Text | Forms scale to 200% without loss |
| 1.4.10 | Reflow | Single column at 320px viewport |
| 1.4.12 | Text Spacing | No loss of content with custom spacing |
| 2.1.1 | Keyboard | All controls keyboard operable |
| 2.1.2 | No Keyboard Trap | Focus never trapped except in modals |
| 2.4.3 | Focus Order | Logical tab order |
| 2.4.6 | Headings and Labels | All fields have visible labels |
| 2.4.7 | Focus Visible | 2px outline with offset |
| 2.5.3 | Label in Name | Label text matches accessible name |
| 3.2.1 | On Focus | No context changes on focus |
| 3.2.2 | On Input | No context changes on input |
| 3.3.1 | Error Identification | Errors described in text |
| 3.3.2 | Labels or Instructions | All fields labeled |
| 3.3.3 | Error Suggestion | Suggestions provided where possible |
| 3.3.4 | Error Prevention (Legal/Financial) | Review step before submission |
| 4.1.2 | Name, Role, Value | Custom controls have proper ARIA |
| 4.1.3 | Status Messages | Live regions for form status |

## Semantic HTML

```tsx
<form onSubmit={handleSubmit} noValidate>
  <fieldset>
    <legend>Personal Information</legend>
    <label htmlFor="name">Full Name</label>
    <input id="name" ... />
    <span id="name-error" role="alert">{error}</span>
  </fieldset>
</form>
```

| Element | Usage |
|---|---|
| `<form>` | Root form element |
| `<fieldset>` | Grouping related fields |
| `<legend>` | Description for fieldset |
| `<label>` | Every input has a label |
| `<button>` | Submit/cancel actions |

## ARIA Attributes

| Attribute | Element | Condition |
|---|---|---|
| `aria-invalid` | Input | `true` when field has error |
| `aria-describedby` | Input | Links to helper text or error |
| `aria-errormessage` | Input | Links to error message |
| `aria-required` | Input | `true` when field is required |
| `aria-label` | Icon-only button | Describes action |
| `aria-live="polite"` | Error container | Announces errors |
| `aria-live="assertive"` | Submission status | Announces form result |
| `role="alert"` | Error message | Immediate announcement |
| `aria-current="step"` | Step indicator | Active step |
| `aria-expanded` | Collapsible section | Open/closed state |
| `role="progressbar"` | Upload progress | Progress value |

## Keyboard Navigation Patterns

| Pattern | Implementation |
|---|---|
| Tab order | Logical DOM order matching visual |
| Focus indicators | 2px solid outline, 2px offset, `:focus-visible` |
| Skip links | Skip to form, skip to first error |
| Auto-focus | First field on mount (sparingly) |
| Focus trapping | Modal/popup selects only |
| Return focus | Restore focus after popup/modal |

```css
:focus-visible {
  outline: 2px solid var(--color-primary);
  outline-offset: 2px;
}
```

## Focus Management

- First field receives focus on form mount (or step change)
- First invalid field receives focus on validation failure
- Submit button receives focus after successful submission
- Cancel returns focus to trigger element
- Modal forms trap focus within the modal

## Screen Reader Support

| Component | ARIA Pattern |
|---|---|
| Select | `combobox` + `listbox` + `option` |
| MultiSelect | `combobox` + `listbox` + `aria-multiselectable` |
| DropZone | `button` role, `aria-label` |
| FilePreview | `aria-label` on remove button |
| StepIndicator | `list` + `listitem` + `aria-current` |
| ValidationSummary | `alert` role, links to fields |
| Upload progress | `progressbar` with `aria-valuenow` |

## Error Announcement Patterns

```tsx
// Field error
<div id={`${name}-error`} role="alert" aria-live="polite">
  {error}
</div>

// Form summary
<div role="alert" aria-live="assertive" aria-atomic="true">
  {`${count} error(s) found. First error: ${firstError}`}
</div>

// Submission status
<div role="status" aria-live="polite" aria-atomic="true">
  {isSubmitting ? 'Submitting...' : isSuccess ? 'Submitted successfully' : ''}
</div>
```

## Reduced Motion Support

```css
@media (prefers-reduced-motion: reduce) {
  .form-section-collapse {
    transition: none;
  }
  .step-indicator {
    transition: none;
  }
  .dropzone-drag-over {
    transition: none;
  }
}
```

## High Contrast Support

- All form controls visible in Windows High Contrast Mode
- Focus indicators use outlines (not box-shadow)
- Error states include icon + text (not color alone)
- Required indicators use asterisk + text
- Progress bars have visible value text

## Testing Procedures

| Test | Tool / Method |
|---|---|
| Keyboard navigation | Manual: Tab, Enter, Escape, Arrows |
| Screen reader | NVDA (Windows), VoiceOver (macOS) |
| Focus order | Tab through all interactive elements |
| Color contrast | Axe DevTools, WCAG Contrast Checker |
| Zoom | 200% browser zoom, 400% text zoom |
| Reduced motion | OS-level prefers-reduced-motion |
| High contrast | Windows High Contrast Mode |
| Form validation | Axe, manual error triggering |
| ARIA validation | Axe DevTools, WAVE |
