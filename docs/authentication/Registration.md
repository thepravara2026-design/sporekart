# Authentication — Registration

**Screen:** `RegisterPage` · route `/register` · preview `/preview/register`

## Purpose
Self-serve account creation for **Customers**. Collects profile, captures consent, then triggers phone OTP verification (same `/verify-otp` flow as login, tagged `flow: 'register'`).

## Layout
- `AuthLayout` split view.
- Header: eyebrow "Create account", title "Set up your profile", subtitle.
- `Full name` Input (autocomplete `name`).
- `Phone number` Input (tel).
- `Email (optional)` Input (email).
- `Select` — "I am registering as": `Customer` (enabled), `Grower (invite only)` / `Partner (invite only)` (disabled).
- Consent `Checkbox` (service communications on this number).
- Privacy Policy `Checkbox` (required).
- Inline `AuthAlert` on error.
- Primary `Button` ("Create account") with `loading` state.
- Footer: "Already have an account? Sign in".

## Behavior
- Validates name, phone, optional email, consent, and privacy before `authClient.sendOtp('phone', ...)`.
- On success → navigate to `/verify-otp` with `state.profile = { fullName, phone, email?, role }`.

## Validation
- Full name required.
- Phone: `^[+]?[\d\s()-]{8,15}$`.
- Email (if provided): standard email regex.
- Both consent checkboxes required; errors surfaced via `Checkbox error` + `AuthAlert`.

## Design notes
- Role picker restricts self-serve to `customer`; `grower`/`partner` are disabled (invite-only) — confirm provisioning flow with product.
- No password captured (passwordless OTP). If a credential model is later required, use the design-system `Password` component (show/hide + strength) — not yet wired.

## Accessibility
- Labels, `aria-invalid`, `role="alert"` on errors; focus first invalid field on submit.
- `Select` is keyboard navigable with helper text describing invite-only roles.
