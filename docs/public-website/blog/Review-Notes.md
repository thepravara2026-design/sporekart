# Blog Review Notes

Status: **Pending review / approval** (Sprint 21 Part 5 → freeze before Part 6).

## Preview routes
- `/preview/blog` — Landing
- `/preview/blog/article` — Article (renders a featured article in a frame)
- `/preview/blog/search` — Search (`?q=oyster`)

Each preview includes a desktop/laptop/tablet/mobile viewport switcher, plus:
- Accessibility notes (landmarks, keyboard, ARIA, motion, contrast).
- Responsive notes (grid reflow, breakpoints, no-overflow, sticky behaviour).
- Section checklist per view.
- "Approval status: Pending review" badge.

## What to verify visually
- [ ] Hero gradient + search alignment on desktop and mobile (no overflow at 375px).
- [ ] Featured article two-column → stacked on tablet/mobile.
- [ ] Category grid reflows (auto-fill minmax 240px).
- [ ] Trending + tags two-column collapses under 920px.
- [ ] Article: sticky TOC highlights active section while scrolling; progress bar
      tracks scroll; prev/next navigable.
- [ ] Callouts (info/success/warning/tip), quote, and image/video placeholders render.
- [ ] Related articles, product/training promos, newsletter present.
- [ ] Search: results grid, empty state, popular searches, tag cloud.
- [ ] Hover/focus states on cards, categories, tags, TOC, share buttons.

## Open questions for reviewer
1. Preferred default hero image vs. gradient (currently gradient + MediaPlaceholder).
2. Article counts: keep illustrative placeholders or hide until CMS?
3. Author avatar: initials monogram acceptable, or real photos later?
4. Reading progress bar color (currently accent green) — confirm.

## Sign-off
- [ ] UX
- [ ] UI
- [ ] SEO
- [ ] Accessibility
- [ ] Performance
- [ ] Content
