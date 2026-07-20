# Sprint C — Performance Report

## Build Performance
- Build time: 11.72s
- Total chunks: Multiple (lazy-loaded routes)
- Main bundle: 307 KB (gzip: 88 KB)
- CSS bundle: 34.5 KB (gzip: 7.35 KB)

## Service Worker (C-005)
- **Strategy**: Cache-first (precached assets), stale-while-revalidate (navigation)
- **Precache**: `/`, `/index.html`
- **Activation**: Cleans old caches, claims clients immediately
- **Impact**: Enables offline fallback for cached pages

## Performance Budgets (C-009)
| Metric | Budget | Assertion |
|--------|--------|-----------|
| Performance score | ≥ 80 | Warn |
| Accessibility score | ≥ 90 | Error |
| Best Practices score | ≥ 90 | Error |
| SEO score | ≥ 90 | Error |
| First Contentful Paint | < 2000 ms | Warn |
| Largest Contentful Paint | < 3000 ms | Warn |
| Time to Interactive | < 4000 ms | Warn |
| Total Blocking Time | < 300 ms | Warn |
| Cumulative Layout Shift | < 0.1 | Warn |
| Unused JavaScript | 0 bytes | Warn |
| Unused CSS | 0 bytes | Warn |
| Optimized Images | 0 violations | Error |

## Existing Lazy Loading
- All preview and detail routes use `React.lazy()` with `<Suspense>`
- Skeleton fallback provided during loading
- No render-blocking third-party scripts
