# Authentication — Login

**Screen:** `LoginPage` · route `/login` · preview `/preview/login`

## Purpose
Entry point for returning users. Phone/email-first, OTP-based (passwordless), with explicit consent and placeholder social options.

## Layout
- `AuthLayout` split view: brand panel (left) + form panel (right).
- Header: eyebrow "Sign in", title "Access your workspace", subtitle.
- Channel switch (`role="radiogroup"`): **Phone** / **Email**.
- Identifier `Input` with channel-aware label, placeholder, prefix icon, and validation.
- `Remember me` checkbox + "Forgot access?" link.
- Terms & Privacy `Checkbox` (required gate).
- Inline `AuthAlert` on error.
- Primary `Button` ("Send secure code") with `loading` state.
- Divider + `SocialLogin` (Google/Apple/Facebook placeholders).
- Footer: "New to SporeKart? Create an account" + support/privacy links.

## Behavior
- Switching channel clears the identifier and field error.
- Submit validates the identifier format (phone regex / email regex) and the terms checkbox before calling `authClient.sendOtp`.
- On success → navigate to `/verify-otp` with `{ channel, destination, remember, flow: 'login' }`.
- On failure → `AuthAlert` with the stubbed message.

## Validation
- Phone: `^[+]?[\d\s()-]{8,15}$`
- Email: `^[^\s@]+@[^\s@]+\.[^\s@]+$`
- Terms must be accepted (explicit error if skipped).
- Field errors set `aria-invalid` + focus the field.

## Accessibility
- Channel switch keyboard operable with `aria-checked`.
- All inputs labeled; errors use `role="alert"`.
- Focus moves to the first invalid field on validation failure.

## Open questions
- Real post-verify destination is owned by the enterprise shell.
- Social providers pending platform auth integration.
