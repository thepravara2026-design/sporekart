# Review Process — SporeKart Enterprise AI Platform
## Phase 5 / Sprint 19 (Part 1A): Enterprise Product Experience Foundation

> **Status:** Documentation Only. This defines the iterative design & development
> workflow and the review gates every page must pass. **No page proceeds without
> approval at each gate.**

---

## 1. The 15-Step Iterative Workflow

1. **Business Goal** — Define the outcome the page must drive (from the Page
   Design Brief). No work starts without a clear business goal.
2. **User Research** — Validate needs against personas (`user-personas.md`) and
   journeys (`user-journeys.md`). Identify real tasks and friction.
3. **User Journey** — Map the end-to-end path for this page (entry → action →
   success), including friction points.
4. **Information Architecture (IA)** — Define structure, naming, and navigation
   placement consistent with the rest of the product.
5. **Wireframe** — Low-fidelity layout focusing on structure and hierarchy, not
   visuals. Reuses the frozen component set conceptually.
6. **High Fidelity** — Apply the design system, tokens, and brand. Pixel-level
   craft within the frozen components.
7. **Design Review** — Internal design critique against philosophy and
   principles. *(Feeds Gate 1 & Gate 2.)*
8. **React Implementation** — Build in React + Vite + TypeScript using frozen
   design-system components. No new components without freeze policy.
9. **Local Preview** — Deploy to a local route (`Preview Route` in the brief),
   accessible for review on desktop and mobile.
10. **User Review** — Test with representative users / stakeholder walkthrough.
    Capture comprehension and task success.
11. **Feedback** — Consolidate findings; log as actionable items with owners.
12. **Redesign** — Iterate on wireframe/high-fidelity/implementation as needed.
13. **Updated Preview** — Re-publish the local preview reflecting changes.
14. **Freeze** — Once approved at all gates, the page and any new components are
    frozen into the design system (`component-freeze-policy.md`).
15. **Documentation** — Record the brief, decisions, sign-offs, and KPIs. Close
    the loop for governance and future reference.

The workflow is iterative: feedback (step 11) may return to any earlier step.

---

## 2. Review Gates

No page moves forward without explicit sign-off at each gate.

### Gate 1 — UX Review
- **Focus:** Structure, flow, comprehension, accessibility foundations.
- **Checks:** Journey correctness, IA consistency, task clarity, a11y basics.
- **Approvers:** Design, CPO/CXO.
- **Outcome:** Pass → proceed to visual; Fail → return to Wireframe/IA.

### Gate 2 — Visual Design Review
- **Focus:** Adherence to design philosophy, tokens, brand personality.
- **Checks:** Typography, spacing, color, hierarchy, motion restraint, premium
  feel.
- **Approvers:** Design, CPO/CXO.
- **Outcome:** Pass → proceed to implementation; Fail → return to High Fidelity.

### Gate 3 — Implementation Review
- **Focus:** Fidelity to design, performance, code quality, component reuse.
- **Checks:** Uses frozen components only, performance budgets met, responsive,
  keyboard/screen-reader support, no regressions.
- **Approvers:** Eng, QA, Design.
- **Outcome:** Pass → proceed to final; Fail → return to React Implementation.

### Gate 4 — Final Approval
- **Focus:** Holistic readiness against business goal and vision.
- **Checks:** All prior gates closed, KPIs defined, documentation complete,
  preview verified.
- **Approvers:** CPO, CXO, Design, Eng, QA.
- **Outcome:** Approved → Freeze (step 14). Fail → return to relevant step.

---

## 3. Enforcement

- A page in **Changes Requested** or **Draft** cannot enter the next gate.
- Gate decisions are recorded in the brief's Sign-off Record.
- Skipping a gate requires explicit, recorded exception approval by CPO + CXO.
- Frozen pages become the baseline for all future work.
