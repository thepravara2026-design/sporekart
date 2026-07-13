# Performance Standards — SporeKart Enterprise Web Application

## 1. Core Web Vitals Targets (Production)

| Metric | Good | Budget (Max) | Measurement |
|--------|------|--------------|-------------|
| **LCP** (Largest Contentful Paint) | ≤ 2.5s | **2.5s** | Lighthouse (Mobile 4G), RUM p75 |
| **INP** (Interaction to Next Paint) | ≤ 200ms | **200ms** | Lighthouse + RUM p75 |
| **CLS** (Cumulative Layout Shift) | ≤ 0.1 | **0.1** | Lighthouse, RUM p75 |
| **TTFB** (Time to First Byte) | ≤ 800ms | **800ms** | Server Timing, RUM |
| **FCP** (First Contentful Paint) | ≤ 1.8s | **1.8s** | Lighthouse, RUM |

**Measurement:** 75th percentile (p75) across all users, Mobile 4G throttling (RTT 150ms, 1.6 Mbps down)

---

## 2. Resource Budgets (Per Page Load)

| Resource | Budget (gzipped) | Enforcement |
|----------|------------------|-------------|
| **Total Initial Transfer** | **350 KB** | CI Gate (G4) |
| **JavaScript** | **170 KB** | CI Gate (G4) |
| **CSS** | **35 KB** | CI Gate (G4) |
| **Fonts** | **50 KB** | CI Gate (G4) |
| **Images (above fold)** | **100 KB** | CI Gate (G4) |
| **Requests** | **< 40** | CI Gate (G4) |

**Per-Route JS Budget:**

| Route Type | Budget (gz) |
|------------|-------------|
| Public (/, /products) | 80 KB |
| Authenticated Dashboard | 120 KB |
| Data-heavy (Analytics, Admin) | 170 KB |
| Forms/Checkout | 100 KB |

---

## 3. Rendering Strategy

| Route Type | Strategy | Rationale |
|------------|----------|-----------|
| **Public** (/, /products, /training, /support/kb) | **SSG** (build-time) + ISR (60s) | Fastest LCP; SEO; cacheable |
| **Authenticated** (/account, /orders, /admin, /analytics) | **SSR** (edge) + Streaming | Auth context; personalized; fresh data |
| **Real-time** (AI chat, notifications) | **CSR Islands** in SSR shell | Interactivity; WebSocket |
| **Command Palette, Modals** | Client-only (lazy) | On-demand; preload on hover |

**No full SPA shell hydration on public pages.**

---

## 4. Code Splitting Strategy

### 4.1 Route-Level Splitting (Mandatory)

```tsx
// Every route = separate chunk
const Dashboard = lazy(() => import('./pages/Dashboard'));
const ProductDetail = lazy(() => import('./pages/ProductDetail'));
```

### 4.2 Component-Level Splitting

```tsx
// Heavy components lazy-loaded
const DataTable = lazy(() => import('./components/DataTable'));
const Chart = lazy(() => import('./components/Chart'));
const RichTextEditor = lazy(() => import('./components/RichTextEditor'));
```

### 4.3 Vendor Chunking

```js
// vite.config.ts
build: {
  rollupOptions: {
    output: {
      manualChunks: {
        'vendor-react': ['react', 'react-dom', 'react-router-dom'],
        'vendor-ui': ['@radix-ui/react-dialog', '@radix-ui/react-dropdown-menu'],
        'vendor-form': ['react-hook-form', '@hookform/resolvers', 'zod'],
        'vendor-data': ['@tanstack/react-query', 'axios'],
      }
    }
  }
}
```

**Vendor Chunk Max:** 50 KB gzipped each

---

## 5. Loading Patterns

### 5.1 Route Skeleton (Every Route)

```tsx
<Suspense fallback={<RouteSkeleton />}>
  <Routes>
    <Route path="/dashboard" element={<Dashboard />} />
  </Routes>
</Suspense>
```

**RouteSkeleton** matches final layout: header, sidebar, content blocks, table rows.

### 5.2 Component Skeleton (Async Data)

```tsx
<Suspense fallback={<TableSkeleton rows={5} />}>
  <DataTable data={query.data} />
</Suspense>
```

### 5.3 Image Loading

| Image Role | Pattern |
|------------|---------|
| **LCP Hero/Product** | `fetchpriority="high" loading="eager"` + blurhash (20px) |
| **Gallery/Thumbs** | `loading="lazy"` + blurhash + aspect-ratio box |
| **Avatars** | `loading="lazy"` + colored initials fallback (CSS) |
| **Icons** | Inline SVG (no network) |

**Blurhash:** Generated at build; 20px base64 PNG as background

### 5.4 Font Loading

```html
<!-- Preload critical fonts -->
<link rel="preload" as="font" type="font/woff2" crossorigin
  href="/fonts/inter-var.woff2" />

<!-- font-display: swap for all -->
@font-face {
  font-family: 'Inter';
  src: url('/fonts/inter-var.woff2') format('woff2');
  font-display: swap;
  /* Fallback metrics matched */
  size-adjust: 100%;
  ascent-override: 90%;
  descent-override: 25%;
}
```

**Fonts:** WOFF2 only; subset Latin + Devanagari; max 2 families

---

## 6. Caching Strategy

| Asset | Cache-Control | Invalidation |
|-------|---------------|--------------|
| **HTML (SSR/SSG)** | `public, max-age=0, must-revalidate` | ETag / content hash |
| **JS/CSS (hashed)** | `public, max-age=31536000, immutable` | Filename hash change |
| **Fonts** | `public, max-age=31536000, immutable` | — |
| **Images (content)** | `public, max-age=86400` | Content hash in URL |
| **API GET** | `private, max-age=60, stale-while-revalidate=300` | SWR pattern |
| **API POST/PATCH/DELETE** | `no-store` | — |

### Service Worker
- **Precache:** App shell + critical routes
- **Runtime:** API GET (stale-while-revalidate 5min)
- **Offline:** Fallback page + cached data indicator
- **Update:** `skipWaiting` + `clients.claim`

---

## 6. Network Conditions for Testing

| Profile | RTT | Downlink | Use Case |
|---------|-----|----------|----------|
| **Mobile 4G (Primary)** | 150ms | 1.6 Mbps | Budget baseline |
| **Mobile 3G** | 300ms | 780 Kbps | Stress test |
| **Offline** | — | — | SW fallback |
| **Desktop WiFi** | 50ms | 10+ Mbps | Dev baseline |

**CI Test:** Lighthouse CI with Mobile 4G throttling

---

## 7. Monitoring & Alerting (Production)

| Metric | Alert Threshold | Tool |
|--------|-----------------|------|
| **LCP (p75)** | > 3.0s | RUM (web-vitals) + Grafana |
| **INP (p75)** | > 300ms | RUM + Grafana |
| **CLS (p75)** | > 0.15 | RUM + Grafana |
| **JS Error Rate** | > 0.1% | Sentry |
| **API p99 Latency** | > 1.5s | OpenTelemetry + Grafana |
| **Cache Hit Ratio** | < 90% | CDN logs |
| **Bundle Size (gz)** | > 180 KB | CI + Bundle analyzer |

---

## 8. CI/CD Performance Gates

```yaml
# .github/workflows/perf.yml
jobs:
  lighthouse:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
      - run: npm ci && npm run build
      - uses: treosh/lighthouse-ci-action@v11
        with:
          urls: |
            http://localhost:4173/
            http://localhost:4173/products
            http://localhost:4173/dashboard
          configPath: .lighthouserc.json
          budgetPath: .lighthouse-budget.json

  bundle-analyzer:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: npm ci && npm run build
      - run: npx vite-bundle-analyzer dist/stats.json
```

### .lighthouse-budget.json

```json
{
  "ci": {
    "assert": {
      "assertions": {
        "categories:performance": ["error", { "minScore": 0.95 }],
        "categories:accessibility": ["error", { "minScore": 1.0 }],
        "categories:best-practices": ["error", { "minScore": 0.95 }]
      },
      "budgets": [
        { "resourceSizes": [{ "resourceType": "script", "budget": 170 }, { "resourceType": "css", "budget": 35 }, { "resourceType": "font", "budget": 50 }, { "resourceType": "total", "budget": 350 }] }
      ]
    }
  }
}
```

---

## 9. Font Optimization Checklist

- [ ] WOFF2 only
- [ ] Subset: Latin + Devanagari
- [ ] `font-display: swap`
- [ ] Fallback metrics matched (size-adjust, ascent-override, descent-override)
- [ ] Preload critical font (Inter var)
- [ ] Self-hosted (no Google Fonts CDN)
- [ ] Total font transfer ≤ 50 KB gz

---

## 9. Image Optimization Checklist

- [ ] WebP + AVIF ( `<picture>` )
- [ ] Responsive `srcset` (1x, 2x, 3x)
- [ ] `sizes` attribute for art direction
- [ ] Blurhash placeholder (20px)
- [ ] Aspect-ratio boxes (no CLS)
- [ ] Lazy load below fold
- [ ] LCP image: `fetchpriority="high"` + `loading="eager"`
- [ ] Compressed (WebP q75, AVIF q50)

---

## 10. Version History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | Sprint 19 Part 1E | Enterprise Performance Team | Initial standards |

---

**Authority:** Principal Performance Engineer  
**Review Cycle:** Quarterly (aligned with Lighthouse CI updates)  
**Effective:** Sprint 19 Part 1E certification