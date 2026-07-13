# Loading Experience — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1C): UX Standards & Accessibility Foundation

> **Status:** Mandatory patterns. No loading spinner without a skeleton. Reuses Principle 7 (Feedback) and Principle 11 (Performance Perception).

---

## 1. Loading Hierarchy

| Level | Trigger | Pattern | Max Duration Before Escalation |
|-------|---------|---------|--------------------------------|
| **Route** | Navigation (link, palette, browser back) | Full-page skeleton (shell + content blocks) | 300ms → show skeleton |
| **Section** | Tab switch, accordion open, filter change | Section skeleton (cards, table rows, chart box) | 200ms |
| **Component** | Async data in mounted view (chart, list, detail) | Component skeleton (match final dimensions) | 150ms |
| **Action** | Button click (submit, save, delete) | Inline button spinner + disabled | 100ms |
| **Background** | Sync, prefetch, analytics | Header sync indicator (subtle) | Non-blocking |

**Rule:** Never show a blank white screen. Never show a spinner alone on white.

---

## 2. Skeleton Specifications

### 2.1 Route Skeleton (Page Shell)
```tsx
<AppShell>
  <Header />           {/* Real, sticky */}
  <Sidebar />         {/* Real, interactive */}
  <main>
    <Breadcrumb />    {/* Real */}
    <ContentSkeleton>
      <HeaderSkeleton />      {/* Title + action slot */}
      <PanelSkeleton />       {/* Matches content type */}
    </ContentSkeleton>
  </main>
</AppShell>
```

### 2.2 Content Skeletons by Type

| Content Type | Skeleton Structure | Dimensions |
|--------------|-------------------|------------|
| **Dashboard / Overview** | 3–4 metric cards (skeleton) + chart box + table skeleton (5 rows) | Cards: 160×100; Chart: 400×250; Table: 5×row-height |
| **List / Table** | Header row (real) + 8 skeleton rows | Row height matches data row (48px comfortable / 36px compact) |
| **Card Grid** | 6–12 skeleton cards (responsive columns) | Card aspect ratio 4:3 or 16:9 |
| **Detail View** | Header skeleton + 3–5 field skeletons (label + value) | Field height 20px; label 12px |
| **Form** | Field skeletons in groups (label + input box) | Input height 40px; gap 16px |
| **Chart** | Box with chart-area skeleton + legend skeleton | Matches chart container |
| **Empty State** | No skeleton — shows empty state immediately | — |

### 2.3 Skeleton Visuals
- **Base color:** `var(--color-skeleton-base)` (light gray, e.g., `#e8ece8`)
- **Shimmer:** `linear-gradient(90deg, transparent, var(--color-skeleton-highlight), transparent)` animated 1.5s infinite
- **Reduced motion:** Static `var(--color-skeleton-base)` only
- **Border radius:** Matches final component (cards: 8px; inputs: 6px; avatars: 50%)
- **Text lines:** 1–3 lines of varying width (simulate real content)

---

## 3. Route Transition Loading

### 3.1 Client-Side Navigation
1. User clicks link / presses Enter in palette
2. **Immediate:** Start route transition, show route skeleton (if > 50ms)
3. **Parallel:** Fetch route code chunk + data (React Query / SWR)
4. **Stream:** SSR shell streams; Suspense boundaries resolve
5. **Hydrate:** Interactive; skeleton → content cross-fade (150ms)
6. **Focus:** Move to `#main` (or first heading) for a11y

### 3.2 Server-Side Navigation (Full Reload)
- Critical CSS inlined
- Font preload (`<link rel="preload" as="font" crossorigin>`)
- LCP image `fetchpriority="high"`
- No skeleton needed — HTML arrives with content

---

## 4. Action Loading (Mutations)

| Action Type | Pattern | Success | Error |
|-------------|---------|---------|-------|
| **Create** (New order, product) | Button spinner → optimistic list insert → toast + undo | Toast "Created" + undo | Inline error on button + toast; rollback optimistic |
| **Update** (Edit profile) | Field/section spinner → optimistic update → toast | Toast "Saved" | Inline error on field + toast; revert |
| **Delete** (Archive) | Confirm dialog → button spinner → optimistic remove → toast + undo | Toast "Deleted" + undo (5s) | Toast "Failed" + restore row |
| **Bulk** (Select → Delete) | Progress bar in toast → individual toasts | Summary toast | Per-item error badges |

**Button Spinner:** 16px, centered, replaces label. Button `disabled`, `aria-busy="true"`.

---

## 5. Data Fetching Loading (Queries)

| Query Type | Pattern | Stale-While-Revalidate |
|------------|---------|------------------------|
| **Route-critical** (list, detail) | Suspense + skeleton | 60s |
| **Secondary** (charts, related) | Component skeleton | 120s |
| **Background** (notifications, sync) | Header indicator only | 30s |
| **Search** | Debounced (300ms) + skeleton results | 30s |
| **Infinite scroll** | Skeleton row at bottom | 60s |

**React Query / SWR Config:**
```ts
{
  staleTime: 60_000,
  gcTime: 300_000,
  refetchOnWindowFocus: false, // manual refresh button
  retry: 1,
  placeholderData: keepPreviousData // for pagination
}
```

---

## 6. Image Loading

| Image Role | Pattern |
|------------|---------|
| **LCP Hero / Product** | `<img fetchpriority="high" loading="eager" srcset="..." sizes="...">` + blurhash placeholder (20px) |
| **Gallery / Thumbs** | `loading="lazy"` + blurhash + aspect-ratio box |
| **Avatars** | `loading="lazy"` + colored initials fallback (CSS) |
| **Icons** | Inline SVG (no network) |

**Blurhash:** Generated at build; 20px base64 PNG as `background-image` on wrapper.

---

## 7. Font Loading

- **WOFF2 only**, subset (Latin + Devanagari for Hindi/Marathi)
- **Preload:** Primary font (Regular 400, Medium 500)
- **font-display:** `swap`
- **Fallback metrics:** `size-adjust`, `ascent-override`, `descent-override` matched to primary
- **Critical CSS:** Inlines font-face with local fallback

---

## 8. Error & Retry Loading

| Scenario | UI |
|----------|----|
| **Route data fail** | Full-page error state: illustration + message + "Retry" button (focus on retry) |
| **Section fail** | Inline error in panel + "Retry" button |
| **Image fail** | Alt text visible; fallback SVG icon |
| **Font fail** | System font stack renders immediately |
| **Offline** | Banner "Working offline — changes will sync" + queue indicator |

**Retry Button:** Primary style, `aria-label="Retry loading [resource]"`, exponential backoff (1s, 2s, 4s) max 3 attempts.

---

## 9. Accessibility Loading

- **Skeletons:** `aria-hidden="true"` (decorative); real content announces when loaded
- **Live regions:** `aria-live="polite"` for "Loading complete", "Results updated"
- **Focus:** Never trapped in skeleton; skip link works
- **Reduced motion:** Skeletons static; no shimmer animation

---

## 10. Prototype Loading Gallery

Route: `/demo/loading` — All skeleton variants, action loaders, error states, offline banner, retry flows. Test with network throttling (3G, Offline).