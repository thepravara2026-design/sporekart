# Page Design Brief — Reusable Template
## SporeKart Enterprise AI Platform · Phase 5 / Sprint 19 (Part 1A)

> **How to use:** Copy this template for every new page or major screen. Fill
> every field. Attach to the review process (`review-process.md`). A page does
> not enter development without a completed, approved brief.

---

```markdown
# Page Design Brief: <Page Name>

## 1. Page Name
<Human-readable name of the page/screen>

## 2. Purpose
<One or two sentences: what this page exists to do for the user and the business.>

## 3. Business Goal
<The measurable business outcome this page supports (e.g., increase completed
checkouts by X%, reduce support tickets for order status).>

## 4. Target Audience
<Primary persona(s) from user-personas.md. Note role, device, context of use.>

## 5. Primary Actions
<The one or two most important things the user must be able to do here.
List in order of importance.>

## 6. Secondary Actions
<Supporting actions that must be available but should not compete with primaries.>

## 7. Emotional Goal
<How the user should feel after using this page (e.g., confident, informed, calm).
Map to product-vision.md experience attributes.>

## 8. Accessibility Goals
<Specific a11y targets: WCAG 2.1 AA, keyboard support, screen-reader labels,
touch target size, contrast, reduced-motion. Reference experience-principles #5.>

## 9. SEO Goals
<For public pages: target keywords, metadata, structured data, crawlability.
Note if page is authenticated/private (N/A).>

## 10. Performance Goals
<Concrete budgets: Largest Contentful Paint, Time to Interactive, bundle size,
network payload, especially for mobile (Principle #4, #6).>

## 11. KPIs
<The metrics tracked for this page: conversion, task success rate, time on task,
error rate, etc.>

## 12. Success Metrics
<Target values / thresholds that define the page as successful post-launch.>

## 13. Dependencies
<APIs (/api/v1/**), design-system components, data, other teams, content.>

## 14. Preview Route
<Local route where the page is accessible for review, e.g. /, /products, /cart,
/checkout, /dashboard, /admin, /governance, /analytics, /training.>

## 15. Approval Status
<One of: Draft · In Review · Changes Requested · Approved · Frozen.
See approval-workflow.md.>

## Sign-off Record
| Role | Name | Gate | Date | Decision |
| --- | --- | --- | --- | --- |
| CPO |  |  |  |  |
| CXO |  |  |  |  |
| Design |  |  |  |  |
| Eng |  |  |  |  |
| QA |  |  |  |  |
```

---

## Notes
- Keep the brief to one page where possible; link detailed research separately.
- "Preview Route" is mandatory — no page is reviewed without a live local route.
- Update "Approval Status" as the page moves through gates; never skip a gate.
