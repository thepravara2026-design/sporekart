# Homepage SEO

**File:** `src/public-website/home/HomePage.tsx` (via `Seo` from Part 1)

## Injected Head Tags
- `<title>`: SporeKart — India's Trusted Mushroom Cultivation Ecosystem
- `meta[name="description"]`: cultivation ecosystem summary
- `meta[name="robots"]`: index, follow
- Open Graph: `og:title`, `og:description`, `og:type=website`, `og:url`
- Twitter: `twitter:card=summary_large_image`, `twitter:title`, `twitter:description`
- `link[rel="canonical"]`: `https://sporekart.example.com/`

## Structured Data (JSON-LD)
- `Organization` — name, url, slogan, description
- `WebSite` — name, url
- `BreadcrumbList` — Home → `/`

## Semantic Structure
- One H1 (hero). Section titles H2. Card/step titles H3.
- Landmarks: `header` (Part 1), `main`, `footer` (Part 1).
- Image/visual placeholders use `aria-label`; decorative visuals `aria-hidden`.

## Notes
- No backend changes; canonical/OG domains are placeholders (`sporekart.example.com`).
- Sitemap/robots.txt generation deferred to a later part.
