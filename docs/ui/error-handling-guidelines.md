# Error Handling Guidelines — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1C): UX Standards & Accessibility Foundation

> **Status:** Mandatory error patterns. Every error must answer: What happened? Why? How to recover? Reuses Principle 7 (Feedback), Principle 9 (Trust), Principle 10 (Accessibility).

---

## 1. Error Taxonomy

| Category | Source | User-Facing Title | Severity | Auto-Retry? |
|----------|--------|-------------------|----------|-------------|
| **Not Found** | 404, missing entity | "Not found" | Low | No |
| **Forbidden** | 403, RBAC deny | "Access restricted" | Medium | No |
| **Unauthorized** | 401, token expiry | "Session expired" | Medium | No (redirect) |
| **Validation** | 400, 422, Zod/Schema | "Check your input" | Low | No (user fixes) |
| **Conflict** | 409, duplicate key | "Already exists" | Medium | No |
| **Rate Limited** | 429, token bucket | "Too many requests" | Low | Yes (with backoff) |
| **Server Error** | 500, 502, 503 | "Something went wrong" | High | Yes (max 3) |
| **Network** | Offline, DNS, timeout | "Connection lost" | High | Yes (exponential) |
| **Timeout** | Gateway timeout | "Request timed out" | Medium | Yes (max 3) |
| **Business Rule** | 422 custom code | Domain-specific | Varies | No |

---

## 2. Display Patterns by Scope

### 2.1 Page-Level (Full Screen)
**Routes:** 404, 403, 401, 500
```
┌────────────────────────────────────┐
│ [Icon]                             │
│ Title: "Page not found"            │
│ Body: Explanation + guidance       │
│ [Primary CTA]  [Secondary CTA]     │
│                                    │
│ Reference: ERR-20260712-ABC123     │
└────────────────────────────────────┘
```
- Centered, max-width 480px
- Illustrative icon (not emoji in production — SVG)
- `role="alert"` for 500; `role="status"` for 404/403
- Focus on primary CTA on mount

### 2.2 Section-Level (Inline Panel)
**Context:** Failed widget in dashboard, failed tab load
```
┌────────────────────────────────────┐
│ ⚠️  Unable to load analytics       │
│    The metrics service is          │
│    temporarily unavailable.        │
│                                    │
│    [ Retry ]   [ View Cached ]     │
└────────────────────────────────────┘
```
- Replaces panel content
- Amber border, warning icon
- `aria-live="polite"` on mount

### 2.3 Field-Level (Form Validation)
**Context:** Input, select, date picker
```
Email address *
[ user@invalid        ]  ← red border
⚠️  Enter a valid email address
```
- `aria-invalid="true"` on input
- Error linked via `aria-describedby="email-error"`
- `role="alert"` on error element
- Clear on input (debounced 300ms)

### 2.4 Toast (Non-Blocking)
**Context:** Background sync, partial save, async mutation
```
┌────────────────────────────────────┐
│ ✅  Order ORD-123 placed     [×]   │
│    Estimated delivery: Jul 15      │
└────────────────────────────────────┘
```
- Top-right, stack max 3
- Auto-dismiss: Success 4s, Warning 8s, Error: persistent
- `role="status"` (success/info), `role="alert"` (error)
- Keyboard: `Tab` to enter, `Esc` to dismiss

### 2.5 Banner (Persistent)
**Context:** Offline, maintenance, critical warning
```
┌────────────────────────────────────────────────────┐
│ 🌐  Working offline — changes will sync when       │
│    reconnected.                          [Dismiss] │
└────────────────────────────────────────────────────┘
```
- Below header, above breadcrumb
- Dismissible (unless critical)
- `role="status"` + `aria-live="polite"`

### 2.6 Modal Dialog (Blocking)
**Context:** Destructive confirm, critical error with recovery
```
┌────────────────────────────────────┐
│ ⚠️  Delete this order?             │
│                                    │
│ This cannot be undone.             │
│                                    │
│ [Cancel]    [Delete order]         │
└────────────────────────────────────┘
```
- Focus trap, `Esc` closes
- Focus first button on open
- `role="alertdialog"`, `aria-modal="true"`

---

## 3. Recovery Actions by Error Type

| Error | Primary Recovery | Secondary |
|-------|------------------|-----------|
| 404 | "Go to Home" / "Search" | Link to parent section |
| 403 | "Request Access" (email admin) | "Go to Dashboard" |
| 401 | "Sign In" (preserves return URL) | — |
| Validation | Focus first error + inline fix | "Clear form" (if draft) |
| Conflict | "View Existing" (link to entity) | "Edit Existing" |
| Rate Limit | Countdown "Retry in 30s" | Auto-retry when window opens |
| 500 | "Retry" (preserves form) | "Contact Support" (prefills ref) |
| Network | "Retry Now" (manual) | "Work Offline" (if supported) |
| Timeout | "Retry" (preserves input) | "Save as Draft" |

---

## 4. Error Object Contract (API ↔ UI)

```typescript
interface ApiError {
  code: string;                    // "VALIDATION_ERROR", "ORDER_NOT_FOUND"
  message: string;                 // User-facing (localized)
  status: number;                  // HTTP status
  details?: Record<string, string>; // Field → message (validation)
  referenceId: string;             // "ERR-20260712-ABC123" (for support)
  retryable: boolean;              // Client should offer retry
  retryAfter?: number;             // Seconds (rate limit)
}
```

**Frontend maps:** `status` + `code` → Display Pattern + Recovery Actions.

---

## 5. Offline & Retry Strategy

### 5.1 Offline Detection
- `navigator.onLine` + failed fetch → set `isOffline=true`
- Banner appears: "Working offline — changes will sync"
- Mutations queued in IndexedDB (outbox pattern)
- Service Worker serves cached shells

### 5.2 Retry Policy
| Attempt | Delay | Jitter |
|---------|-------|--------|
| 1 | 1s | ±200ms |
| 2 | 2s | ±400ms |
| 3 | 4s | ±800ms |
| **Max** | **3 attempts** | — |

**User-facing:** Toast shows "Retrying… (1/3)" → "Retrying… (2/3)" → Error toast with "Retry Now"

---

## 6. Validation Timing

| Trigger | Behavior |
|---------|----------|
| **On Blur** | Validate field; show error if invalid |
| **On Change (dirty)** | Clear error on valid input (debounced 300ms) |
| **On Submit** | Validate all; focus first error; block submit |
| **Async (unique check)** | Debounced 500ms on blur; show pending spinner |

---

## 7. Accessibility Requirements

- **All errors:** `role="alert"` or `aria-live="assertive"` (page/section) / `aria-live="polite"` (toast)
- **Field errors:** `aria-invalid="true"`, `aria-describedby` linking error
- **Focus:** On error submit → first invalid field; on modal open → first button
- **Color:** Not sole indicator (icon + text + border)
- **Screen reader:** Error announced immediately; toast announced on appear

---

## 8. Logging & Monitoring (Client)

```typescript
// Every error shown to user also logged:
logError({
  code: error.code,
  referenceId: error.referenceId,
  userMessage: error.message,
  context: { route: location.pathname, userRole: getRole() },
  timestamp: Date.now(),
});
```
- Sampled 10% to analytics; 100% to error tracking (Sentry)
- PII scrubbed

---

## 9. Prototype Error Gallery

Route: `/demo/errors` — All patterns:
- [ ] 404 page
- [ ] 403 page (role switcher to test)
- [ ] 401 page
- [ ] 500 page (with reference ID copy)
- [ ] Network offline banner
- [ ] Timeout inline + retry
- [ ] Validation form (all field types)
- [ ] Conflict inline
- [ ] Toast stack (success/warning/error)
- [ ] Delete confirm modal
- [ ] Offline queue indicator