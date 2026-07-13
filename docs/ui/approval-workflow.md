# Approval Workflow — SporeKart Enterprise AI Platform
## Phase 5 / Sprint 19 (Part 1A): Enterprise Product Experience Foundation

> **Status:** Documentation Only. Defines roles, gates, preview-route requirement,
> statuses, and the sign-off record that governs every page.

---

## 1. Roles and Responsibilities

| Role | Responsibility in Approval |
| --- | --- |
| **CPO** (Chief Product Officer) | Owns product vision alignment; final business sign-off. |
| **CXO** (Chief Experience Officer) | Owns experience quality; brand and principles sign-off. |
| **Design** | Owns design-system adherence, philosophy, visual quality. |
| **Eng** (Engineering) | Owns implementation fidelity, performance, reuse of frozen components. |
| **QA** | Owns accessibility, functional, and regression verification. |

All five roles are represented at **Gate 4 (Final Approval)**. Gates 1–3 involve
the relevant subset.

---

## 2. The Four Review Gates

1. **Gate 1 — UX Review:** structure, flow, comprehension, a11y foundations.
2. **Gate 2 — Visual Design Review:** philosophy, tokens, brand, premium feel.
3. **Gate 3 — Implementation Review:** fidelity, performance, component reuse.
4. **Gate 4 — Final Approval:** holistic readiness and business alignment.

See `review-process.md` for detailed checks and outcomes per gate.

---

## 3. Preview Route Requirement

Every page must be accessible via a **local preview route** before any gate
review. This is mandatory — reviews are conducted on the running interface, not
static files.

Standard routes:

- `/` — Home
- `/products` — Product Discovery / Shopping
- `/cart` — Cart
- `/checkout` — Checkout
- `/orders` — Order Tracking
- `/training` — Training Registration / Dashboard
- `/dashboard` — User Dashboard / Profile
- `/admin` — Admin Operations
- `/governance` — Governance
- `/analytics` — Analytics
- `/assistant` — AI Assistant

Each page's **Preview Route** is recorded in its Page Design Brief.

---

## 4. Approval Statuses

A page moves through these statuses, recorded in the brief:

| Status | Meaning |
| --- | --- |
| **Draft** | Brief created; not yet reviewed. |
| **In Review** | Active at one or more gates. |
| **Changes Requested** | Returned with required revisions; cannot advance. |
| **Approved** | Passed all four gates. |
| **Frozen** | Approved and locked into the design system / shipped baseline. |

Transitions are linear with possible returns to **Changes Requested**. A page
cannot be **Frozen** without first being **Approved**.

---

## 5. Sign-off Record

Each brief carries a Sign-off Record capturing every gate decision:

| Role | Name | Gate | Date | Decision |
| --- | --- | --- | --- | --- |
| CPO |  | Gate 4 |  | Approve / Changes |
| CXO |  | Gate 1/2/4 |  | Approve / Changes |
| Design |  | Gate 1/2/3 |  | Approve / Changes |
| Eng |  | Gate 3 |  | Approve / Changes |
| QA |  | Gate 3/4 |  | Approve / Changes |

The Sign-off Record is the canonical proof that a page was approved. Without it,
the page is not considered frozen and must not ship.

---

## 6. Enforcement

- No page proceeds past a gate without the required approvers' sign-off.
- Exceptions (skipped gate, status jump) require explicit CPO + CXO record.
- CI can block merges for pages lacking an Approved/Frozen status and a completed
  Sign-off Record.
