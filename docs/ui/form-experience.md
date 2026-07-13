# Form Experience Standards — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1C): UX Standards & Accessibility Foundation

> **Status:** Mandatory patterns. Every form in the application follows these rules. Reuses Principle 3 (Minimal Cognitive Load), Principle 6 (Forgiveness), Principle 10 (Accessibility).

---

## 1. Field Anatomy

```
┌─────────────────────────────────────────────────────────────┐
│  LABEL (required)                    [Optional: Helper]     │
│  ┌─────────────────────────────────────────────────────┐    │
│  │ INPUT                                                │    │
│  └─────────────────────────────────────────────────────┘    │
│  ⚠  ERROR MESSAGE (if invalid)  or  ✓  HINT (if valid)      │
└─────────────────────────────────────────────────────────────┘
```

| Part | Required? | Spec |
|------|-----------|------|
| Label | **Yes** | `<label for="id">` — visible; `required` → asterisk in label |
| Helper Text | Optional | Below label, `--color-text-secondary`, 13px |
| Input | **Yes** | `id` matches label `for`; `aria-describedby` for helper/error |
| Error | Conditional | `role="alert"`, `aria-live="polite"`, linked via `aria-describedby` |
| Hint (success) | Optional | Green check + "Looks good" — only for complex async validation |

---

## 2. Required Fields

- **Asterisk in label:** `Email *` (not in placeholder)
- **`aria-required="true"`** on input
- **`required` attribute** for native validation backup
- **Never** rely on placeholder for required indication

---

## 3. Validation Timing

| Trigger | Fields | Behavior |
|---------|--------|----------|
| **On Blur** | All | Validate format, required, length. Show inline error. |
| **On Change (debounced 300ms)** | Async (email unique, PIN format, API checks) | Show spinner in field; show result. |
| **On Submit** | All + cross-field | Prevent submit; focus first error; scroll to it. |

**Rule:** Never validate on keystroke (except character count). Debounce async.

---

## 4. Error Message Standards

| Error Type | Template | Example |
|------------|----------|---------|
| Required | "[Field] is required" | "Email is required" |
| Format | "Enter a valid [format]" | "Enter a valid email address" |
| Length (min) | "[Field] must be at least [N] characters" | "Password must be at least 8 characters" |
| Length (max) | "[Field] must be no more than [N] characters" | "Bio must be no more than 500 characters" |
| Pattern | "[Field] must [requirement]" | "PIN must be 6 digits" |
| Async (taken) | "This [field] is already in use" | "This email is already registered" |
| Async (not found) | "No [resource] found with this [field]" | "No account found with this email" |
| Cross-field | "[Field A] must be [relation] [Field B]" | "End date must be after start date" |
| Range | "[Field] must be between [min] and [max]" | "Quantity must be between 1 and 100" |

**Tone:** Direct, helpful, no "Please" or "Oops". See microcopy-guidelines.md.

---

## 5. Field Types — Specific Patterns

### 5.1 Text / Email / Tel / URL
- `inputmode` + `type` appropriate
- `autocomplete` values (email, tel, url, username, current-password, new-password)
- Clear button (×) on focus with value

### 5.2 Password
- Toggle visibility (👁/👁‍🗨) — button, not icon-only
- Strength meter (optional) — 4 segments, labels: Weak/Fair/Good/Strong
- `autocomplete="new-password"` / `"current-password"`

### 5.3 Select / Combobox
- Native `<select>` for ≤7 options, static
- **Combobox (autocomplete)** for >7 or searchable — ARIA 1.2 pattern
- `aria-expanded`, `aria-activedescendant`, keyboard: Arrow keys, Enter, Escape

### 5.4 Radio Group / Checkbox Group
- `<fieldset>` + `<legend>` = group label
- Each `<input>` + `<label>` wrapped or `for/id`
- Vertical stack (mobile), horizontal (desktop ≥3 options)
- Checkbox: `indeterminate` for "Select all" parent

### 5.5 Date / Date Range
- **Single date:** Native `<input type="date">` + calendar button (popover)
- **Range:** Two date inputs (From / To) + preset chips (Last 7d, Last 30d, Custom)
- `autocomplete="bday-day"` etc. for native support
- Keyboard: Arrow keys in popover, Enter to select, Escape to close

### 5.6 File Upload
- Drag-drop zone + "Browse" button
- Accept attribute; max size shown
- Preview chips for selected (image: thumbnail; file: name + size)
- Remove button per file; "Clear all"
- Progress during upload; pause/cancel

### 5.7 OTP / PIN (6-digit)
- **6 separate inputs** (not one masked) — auto-advance on digit
- Paste support (fills all 6)
- Arrow keys navigate; Backspace clears + moves back
- `inputmode="numeric"` `autocomplete="one-time-code"`
- Accessibility: each has `aria-label="Digit 1"` etc.

### 5.8 Address Form
- **Smart defaults:** Country → state/province → city cascading
- `autocomplete`: `street-address`, `address-level2` (city), `address-level1` (state), `postal-code`, `country`
- "Use current location" button (geolocation API, opt-in)
- International: address format per country (library)

### 5.9 Checkout Form
- **Steps:** Contact → Shipping → Payment → Review
- Each step: own URL (`/checkout/contact`, `/checkout/shipping`...) for refresh safety
- Progress indicator (stepper) — current step highlighted
- Auto-save draft per step (localStorage + server)
- Payment: Stripe Elements / secure iframe — no raw card data in app

---

## 6. Auto-Save & Draft Recovery

| Trigger | Action |
|---------|--------|
| **On blur** (any field) | Save draft to localStorage (key: `draft:{route}:{userId}`) |
| **On change** (debounced 1s) | Update draft |
| **On beforeunload** | Final sync |
| **On mount** | If draft exists & < 24h old → show banner "Continue where you left off?" with "Restore" / "Discard" |
| **On submit success** | Clear draft |

**Draft data:** Encrypted (Web Crypto API) if sensitive; otherwise plain JSON.

---

## 7. Submission Flow

```
User clicks Submit
       │
       ▼
Disable submit button, show spinner
       │
       ▼
Client validation pass?
       │
       ├─ No → Focus first error, re-enable button
       │
       └─ Yes → POST /api/...
                     │
                     ├─ 2xx → Success toast, navigate/clear, clear draft
                     │
                     ├─ 400 (validation) → Map errors to fields, focus first, re-enable
                     │
                     ├─ 409 (conflict) → Inline error on field + "View existing" link
                     │
                     ├─ 422 (business rule) → Toast + inline if field-specific
                     │
                     └─ 5xx / network → Toast "Something went wrong. Retry?" + preserve input
```

**Double-submit protection:** Button disabled after first click; idempotency key in header.

---

## 8. Keyboard & Accessibility

- **Tab order:** Label → Input → Helper/Error (logical DOM order)
- **Enter in field:** Submits form (except textarea, multiline select)
- **Escape:** Closes popovers (date, combobox), clears file preview focus
- **Arrow keys:** Navigate radio, checkbox group, date picker, combobox options
- **Screen reader:** Error announced via `role="alert"`; success via `role="status"`
- **Focus on error:** `input[aria-invalid="true"]:first-of-type` focused

---

## 9. Responsive Forms

| Breakpoint | Layout |
|------------|--------|
| xs/sm | Single column, full-width inputs, stacked buttons |
| md | Two-column for related pairs (First/Last name, City/State) |
| lg+ | Max field width 480px; labels above (not inline) |

---

## 10. Prototype Form Gallery

Route: `/demo/forms` — All patterns: basic, async validation, OTP, address, checkout steps, file upload, date range, combobox, auto-save demo, error mapping, keyboard nav.