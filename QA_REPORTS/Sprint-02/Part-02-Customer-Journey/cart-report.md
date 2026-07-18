# SporeKart QA Sprint 2 — Cart Report

**Date:** 2026-07-17

---

## Current State

**No shopping cart exists in the application.** Key findings:

1. **No `/cart` route** — returns 404
2. **No cart page component** — not implemented
3. **No cart state management** — no React context, Redux store, or similar
4. **No cart badge/icon** in the public header
5. **Add to Cart buttons** in `ProductCard.tsx` (design system) have `onAddToCart` prop but no store wired to it
6. **Toast messages** in RecommendationsPage, WishlistPage, and OrderDetailsPage reference cart but are mock-only
7. **`.env.mock`** has `MOCK_CART_SERVICE=http://localhost:8083` but no frontend integration

## What Exists (Partial)

| Feature | Status | Notes |
|---------|--------|-------|
| Cart icon in icon registry | ✅ | `shopping-cart` SVG exists |
| Add to Cart button on ProductCard | ✅ | Design system component, no store |
| Toast notification for "added to cart" | ⚠️ | Mock-only in a few pages |
| Cart badge in header | ❌ | No badge rendered |
| `/cart` route | ❌ | Returns 404 |

## What's Missing

| Feature | Status | Impact |
|---------|--------|--------|
| Cart page | ❌ | Cannot view cart |
| Add to cart functionality | ❌ | Cannot add items |
| Remove items | ❌ | Cannot remove items |
| Quantity increase/decrease | ❌ | Cannot adjust quantities |
| Quantity limits | ❌ | Not implemented |
| Cart badge count | ❌ | No visual indicator |
| Cart total calculation | ❌ | No total shown |
| Empty cart state | ❌ | Not implemented |
| Cart persistence (localStorage) | ❌ | Not implemented |
| Cart refresh behavior | ❌ | Not implemented |
| Guest cart | ❌ | Not implemented |
| Logged-in cart | ❌ | Not implemented |
| Guest → logged-in transition | ❌ | Not implemented |
| Multi-tab cart | ❌ | Not implemented |

## Bugs

| ID | Issue | Severity |
|----|-------|----------|
| BUG-CART-001 | No shopping cart exists | Critical |
| BUG-CART-002 | No Cart route (returns 404) | Critical |
| BUG-CART-003 | Add to Cart buttons on ProductCard have no store wired | High |
| BUG-CART-004 | No cart icon/badge in public header | Medium |

## Recommendation

Build the shopping cart as the highest-priority e-commerce feature. Implementation should include:
1. Cart React context with localStorage persistence
2. Cart page at `/cart` with item list, quantity controls, totals
3. Cart badge in header with item count
4. Integration with ProductCard's `onAddToCart` prop
5. Guest cart → logged-in cart merge support
