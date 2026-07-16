# Course Details Page Architecture — Sprint 26 Part 8

## Layout
Single premium page with a hero banner and a two-column body (`1fr / 320px`) that collapses to a
stacked flow on mobile. The right column is a **sticky enrollment panel**.

## Sections (in order)
1. Hero Banner — badge row, title, description, meta chips (level/duration/delivery/language/modules/rating)
2. Overview
3. Course Highlights (4 stat cards)
4. Learning Objectives
5. Who Should Join (audience badges)
6. Prerequisites
7. Curriculum Overview (numbered highlights)
8. Resource Highlights
9. Trainer Placeholder (avatar initials)
10. Pricing Summary (inside sticky panel)
11. Seat Availability (inside sticky panel)
12. FAQs (accordion-style cards)
13. Certification Preview
14. Related Courses
15. Sticky Enrollment Panel — price, seats, enroll CTA, save, next-batch, included list

## SEO Metadata
- `<title>` from `seoTitle`
- meta description, canonical, Open Graph, Twitter Card via `<Seo>`
- JSON-LD `Course` structured data (provider, courseCode, educationalLevel, inLanguage, teaches, offers)

## Reuse
`Seo`, `BreadcrumbFoundation`, `Card`, `Badge`, `Icon`, `MediaPlaceholder` (hero reserved),
`CourseCard` (related). No duplicated UI.
