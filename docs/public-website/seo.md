# Public Website — SEO Foundation

The `Seo` component (`src/public-website/Seo.tsx`) manages the document `<head>` for
public pages. It runs in a `useEffect` and is safe to mount/unmount (cleanup removes
the injected JSON-LD script).

## Injected Tags
| Tag | Source prop | Default |
| --- | --- | --- |
| `<title>` | `title` | `SporeKart \| Mushroom cultivation, simplified.` (suffixes `\| SporeKart`) |
| `meta[name="description"]` | `description` | — |
| `meta[name="robots"]` | `noindex` | `index, follow` (or `noindex, nofollow`) |
| `meta[property="og:title"]` | `title` | full title |
| `meta[property="og:description"]` | `description` | — |
| `meta[property="og:type"]` | `type` | `website` |
| `meta[property="og:url"]` | `canonical` | — |
| `meta[property="og:image"]` | `image` | — |
| `meta[name="twitter:card"]` | `image` | `summary_large_image` / `summary` |
| `meta[name="twitter:title"]` | `title` | full title |
| `meta[name="twitter:description"]` | `description` | — |
| `link[rel="canonical"]` | `canonical` | — |
| `script[type="application/ld+json"]` | `structuredData` | — |

## Structured Data
Pass any JSON-LD object or array to `structuredData`. Example used in the SEO preview:

```json
{
  "@context": "https://schema.org",
  "@type": "Organization",
  "name": "SporeKart",
  "url": "https://sporekart.example.com"
}
```

## Usage
Each `PublicRoutePlaceholder` already calls `Seo` with `title`, `description`, and a
`canonical` derived from the route path. The preview route `/preview/public-seo`
demonstrates the injected head; inspect devtools `<head>` to verify.

## Future
- Sitemap and `robots.txt` generation (later part).
- Per-page Open Graph image defaults.
- Hreflang for locale variants (if multi-locale is adopted).
