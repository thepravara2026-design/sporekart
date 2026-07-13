# User Journeys — SporeKart Enterprise AI Platform
## Phase 5 / Sprint 19 (Part 1A): Enterprise Product Experience Foundation

> **Status:** Documentation Only. Each journey uses a consistent format:
> **Entry Points · Primary Actions · Success Criteria · Potential Friction Points.**
> These journeys are the basis for page design briefs and wireframes.

---

## Home
- **Entry Points:** Direct URL `/`, search engine, shared link, app launch.
- **Primary Actions:** Understand the platform, navigate to a task, sign in/up.
- **Success Criteria:** User immediately understands what SporeKart offers and
  finds a clear next step within seconds.
- **Potential Friction Points:** Unclear value proposition, slow load, cluttered
  hero, no clear path for first-time vs returning users.

---

## Shopping
- **Entry Points:** Home, category nav, search, promotional link.
- **Primary Actions:** Browse categories, filter, view product lists, add to cart.
- **Success Criteria:** User finds relevant products with confidence and adds
  intended items without confusion.
- **Potential Friction Points:** Too many choices, weak filters, unclear
  availability, hidden costs.

---

## Product Discovery
- **Entry Points:** Search, recommendations, category, AI assistant suggestion.
- **Primary Actions:** Read description, view specs/authenticity, compare, decide.
- **Success Criteria:** User understands the product's fit for their need and
  trusts its authenticity.
- **Potential Friction Points:** Jargon without explanation, missing source/
  certification, no vernacular support.

---

## Checkout
- **Entry Points:** Cart, express buy, saved cart.
- **Primary Actions:** Review items, select address/payment, confirm order.
- **Success Criteria:** Order placed with full price transparency and immediate
  confirmation; no abandoned cart from confusion.
- **Potential Friction Points:** Surprise fees, forced account creation, weak
  error handling, no clear total before commit.

---

## Order Tracking
- **Entry Points:** Confirmation, account, email/SMS link, home.
- **Primary Actions:** View status, ETA, support, reorder.
- **Success Criteria:** User knows exactly where the order is and when to expect it.
- **Potential Friction Points:** Stale status, unclear stages, no recourse on delay.

---

## Training Registration
- **Entry Points:** Training listing, home, trainer share, notification.
- **Primary Actions:** View session, register, receive confirmation.
- **Success Criteria:** User registers in minimal steps and gets clear next steps.
- **Potential Friction Points:** Complex forms, no capacity clarity, no reminder.

---

## Training Dashboard
- **Entry Points:** Account, trainer link, confirmation email.
- **Primary Actions:** View enrolled/created trainings, manage, track attendance.
- **Success Criteria:** Trainer and attendee see relevant, accurate information.
- **Potential Friction Points:** Wrong role view, missing export, stale data.

---

## Profile Management
- **Entry Points:** Account menu, settings, post-auth redirect.
- **Primary Actions:** Edit details, addresses, preferences, security.
- **Success Criteria:** User updates info confidently; changes persist and are
  clearly confirmed.
- **Potential Friction Points:** Lost changes on error, unclear save state,
  no verification.

---

## AI Assistant
- **Entry Points:** Persistent launcher, contextual help, product page.
- **Primary Actions:** Ask question, receive guidance, take suggested action.
- **Success Criteria:** User gets a helpful, honest, sourced answer and a clear
  next step.
- **Potential Friction Points:** Overconfident/wrong answers, no source, no
  escape to human support, hallucinated products.

---

## Admin Operations
- **Entry Points:** `/admin`, role-based login, notification.
- **Primary Actions:** Manage users, content, orders, config, monitor.
- **Success Criteria:** Admin completes tasks efficiently with full context and
  auditability.
- **Potential Friction Points:** Disconnected screens, slow lists, no bulk
  actions, weak audit log.

---

## Governance
- **Entry Points:** `/governance`, admin role.
- **Primary Actions:** Review policies, approvals, compliance, access control.
- **Success Criteria:** Governance actions are transparent, recorded, and
  reversible with oversight.
- **Potential Friction Points:** Unclear ownership, missing trail, policy drift.

---

## Analytics
- **Entry Points:** `/analytics`, dashboard, role-based login.
- **Primary Actions:** View metrics, filter, export, drill down.
- **Success Criteria:** User derives a trusted insight and can act on it.
- **Potential Friction Points:** Vanity metrics, no context/source, slow
  charts, inaccessible visualizations.

---

## Cross-Journey Notes
- Every journey must respect the 10 experience principles, especially
  Accessibility, Mobile, and Transparency.
- All journeys reuse frozen design-system components.
- Friction points identified here become explicit test cases in QA.
