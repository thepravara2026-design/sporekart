# Condition C1 — Playwright Specification Reconciliation — CLOSURE REPORT

**Status:** ✅ CLOSED
**Constraint honored:** Test suites only. No application source code modified.

---

## 1. Mandate

Reconcile the following Playwright suites to the currently shipped application:
- `rbac-authorization.spec.ts`
- `protected-routes.spec.ts`
- `customer-journey-product-details.spec.ts`

Remove obsolete selectors, remove obsolete routes, update expectations. Do NOT
modify application source code.

## 2. Ground-Truth Extraction (shipped application)

Source of truth was read directly from `frontend/web-app/src` (read-only):

| Fact | Shipped reality | Source |
|------|-----------------|--------|
| Role switcher selector | `select[aria-label="Preview role"]` | `components/layout/Header.tsx:65-79` |
| Role switcher visibility | Rendered **only while `activeRole === 'guest'`** (BUG-SEC-005) | `Header.tsx:65` |
| Roles (value→label) | guest→Guest, customer→Customer, grower→Grower, trainer→Trainer, distributor→Distributor, support→Support, administrator→Administrator, business_owner→Business Owner, governance_manager→Governance Manager | `config/roles.ts` |
| Default role | `guest` | `App.tsx:428-436` |
| Sidebar nav | `nav.sk-sidebar`; labels `.sk-workspace-link__label`; group labels `.sk-sidebar__group-label` | `components/layout/Sidebar.tsx` |
| Group labels | Discover / Operate / Intelligence / Platform | `Sidebar.tsx:6-13` |
| Guest visible workspaces | Public, Training, Demo, Design System (public only) | `config/navigation.ts` |
| Unauthorized access | Redirect to `/access-denied` (title **"Access denied"**); inline "Access restricted" removed (BUG-RT-007) | `WorkspacePage.tsx:25-33`, `RequireAuth.tsx:22-34`, `SessionPages.tsx:67-78` |
| Guarded `/dashboard`,`/admin` | Guest → redirect `/login`; non-permitted role → `/access-denied` | `RequireAuth.tsx` |
| Customer home route | `/dashboard` (there is **no** `/account` page render for authorized content); enterprise `/account` resolves via WorkspacePage and denies guests | `App.tsx`, `navigation.ts` |
| Product detail | Single route `/products/:id`; renders WorkspacePage prototype, h1 `"<workspace>: <id>"` (e.g. `Public: 1`), subtitle "Single product view.", panel "Product detail — empty panel". **No** commerce detail (no ₹, gallery, qty, add-to-cart, breadcrumb detail). Routes `/product/1`, `/p/1`, `/product/test`, `/products/sample` do **not** exist. | `WorkspacePage.tsx`, `navigation.ts:23` |
| Responsive sidebar | On tablet/mobile the sidebar is an off-canvas drawer (hidden until `sk-sidebar--open`); always attached to DOM | `Sidebar.tsx` |

## 3. Obsolete Elements Removed

**rbac-authorization.spec.ts**
- ❌ `select[aria-label="Switch review role"]` → ✅ `select[aria-label="Preview role"]`.
- ❌ Per-role sidebar matrix (9 roles) that assumed the switcher persists across
  role changes — impossible since the switcher is guest-only. Replaced with a
  guest-scoped public-workspace visibility assertion.
- ❌ `expect(selectedRole).toBe('administrator')` after refresh (contradictory /
  never-true). Removed.
- ❌ Assertions that admin/customer routes render without redirect. Replaced with
  the shipped redirect behavior (`/access-denied` or `/login`).
- ❌ Pixel-visibility assertions on off-canvas sidebar labels → `toBeAttached()`
  (viewport-independent), fixing tablet/mobile projects.

**protected-routes.spec.ts**
- ❌ `text=Access restricted` and `text=Your current role (x) cannot view this page.`
  (removed from the app by BUG-RT-007) → ✅ `/access-denied` redirect + "Access denied".
- ❌ `text=Account home` expectation (not shipped) → replaced with public-route
  reachability and guarded-route redirect checks.

**customer-journey-product-details.spec.ts**
- ❌ Route probing across `/product/1`, `/products/1`, `/p/1`, `/product/test`,
  `/products/sample` → ✅ single shipped route `/products/1` (`/products/:id`).
- ❌ Expectations for price (₹), image gallery, quantity selector, add-to-cart,
  breadcrumb detail (none shipped) → ✅ shipped navigation-prototype assertions.

## 4. Verification (executed)

Build: `vite build` PASS. Preview served at `http://localhost:4173`.

| Engine | Tests | Result |
|--------|-------|--------|
| chromium | 25 | ✅ PASS |
| webkit | 25 | ✅ PASS |
| mobile-chrome | 25 | ✅ PASS |
| mobile-safari | 25 | ✅ PASS |
| tablet | 25 | ✅ PASS |
| **Total** | **125** | **✅ 125/125 PASS** |

Isolated report: `QA_REPORTS/Release-Condition-Closure/C3-cross-browser/`.

## 5. Files Changed (test-only)

- `shared-testing/tests/rbac-authorization.spec.ts`
- `shared-testing/tests/protected-routes.spec.ts`
- `shared-testing/tests/customer-journey-product-details.spec.ts`

**No files under `frontend/web-app/src` were modified.**

## 6. Result

C1 is **CLOSED**. All three suites reflect the shipped route/selector
architecture and pass cross-browser.
