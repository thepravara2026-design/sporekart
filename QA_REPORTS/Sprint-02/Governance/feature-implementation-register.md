# SporeKart QA Sprint 2 — Feature Implementation Register

**Date:** 2026-07-17  
**Scope:** Planned functionality that is incomplete or not yet implemented.  
**Total Implementation Gaps:** 8  

---

## Implementation Gap Register

### GAP-001 — Product Catalog / Listing Page

| Field | Value |
|-------|-------|
| **Priority** | P0 — Critical |
| **Module** | Customer Journey — Products |
| **Business Impact** | Business Critical — Core e-commerce feature. Users cannot browse products. |
| **Current State** | `/products` shows 6 category cards only. No product grid, listing, or pagination. |
| **Required for v1.0** | Yes — users must be able to browse products |
| **Routes Affected** | `/products` |
| **Components Needed** | ProductGrid, ProductCard integration (exists in design system), Pagination, CategoryFilter |
| **Evidence** | customer-journey-catalog test results: category cards render, no listing |
| **Dependencies** | Product API, product data model |
| **Suggested Approach** | Build MVP: product grid + pagination only. Defer filters/sorting. |

---

### GAP-002 — Product Detail Page

| Field | Value |
|-------|-------|
| **Priority** | P0 — Critical |
| **Module** | Customer Journey — Product Details |
| **Business Impact** | Business Critical — Users cannot view product details, images, or prices. |
| **Current State** | No product detail route exists. All `/product/*` and `/products/*` patterns return 404. |
| **Required for v1.0** | Yes |
| **Routes Needed** | `/products/:slug` or `/product/:id` |
| **Components Needed** | ImageGallery, ProductInfo, QuantitySelector, AddToCart (exists in design system), Breadcrumb, RelatedProducts |
| **Evidence** | customer-journey-product-details test results: all routes 404 |
| **Dependencies** | Product catalog, product API |

---

### GAP-003 — Shopping Cart

| Field | Value |
|-------|-------|
| **Priority** | P0 — Critical |
| **Module** | Customer Journey — Cart |
| **Business Impact** | Revenue Impact — Users cannot add items to cart, view cart, or proceed to checkout. |
| **Current State** | No `/cart` route, no cart page, no cart state management. `AddToCart` button exists in `ProductCard.tsx` (design system) but has no store wired. Cart icon exists in icon registry. |
| **Required for v1.0** | Yes — cart is a prerequisite for checkout |
| **Routes Needed** | `/cart` |
| **Components Needed** | CartContext (React context + localStorage), CartPage, CartItem, CartSummary, CartBadge (header), CheckoutButton |
| **Evidence** | customer-journey-cart test results: `/cart` returns 404 |
| **Dependencies** | Product detail page, product API |
| **Suggested Approach** | Start with simple localStorage-based cart. Add guest→logged-in merge later. |

---

### GAP-004 — Product Search

| Field | Value |
|-------|-------|
| **Priority** | P1 — High |
| **Module** | Customer Journey — Search |
| **Business Impact** | Customer Blocking — Users cannot search for products |
| **Current State** | `/search` implements blog-only full-text search (14 mock articles). No product search exists. |
| **Required for v1.0** | Yes — essential for product discovery |
| **Routes Affected** | `/search`, `/search?q=` |
| **Components Needed** | SearchBar (header), SearchResults with product cards, SearchFilters |
| **Evidence** | customer-journey-catalog search tests: search only covers blog content |
| **Dependencies** | Product catalog, search index (Elasticsearch / Algolia) |

---

### GAP-005 — Product Filters

| Field | Value |
|-------|-------|
| **Priority** | P1 — High |
| **Module** | Customer Journey — Filters |
| **Business Impact** | Customer Blocking — Users cannot refine product discovery |
| **Current State** | No filter controls on public product pages. FilterBar component exists in admin DataGridPreviews but not wired for public use. |
| **Required for v1.0** | Yes (MVP: category filter only) |
| **Filters Needed** | Category, Price range, Availability, Rating |
| **Evidence** | customer-journey-catalog filter tests: no filter controls found |
| **Dependencies** | Product catalog |
| **Suggested Approach** | MVP: category filter only. Defer price/availability/rating. |

---

### GAP-006 — Product Sorting

| Field | Value |
|-------|-------|
| **Priority** | P1 — High |
| **Module** | Customer Journey — Sorting |
| **Business Impact** | Customer Blocking — Users cannot sort product listings |
| **Current State** | No sort controls on public product pages. Sort components exist in CourseLibrary and WishlistPage (customer workspace) but not for public catalog. |
| **Required for v1.0** | Yes (MVP: price + newest) |
| **Sort Options Needed** | Price Low→High, Price High→Low, Newest, Popularity, A–Z |
| **Evidence** | customer-journey-catalog sort tests: no sort controls found |
| **Dependencies** | Product catalog |
| **Suggested Approach** | MVP: price (low/high) + newest. Defer popularity/alphabetical. |

---

### GAP-007 — Role Switcher UI for QA Mode

| Field | Value |
|-------|-------|
| **Priority** | P1 — High |
| **Module** | RBAC / Authorization |
| **Business Impact** | Security Impact — Cannot verify role-based access control in QA |
| **Current State** | `activeRole` exists in AppContext but no UI control to switch roles. RBAC tests pass manually by reading default role. |
| **Required for v1.0** | No (QA tooling). Required for QA verification. |
| **Components Needed** | RoleSwitcher (select dropdown), enabled only in QA/mock mode |
| **Evidence** | security-validation test: `select[aria-label="Switch review role"]` not found |
| **Suggested Approach** | Add a simple `<select>` in the sidebar or header when `MOCK_MODE=true` |

---

### GAP-008 — Loading Skeletons for Products Page

| Field | Value |
|-------|-------|
| **Priority** | P2 — Medium |
| **Module** | Customer Journey — Product Catalog |
| **Business Impact** | Usability Impact — No loading state during page render |
| **Current State** | Products page renders category cards directly without loading indicator. No skeleton/placeholder components visible. |
| **Required for v1.0** | Nice-to-have. Product catalog loading states. |
| **Components Needed** | SkeletonCard, SkeletonGrid (design system may already have Skeleton component) |
| **Evidence** | customer-journey-visual test: no skeleton elements found |
| **Dependencies** | Product catalog |

---

## Summary

| Priority | Count | Gap IDs |
|----------|-------|---------|
| P0 — Critical | 3 | GAP-001 (Catalog), GAP-002 (Details), GAP-003 (Cart) |
| P1 — High | 4 | GAP-004 (Search), GAP-005 (Filters), GAP-006 (Sorting), GAP-007 (Role Switcher) |
| P2 — Medium | 1 | GAP-008 (Loading Skeletons) |
| **Total Implementation Gaps** | **8** | |

## v1.0 Release Impact

| Gap | Release Blocker? | Can Defer? |
|-----|-----------------|------------|
| Product Catalog | **Yes** — core e-commerce | No |
| Product Details | **Yes** — core e-commerce | No |
| Shopping Cart | **Yes** — prerequisite for checkout | No |
| Product Search | **Yes** — essential UX | With mitigation (category-only browsing) |
| Product Filters | Yes — essential UX | With mitigation (hierarchical categories) |
| Product Sorting | Yes — essential UX | With mitigation (default sort only) |
| Role Switcher | No — QA tooling only | Yes |
| Loading Skeletons | No — nice-to-have | Yes |
