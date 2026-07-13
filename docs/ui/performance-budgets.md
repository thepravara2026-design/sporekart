# Performance Budgets — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1C): UX Standards & Accessibility Foundation

> **Status:** Mandatory budgets. Every page must meet these targets in CI before Gate 3. Reuses Principle 4 (Performance over Visual Excess) from Part 1A.

---

## 1. Core Web Vitals Targets (Production)

| Metric | Good | Budget (Max) | Measurement |
|--------|------|--------------|-------------|
| **LCP** (Largest Contentful Paint) | ≤ 2.5s | **2.5s** | 75th percentile, mobile 4G |
| **INP** (Interaction to Next Paint) | ≤ 200ms | **200ms** | 75th percentile, all interactions |
| **CLS** (Cumulative Layout Shift) | ≤ 0.1 | **0.1** | 75th percentile |
| **TTFB** (Time to First Byte) | ≤ 800ms | **800ms** | Server + CDN |
| **FCP** (First Contentful Paint) | ≤ 1.8s | **1.8s** | 75th percentile |

---

## 2. Resource Budgets (Per Page Load)

| Resource | Budget | Notes |
|----------|--------|-------|
| **Total JS (gzipped)** | **170 KB** | Includes React, Router, vendor. Code-split by route. |
| **Total CSS (gzipped)** | **35 KB** | Critical CSS inlined; rest async. |
| **Fonts (gzipped)** | **50 KB** | WOFF2, subset, `font-display: swap`. Max 2 families. |
| **Images (initial viewport)** | **100 KB** | Hero/LCP image optimized; others lazy. |
| **Total initial transfer** | **350 KB** | Compressed (Brotli/Gzip). |
| **Total requests** | **< 40** | HTTP/2 multiplexing; bundle splitting. |
| **Third-party scripts** | **0** (blocked) | Analytics via lightweight beacon; no tag managers. |

---

## 3. JavaScript Execution Budget

| Phase | Budget |
|-------|--------|
| Main thread blocking (load) | **< 300ms** total |
| Long tasks (>50ms) | **0** on load; **< 2** per interaction |
| Hydration time (per route) | **< 100ms** |
| Route transition (client) | **< 150ms** paint |

**Technique:** Route-level code splitting, `React.lazy`, `Suspense` with skeleton. No vendor chunk > 50KB gzipped.

---

## 4. Rendering Strategy

| Route Type | Strategy |
|------------|----------|
| Public (/, /products, /training, /support/kb) | **SSG** (build-time) + ISR (60s) |
| Authenticated dashboards (/account, /orders, /admin, /analytics) | **SSR** (edge) + streaming |
| Real-time (AI chat, notifications) | **CSR** islands within SSR shell |
| Command palette, modals | Client-only, preloaded on hover/focus |

**No full SPA shell hydration** on public pages. Progressive enhancement.

---

## 5. Loading Patterns (Perceived Performance)

| Pattern | When | Skeleton Spec |
|---------|------|---------------|
| **Route skeleton** | Every route transition | Matches final layout: header, sidebar, content blocks, table rows. Shimmer if `!prefers-reduced-motion`. |
| **Component skeleton** | Async data in mounted component | Card, table row, chart placeholder. Same dimensions as loaded. |
| **Image placeholder** | All `<img>` | Blurhash (20px) → LQIP → HQ. Aspect ratio box. |
| **Font loading** | All text | `font-display: swap`; fallback metrics matched (size-adjust). |
| **Optimistic UI** | Mutations (like, archive, add) | Immediate visual update; rollback on error with toast. |

---

## 6. Caching Strategy

| Asset | Cache-Control | Invalidation |
|-------|---------------|--------------|
| HTML (SSR/SSG) | `public, max-age=0, must-revalidate` | ETag / content hash |
| JS/CSS (hashed) | `public, max-age=31536000, immutable` | Filename hash change |
| Fonts | `public, max-age=31536000, immutable` | — |
| Images (content) | `public, max-age=86400` | Content hash in URL |
| API (GET) | `private, max-age=60, stale-while-revalidate=300` | SWR pattern |
| API (POST/PATCH/DELETE) | `no-store` | — |

**Service Worker:** Precaches shell + critical routes. Runtime caches API GET (stale-while-revalidate). Offline fallback page.

---

## 7. Network Conditions for Testing

| Profile | RTT | Downlink | Use Case |
|---------|-----|----------|----------|
| **Mobile 4G** (primary) | 100ms | 1.6 Mbps | Budget baseline |
| **Mobile 3G** | 300ms | 780 Kbps | Stress test |
| **Offline** | — | — | SW fallback |
| **Desktop WiFi** | 50ms | 10+ Mbps | Dev baseline |

**Test in CI:** Lighthouse CI with Mobile 4G throttling. `lhci autorun` on PR.

---

## 8. Monitoring & Alerting (Production)

| Metric | Alert Threshold | Tool |
|--------|-----------------|------|
| LCP (p75) | > 3.0s | RUM (web-vitals) + Grafana |
| INP (p75) | > 300ms | RUM |
| CLS (p75) | > 0.15 | RUM |
| JS Error Rate | > 0.1% | Sentry |
| API p99 Latency | > 1.5s | OpenTelemetry |
| Cache Hit Ratio | < 90% | CDN logs |

---

## 9. Page Design Brief Performance Section (Template)

Every Part 1D page brief must include:
```markdown
## Performance Budget
- Target LCP: ___ms (element: ___)
- Target INP: ___ms (interaction: ___)
- JS Budget: ___KB (route chunk)
- CSS Budget: ___KB
- Skeleton: [Figma link]
- Code-split: [route/chunk name]
- Preload: [critical assets]
```

---

## 10. Prototype Performance Routes

- `/demo/performance` — LCP/INP/CLS live measurement (web-vitals)
- `/demo/bundle-report` — webpack/vite bundle analyzer iframe
- `/demo/skeleton-gallery` — All skeleton variants side-by-side