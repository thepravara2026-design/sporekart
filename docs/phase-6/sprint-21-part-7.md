# Sprint 21 · Part 7 — Authentication Experience

**Phase:** 6 — Enterprise Product Experience
**Track:** Authentication Engineering (Frontend Experience)
**Status:** ✅ Implemented · Pending design + product review (sprint stops here)
**Build:** `tsc` 0 errors · `vite build` success (per-route code-split)
**Constraint:** UI-only. No backend auth, OTP, RBAC, session, or API logic touched.

---

## 1. Summary

Shipped a complete, production-shaped **Authentication Experience** for SporeKart: a unified login, registration, OTP verification, session-state, and error-handling surface built entirely on the Enterprise Design System v1.0.0. The experience is phone/email + OTP first (passwordless), consistent with the existing public-website and operations-platform patterns, and is wired into the non-enterprise route space so it can be previewed and reviewed without disturbing the enterprise app shell.

All flows are driven by a **UI-only stub client** (`authClient.ts`) that simulates latency and outcomes. It is explicitly marked for replacement by the real Supabase Auth + RBAC integration; no security, token, or session logic was implemented or modified.

---

## 2. Scope

**In scope**
- `LoginPage` (channel switch phone/email, remember me, terms acceptance, social placeholders, send-OTP entry).
- `RegisterPage` (profile capture, role select, dual consent, send-OTP entry).
- `ForgotPasswordPage` (recovery request with success state).
- `VerifyOtpPage` (6-digit OTP, resend countdown, demo mode, success/redirect).
- Session pages: `AuthLoadingPage`, `SessionExpiredPage`, `AccessDeniedPage`, `LoggedOutPage` (LoggedOut available via component; previewed).
- Error pages: `UnauthorizedPage` (401), `ForbiddenPage` (403), `AuthErrorPage`, `NetworkErrorPage`, `ServerErrorPage`, plus an `/auth-error` gallery.
- Reusable `AuthLayout`, `SocialLogin`, `AuthAlert`, `StatusScreen`, `useCountdown` hook, and `auth.css` (token-only, responsive, reduced-motion).
- Five preview routes for isolated review.

**Out of scope (per mandate)**
- Real authentication, OTP delivery, RBAC enforcement, session/token storage, social provider OAuth, and any backend or API changes.
- Navigation after a successful login (enterprise shell owns post-auth routing).

---

## 3. Files Changed / Created

```
frontend/web-app/src/features/auth/
├── auth.css                         # token-only styles, responsive, reduced-motion, preview scaffold
├── AuthLayout.tsx                   # split brand + form layout (a11y, mobile brand mark)
├── authClient.ts                    # UI-only stub (simulated latency/outcomes)
├── index.ts                         # barrel export
├── hooks/
│   └── useCountdown.ts              # resend-cooldown + OTP expiry timer
├── components/
│   ├── SocialLogin.tsx              # Google/Apple/Facebook placeholders (not integrated)
│   ├── AuthAlert.tsx                # inline status/error banner
│   └── StatusScreen.tsx             # reusable centered status (loading/expired/denied)
├── pages/
│   ├── LoginPage.tsx
│   ├── RegisterPage.tsx
│   ├── ForgotPasswordPage.tsx
│   ├── VerifyOtpPage.tsx
│   ├── SessionPages.tsx             # SessionExpired / LoggedOut / AuthLoading / AccessDenied
│   ├── ErrorPages.tsx               # 401 / 403 / Auth / Network / Server
│   └── ErrorGallery.tsx             # /auth-error gallery of all error states
└── preview/
    ├── PreviewScaffold.tsx          # viewport toggle + frame for iframe review
    ├── LoginPreview.tsx
    ├── RegisterPreview.tsx
    ├── OtpPreview.tsx                # exported as AuthOtpPreview (name clash with design-system OtpPreview)
    ├── SessionPreview.tsx
    └── AuthErrorsPreview.tsx

frontend/web-app/src/App.tsx         # lazy routes + isNonEnterpriseRoute AUTH_ROUTES set
```

No changes outside `src/features/auth` except `App.tsx` route wiring.

---

## 4. Routes

| Path | Component | Notes |
|------|-----------|-------|
| `/login` | `LoginPage` | Channel switch (phone/email), terms gate, social placeholders |
| `/register` | `RegisterPage` | Profile + role + dual consent |
| `/forgot-password` | `ForgotPasswordPage` | Recovery request + success |
| `/verify-otp` | `VerifyOtpPage` | Requires `location.state`; `?demo=1` for isolated preview |
| `/auth/loading` | `AuthLoadingPage` | Post-auth transition screen |
| `/session-expired` | `SessionExpiredPage` | Re-auth prompt |
| `/access-denied` | `AccessDeniedPage` | 403-style gate for signed-in users |
| `/auth-error` | `AuthErrorsGallery` | Gallery of 401/403/Auth/Network/Server |

**Preview routes** (for design/product review, iframe-embeddable):
`/preview/login`, `/preview/register`, `/preview/otp`, `/preview/session`, `/preview/auth-errors`.

All auth + preview routes are added to `isNonEnterpriseRoute` so they render with `PublicLayout`/`AuthLayout` rather than the enterprise shell. The `/auth` placeholder previously in the nav config is now backed by real pages.

---

## 5. Design System Compliance

All components reuse the **Enterprise Design System v1.0.0** primitives — no new components were introduced:

- `Button` (variants `primary`/`secondary`/`ghost`/`link`, `size="lg"`, `fullWidth`, `loading`, `leftIcon`)
- `Input` (label, `prefix` icon, `error`, `helperText`, `type="tel"|"email"`)
- `OtpInput` (`length={6}`, `value`/`onChange`/`onComplete`, `error`, `size="lg"`)
- `Checkbox` (`checked`, `onChange`, `error`, label-as-children)
- `Select` (composite) — role picker on register
- `Icon` — brand + UI iconography (verified present in registry: `leaf`, `smartphone`, `mail`, `lock`, `shield`, `key`, `user-plus`, `log-in`, `log-out`, `slash`, `alert-circle`, `alert-triangle`, `wifi`, `database`, `clock`, `check-circle`, `info`, `x-circle`, `refresh`, `arrow-right`, `facebook`, `apple`, `google`)
- `useScopedStyle` for any locally scoped, token-driven styling.

**Styling** is token-only via `auth.css` (semantic tokens: `--color-bg-primary-default`, `--color-bg-primary-weak`, `--color-text-on-primary`, `--color-focus-ring`, `--radius-*`, `--space-*`, `--text-*`, `--shadow-3`, `--duration-fast`, `--easing-standard`, etc.). No hardcoded colors, spacing, or font sizes. Fully responsive (split → stacked at `≤960px`), supports `prefers-reduced-motion`, and high-contrast aware.

---

## 6. UX / Visual Notes

- **Unified layout:** every auth screen uses `AuthLayout` — a two-column split with a left brand panel (SporeKart leaf, value proposition, trust badges) and a right form panel. On mobile the brand collapses to a compact mark and the form goes full-width.
- **Motion is purposeful:** button loading spinners, OTP auto-advance/focus, and a subtle fade-in on the form panel. All disabled under `prefers-reduced-motion`.
- **Feedback hierarchy:** inline `AuthAlert` for form-level errors, per-field `Input`/`Checkbox` errors, `StatusScreen` for full-page states (loading/expired/denied), and a centered `OtpInput` with a live resend countdown.
- **Trust & clarity:** legal consent is explicit (terms + privacy) on login/register; recovery and OTP screens explain what happens next; social buttons are clearly placeholder ("not enabled yet") to avoid implying a working integration.
- **Consistency:** typography, spacing, radius, focus rings, and elevation match the rest of the SporeKart web experience.

---

## 7. Accessibility

- Semantic landmarks: `AuthLayout` renders a `<main>` form panel and an `aria-hidden` decorative brand panel.
- Labels: every `Input`/`OtpInput` has an associated `<label>`; OTP uses `role="group"` + per-digit `aria-label`.
- Errors: field errors use `aria-invalid` + `aria-describedby`; form-level alerts use `role="alert"`.
- Focus: visible focus rings via `--color-focus-ring`; OTP auto-advances and supports arrow-key navigation, Backspace-to-clear, Home/End, and paste-to-fill.
- Channel switch (phone/email) is a `role="radiogroup"` with `aria-checked`; keyboard operable.
- `prefers-reduced-motion` respected; color contrast validated against the semantic token palette (WCAG 2.2 AA target).
- Links use real anchors; buttons use `type="button"`/`type="submit"` appropriately; no `tabindex` hacks.

---

## 8. Responsiveness

- **≥960px:** split layout (brand panel ~45%, form panel ~55%), centered card.
- **<960px:** single column, brand panel collapses to a compact top mark; form full-width with safe-area padding.
- Inputs, OTP boxes, and buttons are `fullWidth` and scale with `--text-*`/`--space-*` tokens. Preview scaffold exposes xs→2xl viewport toggle to validate breakpoints.

---

## 9. Edge Cases & Validation

- **Identifier validation:** phone (`+digits, 8–15`) and email regex before requesting OTP; inline field errors and focus management.
- **Terms gate:** login requires accepted Terms/Privacy before sending OTP; explicit error if skipped.
- **OTP:** rejects `<4` chars and the reserved failure code `000000`; auto-submits on 6-digit completion; resend disabled during a 30s cooldown; OTP expiry countdown (5 min) shown.
- **Demo mode:** `/verify-otp?demo=1` seeds a destination so the screen can be reviewed standalone (no prior navigation state needed).
- **Forgot password:** validates the channel identifier, then shows a success confirmation state (no real email sent).
- **Social:** buttons present but call a stub that returns "not enabled yet" — no silent failure states.
- **Network/Server errors:** dedicated full-page states with retry affordances for the error gallery and real error routes.

---

## 10. How to Run / Test

```bash
# from repo root
cd frontend/web-app
npm install
npm run dev
```

Open in the browser:
- `http://localhost:5173/login`
- `http://localhost:5173/register`
- `http://localhost:5173/forgot-password`
- `http://localhost:5173/verify-otp?demo=1`  (enter any 6 digits except `000000`)
- `http://localhost:5173/session-expired`
- `http://localhost:5173/access-denied`
- `http://localhost:5173/auth-error`  (gallery of 401/403/Auth/Network/Server)

Isolated review (iframe-embeddable):
- `/preview/login`, `/preview/register`, `/preview/otp`, `/preview/session`, `/preview/auth-errors`

**Stub behavior (authClient.ts):**
- OTP: any 6-character code except `000000` verifies; `<4` chars rejected.
- Latency simulated (~900ms) so loading states are visible.
- Social login always returns "not enabled yet".

**Quality gates:**
```bash
npm run build      # tsc -b && vite build  → 0 errors, code-split chunks
```

---

## 11. Open Questions / Assumptions

1. **Post-auth routing** is owned by the enterprise shell; these screens stop at the success/redirect boundary (e.g., `/verify-otp` navigates to a placeholder destination). Confirm the real post-verify target.
2. **Real OTP delivery + Supabase Auth** integration point: `authClient.ts` is the single swap target. No interface changes expected beyond implementation.
3. **Social providers** (Google/Apple/Facebook) are placeholders; the integration, branding review, and copy live with the platform auth team.
4. **"Remember me"** is captured in state only; persistence is a backend concern.
5. **Role gating on register:** only `customer` is self-serve; `grower`/`partner` are invite-only (disabled in `Select`). Confirm provisioning flow.

---

## 12. Status & Next Steps

- ✅ Implementation complete; `tsc` 0 errors; `vite build` success.
- ⏸ **Sprint stop:** awaiting design review (visual/lang) and product review (copy/flow) before backend integration.
- **Next:** on approval, replace `authClient.ts` with Supabase Auth + RBAC; wire real post-auth navigation; enable social providers; add instrumentation (login success/failure, OTP resends).
- **Docs:** see `docs/authentication/*` (Architecture, Login, Registration, OTP, Session-Management, Error-Pages, Accessibility, Review-Notes).
