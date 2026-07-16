# Enterprise Training Pricing, Enrollment & Capacity Management Platform

Phase 11 · Sprint 26 · Part 7 — the commercial and operational architecture governing training
enrollment for the SporeKart Enterprise LMS. This is **not** a payment gateway. It runs entirely in
**Mock Mode** — no payment provider, no APIs, no database, no real transactions. The platform is
payment-provider agnostic and integrates with future billing/finance/ERP/CRM/notification systems
through well-defined extension interfaces.

---

## 1. Enterprise Pricing Architecture

Pricing is modeled by `PricingConfig` bound to each `CourseCommerce` record.

**Pricing models (`PricingModel`)**: free, paid, premium, workshop, corporate, institution,
government (sponsored), scholarship, internal, and future subscription / membership / bundle / dynamic.

**Pricing configuration fields**: baseFee, enrollmentFee, registrationFee, currency (INR/USD/EUR),
visibility (public/private/internal), plus placeholders for discount, GST, early-bird, late
registration, refund policy, cancellation policy, and future installments.

All monetary values are mock. `formatCurrency()` centralizes display (Free when 0).

## 2. Enrollment Workflow

```
Applicant → Enrollment Request → Eligibility Evaluation (mock) → Policy Gate
   → [Open] auto-confirm         → Confirmed
   → [Admin/Corporate/Institution approval] → Pending → Approved → Confirmed
   → [No seat] → Waitlisted → (promotion) → Confirmed
   → Rejected / Cancelled / Expired
   → Completed → Archived
```

Enrollment policies (`EnrollmentPolicyType`): open, admin-approval, invitation, corporate-approval,
institution-approval. Each policy carries an enrollment window, registration deadline, prerequisite
validation flag, eligibility rule bindings, and a max-attempts placeholder.

Admin actions in the Enrollment Dashboard (`EnrollmentTable`) transition requests via
`setRequestStatus` (Approve / Reject) — state-only, no persistence.

## 3. Capacity Management Model

`CapacityConfig` partitions seats into **maxSeats**, **minSeats**, **availableSeats**,
**reservedSeats**, **occupiedSeats**, **blockedSeats**, with future flags for overflow, multi-batch
and parallel-batch allocation. `availableSeats = max - occupied - reserved - blocked`.

`CapacityMeter` renders a segmented, ARIA-labelled seat-allocation bar (occupied/reserved/blocked/
available) reused by the Pricing and Capacity dashboards.

## 4. Registration Lifecycle

Canonical state machine (`REGISTRATION_LIFECYCLE`):

`not-open → open → pending → approved → confirmed → waitlisted → rejected → cancelled → completed → expired → archived`

Each state has a label, a badge variant (`STATUS_BADGE_VARIANT`), and a description. The Lifecycle
panel shows live request counts per stage.

## 5. Eligibility Engine

Reusable, criterion-based rules (`EligibilityRule`) across criteria: age, education, previous-course,
experience, certification, location, corporate, institution, prerequisites. Each rule has a condition
expression, required flag, and **mock** result (pass/fail/warning). Validation is mock-only.

## 6. Folder Structure

```
course-enrollment/
  data/
    enrollmentMockData.ts       # all domain types, enums, labels, options, mock data
  state/
    useEnrollmentState.ts       # useReducer store + action creators
    EnrollmentContext.tsx       # provider + useEnrollmentContext hook
  components/
    EnrollmentLayout.tsx        # header + sidebar + section→panel switch
    shared/
      EnrollmentSidebar.tsx     # 9-section role="tab" navigation
      CommerceToolbar.tsx       # search + reusable filter selects
      useFilteredCourses.ts     # filtering + pagination selector hook
    panels/
      OverviewPanel.tsx         # commerce dashboard
      PricingPanel.tsx          # pricing cards + pagination
      EnrollmentPanel.tsx       # enrollment request table
      CapacityPanel.tsx         # capacity meters + pagination
      PoliciesPanel.tsx         # enrollment policies + bindings
      EligibilityPanel.tsx      # eligibility rules engine
      LifecyclePanel.tsx        # registration lifecycle state machine
      WaitlistPanel.tsx         # waitlist queues + promotion
      PaymentReadinessPanel.tsx # future payment/commerce interfaces
    visualization/
      PricingCard.tsx
      CapacityMeter.tsx
      EnrollmentTable.tsx
      StatusBadge.tsx
    widgets/
      CommerceDashboardWidgets.tsx
```

## 7. Component Inventory

- **Layout/Nav**: EnrollmentLayout, EnrollmentSidebar (9 `role="tab"` sections)
- **Panels (9)**: Overview, Pricing, Enrollment, Capacity, Policies, Eligibility, Lifecycle,
  Waitlist, PaymentReadiness
- **Visualization (4)**: PricingCard, CapacityMeter, EnrollmentTable, StatusBadge
- **Widgets (1)**: CommerceDashboardWidgets (paid/free/seats/requests/approved/pending/waitlisted/
  cancelled/revenue-placeholder + popular + upcoming)
- **Shared (2)**: CommerceToolbar, useFilteredCourses

## 8. State Management Report

`useReducer`-based store (`useEnrollmentState`) exposed via React Context. State: section, courses,
requests, waitlist, eligibilityRules, search, filters, selectedCourseId, page, pageSize. Actions:
SET_SECTION, SET_SEARCH, SET_FILTERS, RESET_FILTERS, SET_SELECTED_COURSE, SET_PAGE,
SET_REQUEST_STATUS, PROMOTE_WAITLIST. Mock data is confined to the `data/` layer; no hardcoded data
in components.

## 9. Responsive Validation Report

- Toolbar filters use `flex-wrap` and per-control min widths → reflow on tablet/mobile.
- Card grids use `repeat(auto-fill/auto-fit, minmax(...))` → fluid columns across breakpoints.
- Tables wrapped in `overflow-x: auto` for horizontal scroll on small screens.
- Layout: fixed 220px sidebar + fluid main content area with independent scroll.

## 10. Accessibility Report (WCAG 2.2 AA)

- Sidebar buttons use `role="tab"` + `aria-selected`; main region `role="tabpanel"` with `aria-label`.
- `EnrollmentTable` uses `scope="col"` headers, a visually-hidden `<caption>`, and per-row action
  `aria-label`s.
- `CapacityMeter` exposes `role="img"` with a descriptive `aria-label` summarizing seat allocation.
- Status/priority conveyed via text labels in addition to color (Badges render text).
- Inputs/Selects reuse accessible design-system components with labels/aria-labels.
- Motion limited to design-system hover transitions (honor reduced-motion at DS level).

## 11. Performance Optimization Report

- `EnrollmentLayout` is route-split via `React.lazy()` in `App.tsx`.
- Filtering, pagination and dashboard aggregations use `useMemo`.
- Action creators memoized with `useCallback` in the state hook.
- Client-side pagination (reused admin `Pagination`) limits rendered rows/cards (pageSize 6),
  preparing for thousands of future enrollments.

## 12. Future Commerce & Payment Integration Readiness

The Payment Readiness panel documents extension interfaces (design only, no implementation):
`PaymentProvider`, `TaxEngine`, `DiscountEngine`, `InvoiceService`, `NotificationChannel`, with
provider adapters (Razorpay, UPI, Net Banking, Card, Corporate Billing) and services (GST, Invoice,
Coupons, Promo Codes, Scholarship, CRM, ERP, Notifications — Email/SMS/WhatsApp).

---

## Routing

Wired in `src/App.tsx` under the training workspace (only `App.tsx` modified among existing files):

- `/admin/training/enrollment` → `TrainingEnrollmentPage` (EnrollmentLayout)
- `/admin/training/enrollment/:section` → same layout

Lazy-loaded via `React.lazy()` + `.then(m => ({ default: m.EnrollmentLayout }))`.

## Reuse & Non-duplication

- **Pagination**: reuses `src/admin/components/navigation/Pagination`.
- **UI**: reuses design-system Card, Badge, Button, Input, Select, Stack.
- Search & filters implemented via shared `CommerceToolbar` + `useFilteredCourses` (no duplicate
  frameworks). No modifications to Auth, RBAC, Products, Orders, Inventory, Warehouse, Checkout,
  Customer site, Admin dashboard, Navigation, or the Design System.

## Quality Gate

- `tsc --noEmit` passes with zero errors.
- Zero regressions in Sprint 26 Parts 1–6; Inventory/Warehouse/Customer platforms unaffected.
- Zero duplicate components; mock data isolated to the `data/` layer.
