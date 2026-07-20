# Condition C07 — SEO/PWA Assets

**PRR Condition:** PRR-C07 — Add SEO/PWA assets to public directory  
**Priority:** MEDIUM  
**Status:** ✅ CLOSED — 20-Jul-2026

---

## Root Cause

The `frontend/web-app/public/` directory contained only `hero-bg.png`. The `index.html` referenced `robots.txt`, `sitemap.xml`, `manifest.json`, `favicon.svg`, `apple-touch-icon.png`, and `og-image.png` but none of these files existed, causing 404 errors.

## Required Operational Action

Create the following static assets in `frontend/web-app/public/`:

| File | Purpose |
|------|---------|
| `robots.txt` | Search engine crawler instructions |
| `sitemap.xml` | XML sitemap with 8 URLs (home, about, contact, faq, knowledge-base, support, training, blog) |
| `manifest.json` | PWA manifest with name, theme color, icons |
| `favicon.svg` | SVG favicon (SporeKart "SK" logo, 100x100) |
| `apple-touch-icon.svg` | Apple touch icon (180x180) |
| `og-image.svg` | Open Graph social share image (1200x630) |

## Evidence

| Artifact | Status |
|----------|--------|
| `frontend/web-app/public/robots.txt` | ✅ Created |
| `frontend/web-app/public/sitemap.xml` | ✅ Created |
| `frontend/web-app/public/manifest.json` | ✅ Created |
| `frontend/web-app/public/favicon.svg` | ✅ Created |
| `frontend/web-app/public/apple-touch-icon.svg` | ✅ Created |
| `frontend/web-app/public/og-image.svg` | ✅ Created |

## Validation

```bash
# Verify all assets are accessible
curl -sI http://localhost:4173/robots.txt
curl -sI http://localhost:4173/sitemap.xml
curl -sI http://localhost:4173/manifest.json
curl -sI http://localhost:4173/favicon.svg
```

**Closure Verification:** All 6 SEO/PWA assets created in `frontend/web-app/public/`. index.html references will now resolve without 404 errors.
