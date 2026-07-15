# Authentication — Architecture

**Sprint 21 · Part 7** · UI-only · Design System v1.0.0

## Layering

```
App.tsx (routing)
  └─ isNonEnterpriseRoute + AUTH_ROUTES
       ├─ /login            → LoginPage
       ├─ /register         → RegisterPage
       ├─ /forgot-password  → ForgotPasswordPage
       ├─ /verify-otp       → VerifyOtpPage   (needs location.state; ?demo=1 for isolation)
       ├─ /auth/loading     → AuthLoadingPage
       ├─ /session-expired  → SessionExpiredPage
       ├─ /access-denied    → AccessDeniedPage
       ├─ /auth-error       → AuthErrorsGallery
       └─ /preview/*        → isolated review scaffolds
```

## Key modules

| Module | Responsibility |
|--------|----------------|
| `AuthLayout` | Two-column brand+form shell; responsive collapse; `aria-hidden` brand panel. |
| `authClient` | UI-only stub (`sendOtp`, `verifyOtp`, `register`, `login`, `forgotPassword`, `socialLogin`). Simulated latency + deterministic demo outcomes. **Single swap target for real auth.** |
| `useCountdown` | Resend cooldown (30s) and OTP expiry (300s) timers. |
| `SocialLogin` | Google/Apple/Facebook buttons (placeholder, not integrated). |
| `AuthAlert` | Inline status/error banner (`type: info | success | warning | error`). |
| `StatusScreen` | Reusable centered full-page state (icon, title, body, actions). |
| `auth.css` | Token-only styles, responsive grid, reduced-motion, preview scaffold. |

## Data flow (login)

1. User picks channel (phone/email) → `LoginPage` validates identifier.
2. Terms gate enforced; on submit calls `authClient.sendOtp(channel, destination)`.
3. On success navigates to `/verify-otp` with `{ channel, destination, remember, flow }` in `location.state`.
4. `VerifyOtpPage` calls `authClient.verifyOtp(...)` on 6-digit completion; `StatusScreen` shows success then redirects.

## Design-system boundary

Every primitive is imported from `src/design-system` (`Button`, `Input`, `OtpInput`, `Checkbox`, `Select`, `Icon`, `useScopedStyle`). No new primitives were created. Styling is confined to `auth.css` using semantic tokens only.

## Swap-in contract (backend)

Replace the body of `authClient` methods with real Supabase Auth + RBAC calls. The method signatures and return shapes (`SendOtpResult`, `AuthResult`) are stable; no screen changes required for the happy path. Session/token storage remains a backend/platform concern.

## Constraints honored

- No backend, OTP, RBAC, session, or API logic implemented or modified.
- No navigation beyond the auth boundary.
- No new design-system components; full token compliance.
