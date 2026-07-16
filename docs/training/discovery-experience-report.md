# Public Discovery Experience Report — Sprint 26 Part 8

## Overview
The discovery experience is the entry point for prospective learners. It is composed of curated
sections plus a fully filterable explorer, all driven by one shared mock catalog.

## Discovery Surfaces
| Surface | Source | Behaviour |
|---------|--------|-----------|
| Featured Courses | `dc.featured` | Carousel of pinned/featured |
| Trending Courses | `dc.trending` | Carousel |
| Recommended | `dc.recommended` | Carousel (future-AI hook) |
| Recently Added | `createdAt` desc | Carousel |
| Upcoming Batches | `lifecycle === scheduled` | Carousel |
| Category Highlights | category counts | Grid of category links |
| Browse All | filtered explorer | Grid/List/Compact/Featured/Carousel + pagination |

## Reuse
- Search box reuses the enterprise search input contract (`role="searchbox"`).
- Filter panel reuses the enterprise filter dimension model.
- Pagination reuses the registry pager contract.
- Cards reuse `Card`/`Badge`/`Icon`.

## Placeholders (future-ready)
- Bookmarks (Set state, no persistence)
- Wishlist (Set state, no persistence)
- Continue Learning (reserved slot, not rendered)
- AI Recommendations (recommended derivation point)
