# QA Sprint 2 — Defect Summary

This register tracks all defects, bugs, and issues identified during the execution of QA Sprint 2.

## 1. Classification Guidelines

| Severity | Definition | Target Resolution Time |
|---|---|---|
| **Critical** | Application crash, auth bypass, session hijack, data exposure/loss | Block blocker, resolved immediately |
| **High** | Core functionality broken (broken login, broken logout, OTP failures, guards failed) | Resolved before sprint sign-off |
| **Medium** | Validation messages missing, UI inconsistencies, timer calculations slightly off | Resolved or documented for next phase |
| **Low** | Typos, spacing, styling alignment, minor animation glitches | Backlogged or fixed if trivial |

---

## 2. Active Defect Log

| Bug ID | Part | Feature | Severity | Priority | Description | Steps to Reproduce | Status |
|---|---|---|---|---|---|---|---|
| **BUG-QA-2-001** | Part 1 | Authentication | **Medium** | **High** | Terms Agreement validation error message not rendered in login form | Submit login form with identifier but terms unchecked. Border turns red, but error text 'Please accept...' does not render. | **Open** |
| **BUG-QA-2-002** | Part 1 | Authentication | **Medium** | **High** | Registration consent & privacy validation error messages not rendered | Submit register form with checkbox agreements unchecked. Checkbox borders turn red, but error text is not rendered. | **Open** |
| **BUG-QA-2-003** | Part 2 | Session | **Medium** | **High** | Focus is not managed or trapped inside SessionTimeoutWarning dialog | Trigger session warning overlay. Focus remains on the background and is not trapped inside the overlay dialog. | **Open** |

---

## 3. Defect Statistics

- **Total Logged Defects:** 3
- **Resolved Defects:** 0
- **Open Defects:** 3
- **Critical/High Open Defects:** 0 (Bugs are Medium severity but High priority)
- **Medium Open Defects:** 3
- **Low Open Defects:** 0
- **Part 3 (Authorization & RBAC) Defects:** 0 — No new defects found
