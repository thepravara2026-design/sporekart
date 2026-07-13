# Public Website — Inner Pages

Sprint 21 Part 4 replaces the public route placeholders with real, content-rich
inner pages. All pages reuse `PublicLayout`, `Seo`, `PublicContentContainer`, the
`PageShell` helpers (`PageHeader`, `SectionHeading`, `Prose`, `CtaBanner`), and the
Part 3 motion helpers (`Reveal`, `MediaPlaceholder`). No backend/API changes.

## Pages
| Route | Component | Purpose |
|-------|-----------|---------|
| `/about` | `AboutPage` | Mission, values, team (placeholders) |
| `/products` | `ProductsPage` | Category grid linking to catalog/training |
| `/training` | `TrainingPage` | Programs, benefits, media placeholder |
| `/blog` | `BlogPage` | Article cards (placeholders) |
| `/contact` | `ContactPage` | UI-only form + contact details |
| `/faq` | `FaqPage` | Categorized accessible accordion (reuses `FAQ_ITEMS`) |
| `/certifications` | `CertificationsPage` | Commitments + certificate placeholder |
| `/privacy-policy` | `LegalPage` | Static policy (placeholder copy) |
| `/terms-and-conditions` | `LegalPage` | Static policy (placeholder copy) |
| `/refund-policy` | `LegalPage` | Static policy (placeholder copy) |
| `/shipping-policy` | `LegalPage` | Static policy (placeholder copy) |
| `/auth` | (kept placeholder) | Auth = backend scope, out of scope |

## Shared Building Blocks (`pages/PageShell.tsx`)
- `PageHeader`: eyebrow + H1 + intro.
- `SectionHeading`: eyebrow + H2 + description, left/center align.
- `Prose`: readable content column.
- `CtaBanner`: conversion banner with primary link.

## Conventions
- Each page sets `breadcrumbs` (Home → Page) and `Seo` (title, description, canonical).
- Data-bound lists (products, posts, courses, team, certificates) are clearly
  placeholder content pending real data.
- Forms are UI-only (no network call); confirmation uses `role="status"`.
- Icons restricted to the verified Design System registry names.
- Motion via `Reveal`; reduced-motion safe.

## Preview
- `/preview/inner-pages` lists every inner page for review.
- Each route is also directly reviewable at its URL.

## Open Items
- Real catalog/blog/course data (backend/CMS).
- Real team bios & photography.
- Real legal copy (legal review).
- Auth flow (`/auth`).
