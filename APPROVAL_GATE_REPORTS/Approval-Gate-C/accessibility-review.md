# Approval Gate C — Accessibility Review

## WCAG 2.1 AA Compliance

### Perceivable
| Guideline | Status | Notes |
|-----------|--------|-------|
| 1.1.1 Non-text Content | ✅ | All icons have `aria-hidden="true"` or alt text |
| 1.3.1 Info and Relationships | ✅ | Landmarks, headings, lists used appropriately |
| 1.3.2 Meaningful Sequence | ✅ | DOM order matches visual order |
| 1.4.1 Use of Color | ✅ | Status dots have `aria-label` text |
| 1.4.3 Contrast (Minimum) | ✅ | CSS custom properties with sufficient ratios |
| 1.4.4 Resize Text | ✅ | Uses relative units (rem, var) |
| 1.4.10 Reflow | ✅ | Mobile responsive with no horizontal scroll |
| 1.4.12 Text Spacing | ✅ | No `!important` overrides on text styles |

### Operable
| Guideline | Status | Notes |
|-----------|--------|-------|
| 2.1.1 Keyboard | ✅ | All interactive elements are keyboard-reachable |
| 2.1.2 No Keyboard Trap | ✅ | Sidebar backdrop closes with click; no focus trap |
| 2.4.1 Bypass Blocks | ✅ | Skip-to-content link present |
| 2.4.2 Page Titled | ✅ | Each route has a title |
| 2.4.3 Focus Order | ✅ | Logical tab order |
| 2.4.4 Link Purpose (In Context) | ✅ | "Go home", "Dashboard", "Help centre" |
| 2.4.6 Headings and Labels | ✅ | Proper h1-h2 structure |
| 2.4.7 Focus Visible | ✅ | `:focus-visible` style with 3px outline |
| 2.5.3 Label in Name | ✅ | Button labels match accessible names |

### Understandable
| Guideline | Status | Notes |
|-----------|--------|-------|
| 3.1.1 Language of Page | ✅ | `<html lang="...">` already present |
| 3.2.3 Consistent Navigation | ✅ | Header/sidebar/footer layout consistent |
| 3.2.4 Consistent Identification | ✅ | Same icons for same actions |
| 3.3.1 Error Identification | ✅ | `role="alert"` on error messages |
| 3.3.2 Labels or Instructions | ✅ | aria-label on all interactive elements |

### Robust
| Guideline | Status | Notes |
|-----------|--------|-------|
| 4.1.1 Parsing | ✅ | Valid HTML structure |
| 4.1.2 Name, Role, Value | ✅ | All custom controls have proper ARIA roles |
| 4.1.3 Status Messages | ✅ | `aria-live="polite"` on notification region |

## Sprint C Accessibility Changes

| Item | Change | Impact |
|------|--------|--------|
| C-003 | ARIA landmarks test | Ensures regression prevention |
| C-004 | Backdrop `aria-hidden="true"` | Properly hidden from AT |
| C-007 | Notification region `aria-live="polite"` | Screen reader announcements |
| C-008 | 404 page redesign | `role="alert"` on persistent content ⚠️ |

## Observations
1. **C-008 (404 page)**: `role="alert"` should be `role="status"` for persistent content. `role="alert"` is reserved for time-sensitive, important messages that interrupt the user. A 404 page is navigated to intentionally and is not a transient alert.
2. **Sidebar backdrop**: The `aria-hidden="true"` is correct, but there is no focus trap when the mobile sidebar is open — users can Tab behind the backdrop. This is consistent with the existing behavior (pre-Sprint C) and not a regression.

## Verdict
✅ **Accessibility maintained with minor observation**. No WCAG violations introduced. The C-008 `role="alert"` observation is a non-blocking best-practice issue.
