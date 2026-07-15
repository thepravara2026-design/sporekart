# Authentication — Error Pages

**Screens:** `UnauthorizedPage` (401), `ForbiddenPage` (403), `AuthErrorPage`, `NetworkErrorPage`, `ServerErrorPage`
**Routes:** `/auth-error` (gallery) · individual states previewed at `/preview/auth-errors`

## Purpose
A coherent set of authentication/authorization failure states with a consistent layout, plain-language copy, and a clear recovery action. Built on `StatusScreen`.

## States

| State | Route/use | Icon | Tone | Primary action |
|-------|-----------|------|------|----------------|
| Unauthorized (401) | `UnauthorizedPage` | `key` / `lock` | "Sign in to continue" | Sign in → `/login` |
| Forbidden (403) | `ForbiddenPage` | `slash` | "You don't have access" | Go home / Contact admin |
| Auth error | `AuthErrorPage` | `alert-circle` | "Something went wrong with sign-in" | Try again → `/login` |
| Network error | `NetworkErrorPage` | `wifi` | "We can't reach SporeKart" | Retry |
| Server error | `ServerErrorPage` | `database` | "Our servers hit a snag" | Retry / Support |

## Gallery (`/auth-error`)
`ErrorGallery` renders all five states in a responsive grid so design/product can review copy, icon, and action placement side by side. Each card links to its standalone route.

## Behavior
- Each page uses `StatusScreen`: icon, title (`<h1>`), body, primary + optional secondary action.
- Retry actions are placeholders (no backend); "Contact admin"/"Support" are anchors.
- Copy follows the microcopy guidelines: plain language, no blame, one clear next step.

## Accessibility
- One `<h1>` per page; recovery action is a native button/link.
- Icons decorative (`aria-hidden`); meaning is textual.
- Color is never the sole signal — icon + label + text body provided.

## Design notes
- Visual language matches the rest of the auth experience (same card, radius, elevation, focus ring).
- Error color uses semantic `--color-text-error` / `--color-border-error` tokens.

## Open questions
- Wire real error boundaries / route guards to render these states on actual 401/403/network/server failures.
