# Phase 6 — Sprint 21 Part 6: Contact, Support, FAQ & Legal

**Status:** PENDING REVIEW (2026-07-13)
**Branch:** `sporetest`
**Date:** 2026-07-13
**Depends on:** Sprint 21 Part 5 (Blog & Knowledge Hub) — APPROVED, Sprint 21 Part 4 (Inner Pages) — COMPLETE

## Mission
Implement the public Contact, Support, FAQ and Legal experience pages — the
trust, help and compliance layer of the SporeKart public site. No backend/API
changes; visual + structural only, built on the Design System v1.0.0 green/neutral
token system.

## Routes implemented
| Route | Page | File |
| --- | --- | --- |
| `/contact` | Contact | `pages/ContactPage.tsx` |
| `/support` | Support | `pages/SupportPage.tsx` |
| `/faq` | FAQ | `pages/FaqPage.tsx` |
| `/privacy-policy` | Legal (privacy) | `pages/LegalPage.tsx` |
| `/terms-and-conditions` | Legal (terms) | `pages/LegalPage.tsx` |
| `/refund-policy` | Legal (refund) | `pages/LegalPage.tsx` |
| `/shipping-policy` | Legal (shipping) | `pages/LegalPage.tsx` |
| `/cookie-policy` | Legal (cookie) — **new** | `pages/LegalPage.tsx` |
| `/disclaimer` | Legal (disclaimer) — **new** | `pages/LegalPage.tsx` |
| `/preview/contact` | Preview: contact | `preview/ExperiencePreview.tsx` |
| `/preview/support` | Preview: support | `preview/ExperiencePreview.tsx` |
| `/preview/faq` | Preview: faq | `preview/ExperiencePreview.tsx` |
| `/preview/legal` | Preview: legal | `preview/ExperiencePreview.tsx` |

## Deliverables
- `src/public-website/experience/data/business-info.ts` — company/contact
  placeholders (`COMPANY`, `CONTACT_CATEGORIES`, `QUICK_LINKS`), clearly badged.
- `src/public-website/experience/data/faq-data.ts` — `FAQ_CATEGORIES`,
  `ALL_FAQ_ITEMS` (migrated from `pages/FaqPage.tsx`), `searchFaq`.
- `src/public-website/experience/ExperienceStyles.tsx` — scoped, token-only CSS
  for contact / support / faq / legal (cards, two-column, timeline, chips,
  accordion, sticky TOC, print rules, reduced-motion).
- `src/public-website/experience/components/ContactForm.tsx` — reusable form:
  client-side validation, `role="alert"` success/error, `aria-invalid`/`aria-describedby`
  on errors, accessible honeypot placeholder, `aria-busy` loading.
- `src/public-website/experience/components/LegalTemplate.tsx` — reusable legal
  template: hero + last-updated, sticky scroll-spy table of contents
  (`aria-current`), print-ready layout, placeholder badge.
- `src/public-website/pages/ContactPage.tsx` — hero, two-column form + info
  (address/phone/email with `aria-label`s, map placeholder), social links,
  emergency-contact note, quick-contact cards, training CTA, newsletter.
- `src/public-website/pages/SupportPage.tsx` — hero, help categories (links),
  response-time cards, support-journey timeline, escalation flow, contact CTA,
  newsletter.
- `src/public-website/pages/FaqPage.tsx` — search, category filter chips
  (`aria-pressed`), accessible accordion (`aria-expanded`/`aria-controls` + region),
  related links, contact CTA, newsletter, FAQ JSON-LD schema.
- `src/public-website/pages/LegalPage.tsx` — extended to 6 policies
  (privacy, terms, refund, shipping, cookie, disclaimer) selected by `pathname`;
  per-doc SEO + BreadcrumbList.
- `src/public-website/preview/ExperiencePreview.tsx` — 4 preview views with
  viewport switcher + a11y/responsive/section-checklist notes + approval status.
- `src/App.tsx` — lazy routes + preview routes wired (`/support`, `/cookie-policy`,
  `/disclaimer`, `/preview/contact|support|faq|legal`).

## SEO
- `Seo` drives `<title>`, meta description, canonical, Open Graph, Twitter Cards
  per page/doc.
- JSON-LD: `FAQPage` (FAQ), `BreadcrumbList` (Legal, Contact, Support). Home/OG
  `site_name` already set globally.
- Semantic H1/H2/H3, internal cross-links (contact ↔ support ↔ faq ↔ legal).

## Verification
- `npm run typecheck`: 0 errors.
- `npm run build`: success (per-page code-split chunks).
- Review: `http://localhost:5173/contact`, `/support`, `/faq`,
  `/cookie-policy`, `/disclaimer`, `/preview/contact|support|faq|legal`.

## Notes / Risks
- All business details (address, phone, email, hours, social handles) are
  **placeholders** — clearly badged where shown.
- The contact form is front-end only; submission wiring, spam/honeypot backend,
  and a "was this helpful?" FAQ vote are intentionally placeholder-ready.
- No backend/API changes; no fabricated statistics.
- **Pending approval:** confirm final contact details, real support SLAs, and
  legal copy before launch.
