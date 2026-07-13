# Experience Principles — SporeKart Enterprise AI Platform
## Phase 5 / Sprint 19 (Part 1A): Enterprise Product Experience Foundation

> **Status:** Documentation Only. These 10 principles are mandatory. Every future
> design and implementation decision must be justified against them. When
> principles conflict, resolve in the order listed — higher principles win.

---

## 1. Simplicity over Complexity
**Description:** The simplest path that fully solves the user's task is always
preferred. We resist feature stacking, nested menus, and clever interactions
that cost comprehension.

**How it influences design:**
- One primary action per screen; secondary actions are visibly subordinate.
- Default to familiar patterns over novel ones.
- Remove every element that does not serve the task. If asked "do we need
  this?", the answer is usually no.

---

## 2. Consistency over Creativity
**Description:** A predictable product is a trusted product. We reuse patterns,
components, and language rather than inventing new ones for each page.

**How it influences design:**
- Every page uses the frozen design system components (`component-freeze-policy.md`).
- Navigation, labels, and terminology are identical across the product.
- Creativity is expressed within the system, never by breaking it.

---

## 3. Clarity over Decoration
**Description:** If something does not make the interface clearer, it should not
be there. Visual treatment serves understanding, not appearance.

**How it influences design:**
- Labels are plain and literal; icons are paired with text where ambiguity exists.
- Data is shown with honest units, context, and source.
- No decorative imagery that competes with content or misleads.

---

## 4. Performance over Visual Excess
**Description:** Speed is a feature and a form of respect. A beautiful screen
that loads slowly destroys trust. We optimize perceived and real performance.

**How it influences design:**
- Lightweight components; no heavy animations or assets that block interaction.
- Stable layouts (no content shift) so the page feels instant.
- Performance budgets are part of the page brief (`templates/page-design-brief.md`).

---

## 5. Accessibility by Default
**Description:** The product is usable by everyone — including low-vision,
low-literacy, motor-impaired, and assistive-technology users — without special
modes or exceptions.

**How it influences design:**
- Semantic HTML, full keyboard support, and screen-reader labels from the start.
- Contrast, focus states, and touch targets meet or exceed WCAG 2.1 AA.
- Accessibility is a gate, not a retrofit (`style-governance.md`).

---

## 6. Mobile-Responsive Web Experience
**Description:** The primary surface for many Indian users is a mid-range
Android phone over mobile data. The web experience must be first-class on
mobile, not a shrunken desktop.

**How it influences design:**
- Mobile-first layouts; desktop extends the same mental model.
- Touch targets, font sizes, and bandwidth use are tuned for phones.
- Future native apps reuse the same APIs and experience logic.

---

## 7. Enterprise-Grade Usability
**Description:** Administrators, distributors, and businesses need dense,
efficient, and reliable tools. The product must scale from a single farmer's
simple task to an enterprise operator's complex workflow.

**How it influences design:**
- Tables, dashboards, and bulk actions are designed for power users.
- Role-appropriate information density; novices are guided, experts are accelerated.
- Errors are recoverable; state is never lost without explicit confirmation.

---

## 8. Scientific and Agricultural Authenticity
**Description:** SporeKart is built on real agricultural science. The interface
must reflect that rigor — no pseudo-science, no inflated claims, no misleading
visuals.

**How it influences design:**
- Data, recommendations, and metrics are labeled with method and source.
- Visual language references labs and fields honestly (calm greens, neutral
  surfaces, precise typography).
- Copy avoids hype; it informs.

---

## 9. User Confidence through Transparency
**Description:** Users should never wonder "what just happened?" or "why did
this cost that?". Transparency builds the trust that defines the brand.

**How it influences design:**
- Prices, fees, and policies are shown before commitment.
- Actions have clear, immediate confirmation and an undo path where possible.
- System status, errors, and wait times are communicated honestly.

---

## 10. Delight through Thoughtful Interactions
**Description:** Premium does not mean cold. Small, well-considered moments —
a clear success state, a helpful empty state, a respectful micro-interaction —
create delight without noise.

**How it influences design:**
- Empty, loading, error, and success states are crafted, not default.
- Micro-interactions are subtle, fast, and meaningful (Principle 7 motion restraint).
- Delight never costs clarity, performance, or accessibility.

---

## Conflict Resolution

When two principles pull in different directions, the higher-numbered-listed
principle does **not** automatically win; instead, surface the conflict in the
design review. As a default hierarchy:

**Accessibility and Transparency (5, 9) > Clarity and Simplicity (1, 3) >
Consistency and Performance (2, 4) > Mobile, Enterprise, Authenticity, Delight
(6, 7, 8, 10).**

No principle may be silently violated. A violation requires explicit, recorded
approval at a review gate.
