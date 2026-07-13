# Public Website — Routing

## Route Table (placeholders)
All paths render `PublicRoutePage`, which resolves the matching `PublicRouteDef`
from `config.ts` and renders `PublicRoutePlaceholder` inside `PublicLayout`.

| Path | Label | Description |
| --- | --- | --- |
| `/` | Home | Public home and brand entry point |
| `/about` | About | About SporeKart |
| `/products` | Products | Public product catalog |
| `/training` | Training | Training & courses |
| `/blog` | Blog | News and articles |
| `/contact` | Contact | Contact us |
| `/faq` | FAQ | Frequently asked questions |
| `/certifications` | Certifications | Certifications & compliance |
| `/privacy-policy` | Privacy Policy | Privacy policy |
| `/terms-and-conditions` | Terms & Conditions | Terms of service |
| `/refund-policy` | Refund Policy | Refund policy |
| `/shipping-policy` | Shipping Policy | Shipping policy |
| `/auth` | Sign In | Authentication entry point |

## Preview Routes
| Path | Component |
| --- | --- |
| `/preview/public-layout` | `PublicLayoutPreview` |
| `/preview/public-header` | `PublicHeaderPreview` |
| `/preview/public-footer` | `PublicFooterPreview` |
| `/preview/public-navigation` | `PublicNavigationPreview` |
| `/preview/public-seo` | `PublicSeoPreview` |

## Wiring in `App.tsx`
- `PUBLIC_ROUTE_PATHS` — 13 paths mapped to `<PublicRoutePage />`.
- `PUBLIC_PREVIEW_ROUTES` — 5 preview paths mapped to lazy preview components.
- `isNonEnterpriseRoute()` decides whether to render the public branch (no enterprise chrome).
- Public + preview routes are **lazy-loaded** to keep the main bundle lean.

## Navigation Map
- `PUBLIC_PRIMARY_NAV` (header): Products, Training, Blog, About, Contact.
- `PUBLIC_FOOTER_NAV` (footer): all public routes grouped into Explore / Company / Policies columns.
- Active states use react-router `NavLink` (`end` for `/`).
- Admin/enterprise routes are **never** listed in the public header or footer.

## Notes
- The legacy `public` workspace in `src/config/navigation.ts` (Sprint 19 prototype:
  Discover) still exists. Clicking its sidebar links switches to the public branch.
  Consolidation of that prototype into this foundation is deferred to a later cleanup.
- `roles: 'public'` semantics apply to all public routes (no auth required).
