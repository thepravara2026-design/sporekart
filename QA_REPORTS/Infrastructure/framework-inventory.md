# Framework Inventory Report

**Pre-QA Sprint 2** | **Date:** 2026-07-17

---

## Test Framework

| Item | Status | Path |
|------|--------|------|
| **Playwright** | ✅ | `shared-testing/node_modules/@playwright/test` |
| **Playwright config** | ✅ | `shared-testing/playwright.config.ts` |
| **Global setup** | ✅ CREATED | `shared-testing/global-setup.ts` |
| **Global teardown** | ✅ CREATED | `shared-testing/global-teardown.ts` |

## Test Spec Files (15 total)

| File | QA Part | Status |
|------|---------|--------|
| `tests/auth-validation.spec.ts` | Part 1 — Auth | ✅ Existing |
| `tests/debug-login.spec.ts` | Part 1 — Debug | ✅ Existing |
| `tests/session-management.spec.ts` | Part 2 — Session | ✅ Existing |
| `tests/rbac-authorization.spec.ts` | Part 3 — RBAC | ✅ Existing |
| `tests/protected-routes.spec.ts` | Part 4 — Routes | ✅ Existing |
| `tests/auth-apis.spec.ts` | Part 5 — APIs | ✅ Existing |
| `tests/cross-browser.spec.ts` | Part 6 — Browser | ✅ Existing |
| `tests/mobile-responsive.spec.ts` | Part 7 — Mobile | ✅ Existing |
| `tests/accessibility.spec.ts` | Part 8 — A11y | ✅ Existing |
| `tests/performance.spec.ts` | Part 9 — Perf | ✅ Existing |
| `tests/security-validation.spec.ts` | Part 10 — Security | ✅ Existing |
| `tests/regression.spec.ts` | Part 11 — Regression | ✅ Existing |
| `tests/qa-foundation.spec.ts` | Foundation | ✅ Existing |
| `tests/mock-validation.spec.ts` | Mock validation | ✅ Existing |
| `tests/smoke.spec.ts` | ✅ CREATED (infra smoke) | New |

## Project Structure (CREATED)

| Directory | Purpose | Status |
|-----------|---------|--------|
| `fixtures/` | Playwright test fixtures | ✅ CREATED (empty — ready for custom fixtures) |
| `helpers/` | Reusable test helpers | ✅ CREATED |
| `utils/` | Utility functions | ✅ CREATED |
| `page-objects/` | Page Object models | ✅ CREATED |
| `mock-data/` | Mock datasets | ✅ CREATED |
| `reporters/` | Custom reporters | ✅ CREATED (empty) |

## Helpers Created

| File | Purpose |
|------|---------|
| `helpers/auth.ts` | Login/logout/role-set helpers for all personas |
| `helpers/navigation.ts` | Page navigation, URL assertions, sidebar clicks |
| `helpers/assertions.ts` | Common assertions (visible, text, count, title) |
| `helpers/api-mock.ts` | API mocking (responses, errors, delays, network) |

## Page Objects Created

| File | Purpose |
|------|---------|
| `page-objects/LoginPage.ts` | Login flow with phone, OTP, terms |

## Mock Data Created

| File | Contents |
|------|---------|
| `mock-data/personas.ts` | 10 personas (guest through governance_manager) |
| `mock-data/products.ts` | 8 products across seeds, fertilizers, etc. |
| `mock-data/orders.ts` | 3 orders with line items |
| `mock-data/addresses.ts` | 3 addresses across 2 customers |
| `mock-data/training.ts` | 4 courses + 4 students |
| `mock-data/notifications.ts` | 3 notifications |

## Utilities Created

| File | Purpose |
|------|---------|
| `utils/env.ts` | Environment helpers (base URL, mock auth token, timeout) |
