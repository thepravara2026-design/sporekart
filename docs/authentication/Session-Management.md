# Authentication — Session Management

**Screens:** `AuthLoadingPage`, `SessionExpiredPage`, `LoggedOutPage`, `AccessDeniedPage`
**Routes:** `/auth/loading`, `/session-expired`, `/access-denied` (LoggedOut available via component; previewed at `/preview/session`)

## Purpose
Communicate session lifecycle states clearly and give users a single obvious path forward. Built on `StatusScreen` for consistency.

## Screens

### AuthLoadingPage (`/auth/loading`)
- Post-auth transition / route guard resolver.
- Centered spinner + "Securing your session…" with brand mark.
- Non-blocking; meant to be replaced by the enterprise shell's real auth resolver.

### SessionExpiredPage (`/session-expired`)
- `Icon: clock`, title "Your session expired", body explaining re-authentication.
- Primary action "Sign in again" → `/login`; secondary "Back to home".
- Tone: reassuring, not alarming.

### LoggedOutPage (component; previewed)
- `Icon: log-out`, title "You've been signed out".
- Primary "Sign in" → `/login`; confirmation that the session ended safely.

### AccessDeniedPage (`/access-denied`)
- 403-style gate for signed-in users lacking permission.
- `Icon: slash`/`shield`, title "Access denied", body referencing the required role/permission.
- Primary "Go to dashboard" / secondary "Contact admin" (placeholder).
- Distinguishes from `ForbiddenPage` (unauthenticated/forbidden error route) by being part of the session flow.

## Behavior
- All use `StatusScreen` (icon, title, body, primary/secondary actions) for visual consistency.
- Actions are real anchors/buttons; no backend session logic implemented.

## Accessibility
- Single `<h1>` per screen; actions are native buttons/links.
- Icons `aria-hidden`; meaning conveyed by text.

## Open questions
- Real session expiry detection + redirect is owned by the enterprise shell / auth guard.
- `AccessDeniedPage` copy should reflect the actual RBAC role names once defined.
