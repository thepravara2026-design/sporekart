# Bug Fix Sprint D — Remaining Backlog

**Date:** 2026-07-18
**Context:** Items that could NOT be closed as P3 polish within Sprint D because they
require feature builds or test-maintenance decisions outside the sprint mandate.

---

## A. Test-Suite / Codebase Architecture Mismatch (BLOCKER for RC1)

**Severity:** HIGH (release-gating)
**Type:** Test maintenance / scope decision — NOT a code defect
**Owner:** QA Lead + Engineering Lead (Approval Gate D)

### Affected specs (25 failures observed on chromium)
- `tests/rbac-authorization.spec.ts` — 22 failures
- `tests/protected-routes.spec.ts` — 3 failures
- `tests/customer-journey-product-details.spec.ts` — 3 failures

### Root cause
The specs assert an **aspirational/legacy architecture** that is not in this build:

| Test Expects | Shipped Reality |
|--------------|-----------------|
| `select[aria-label="Switch review role"]` with 9 options | Header has `select[aria-label="Preview role"]` (guest-only, security-hardened) |
| Routes `/settings`, `/account`, `/catalog`, `/cms`, `/governance`, `/ai` | Real routes are `/dashboard/*`, `/admin/*`, `/training/*` |
| "Access restricted" panel text | Unauthorized roles redirect to `/access-denied` |
| Sidebar label "Administration"/"CMS"/"Governance" groups per role | Sidebar uses "Discover/Operate/Intelligence/Platform" groups; `getVisibleWorkspaces(role)` filters correctly |
| Product detail at `/product/:slug`, `/products/:slug` | Only `/products` category grid exists (documented placeholder) |

### Options for Approval Gate D
1. **Reconcile tests to shipped architecture** (recommended): update the 3 specs to
   real routes, the real role-switcher label, and the real `/access-denied` flow.
   Low risk, unblocks RC1 qualification.
2. **Build the aspirational routes** (`/settings`, `/account`, `/catalog`, product
   detail pages, full RBAC review switcher): large feature scope — **out of Sprint D**.
3. **Quarantine/remove** the mismatched specs and re-baseline QA Sprint 5.

---

## B. Product Detail Pages (Feature Build)

**Severity:** MEDIUM (customer-journey completeness)
**Type:** New feature — explicitly OUT of P3 polish scope
**Register ref:** BUG-QA4-HIGH-004

The public catalog has no `/product/:slug` detail page (image gallery, price,
description, quantity selector, add-to-cart). The `ProductsPage.tsx` is a category
grid with a documented placeholder notice. Building detail pages requires:
- A product data model / mock dataset
- Route + page component
- Gallery, price, description, breadcrumb, add-to-cart wiring
- (eventually) cart/checkout integration

**Recommendation:** Schedule for a post-RC1 sprint. Not a blocker if RC1 is scoped
to the navigation/workspace prototype (which the app explicitly states: *"This is a
navigation prototype. Production content… arrives in later Sprint 19 parts."*).

---

## C. Cross-Browser Verification Environment

**Severity:** MEDIUM (process gap)
**Type:** Infrastructure

Firefox and WebKit browsers are installed (`firefox-1532`, `webkit-2311` present)
but **launch hangs in this environment** even for a trivial chromium-passing smoke
test. This is an environment issue, not an app defect, but it blocks the
cross-browser smoke required by the Sprint D brief.

**Action:** Re-run `cross-browser.spec.ts` (firefox, webkit, mobile) in a capable
CI runner before RC1. Do NOT treat the hang as BUG-AUTH-001 (no `:has()` exists).

---

## D. Minor Polish Candidates (verified NOT present — informational only)

These were checked and found already satisfied; listed so reviewers know they were
not missed:
- Footer contrast (WCAG pass)
- Training page `<title>` (set via SEO)
- Deprecated React Router `component` prop (not used)

---

## Backlog Priority for Approval Gate D

| Rank | Item | Effort | Blocker? |
|------|------|--------|----------|
| 1 | Reconcile 3 mismatched test specs | 0.5–1 day | YES (RC1 qualification) |
| 2 | Cross-browser env fix + re-run | 0.5 day | Process |
| 3 | Product detail pages (feature) | 5–8 days | Post-RC1 |
