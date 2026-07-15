# Changelog

## 0.6.0 - 2026-07-13
### Sprint 21 Part 7: Authentication Experience
- Authentication feature under `frontend/web-app/src/features/auth`: `AuthLayout`, `authClient` (UI-only stub), `useCountdown`, `SocialLogin`, `AuthAlert`, `StatusScreen`.
- Pages: `LoginPage` (phone/email + OTP, terms gate, social placeholders), `RegisterPage` (profile + role + dual consent), `ForgotPasswordPage`, `VerifyOtpPage` (6-digit OTP, resend countdown, demo mode), `SessionPages` (loading/expired/logged-out/access-denied), `ErrorPages` (401/403/Auth/Network/Server), `ErrorGallery`.
- Preview routes: `/preview/login`, `/preview/register`, `/preview/otp`, `/preview/session`, `/preview/auth-errors`.
- `App.tsx`: lazy routes + `isNonEnterpriseRoute` extended with `AUTH_ROUTES`; `/auth` placeholder now backed by real pages.
- Reused Design System v1.0.0 (Button, Input, OtpInput, Checkbox, Select, Icon, useScopedStyle); `auth.css` token-only, responsive, reduced-motion.
- Constraint honored: no backend auth/OTP/RBAC/session/API logic changed. `authClient` is the single swap target for real Supabase Auth.
- Docs: `docs/authentication/*` (Architecture, Login, Registration, OTP, Session-Management, Error-Pages, Accessibility, Review-Notes) + `docs/phase-6/sprint-21-part-7.md`.
- TypeScript: 0 errors; `npm run build`: success (per-route code-split).

## 0.5.0 - 2026-07-13
### Sprint 19 Part 1E: Design Governance & Foundation Certification
- **Governance Framework**: 12 new docs — design-governance, review-framework, quality-gates, component-governance, design-freeze-policy, documentation-standards, performance-standards, accessibility-certification, design-system-governance, frontend-certification, release-readiness, documentation-standards
- **Design Decision Records (8)**: DDR-001 Layout Strategy, DDR-002 Navigation Strategy, DDR-003 Typography System, DDR-004 Color System, DDR-005 Responsive Strategy, DDR-006 Accessibility Standard, DDR-007 Design Token Architecture, DDR-008 Component Freeze Policy
- **Governance Infrastructure**: `/docs/ddr/` (DDR index + 8 records), `/docs/ui/approval-log/` (template), `/docs/ui/design-briefs/`, `/docs/ui/component-docs/`, `/docs/ui/review-notes/`
- **Prototype Extensions**: `/design-system` showcase with 9 interactive sections (Colors, Typography, Spacing, Radius, Elevation, Sizing, Token Inspector, Theme Toggle, Responsive Preview)
- **Governance Artifacts**: `design-governance.md`, `review-framework.md`, `quality-gates.md`, `component-governance.md`, `design-freeze-policy.md`, `documentation-standards.md`, `performance-standards.md`, `accessibility-certification.md`, `design-system-governance.md`, `frontend-certification.md`, `release-readiness.md`, `documentation-standards.md`
- **Foundation Certified**: Sprint 19 Parts 1A-1E complete — Enterprise Product Experience Foundation certified for Sprint 20+

## 0.4.0 - 2026-07-13
### Sprint 19 Part 1D: Enterprise Design Language & Brand Guidelines
- **Documentation**: 12 new design language docs (design-language, brand-guidelines, color-system, typography, spacing-system, design-tokens, elevation-system, iconography, illustration-guidelines, theme-foundation, token-naming-convention, design-review-notes)
- **Token Architecture**: Centralized token system at `frontend/web-app/src/tokens/` (11 JSON files) + data export at `src/pages/design-tokens-data.ts`
- **Design Showcase**: `/design-system` route with interactive gallery — color palette (primitives + semantic), typography scale, spacing (primitives + semantic), radius (scale + component mappings), elevation (levels + component mapping), sizing (icons, illustrations, breakpoints, z-index), token inspector (search/filter), theme toggle (light/dark)
- **Design Language**: 11 visual principles, shape language, whitespace/depth/surface philosophy; brand identity (logo, voice, iconography, motion); color system (green primary scale, neutral, semantic, data viz, WCAG 2.2 AA validated); typography (modular 1.25 scale, clamp() responsive, system stack + mono); spacing (4px base, clamp() responsive); elevation (5 levels, rgba shadows); icons (24×24 base, 2px stroke, 7 sizes, filled variant); illustrations (5 sizes, 7 categories, line art + semantic accent)
- **Theme Foundation**: Light (implemented), Dark (specified), High Contrast (CSS-only), Brand (architecture) — data-theme switching, fallback chain, testing matrix
- **Token Naming**: CTI-inspired `{category}.{property}.{variant}.{state}.{scale}` convention; primitive→semantic→component hierarchy; linting rules; deprecation policy
- **Governance**: Sprint stops for design review before Part 1E

## 0.3.0 - 2026-07-12
### Sprint 19 Part 1C: Enterprise Web Experience — UX Standards & Accessibility Foundation
- **Documentation**: 11 new UX standards docs (ux-standards, responsive-strategy, accessibility-guidelines, keyboard-navigation, loading-experience, error-handling-guidelines, empty-state-guidelines, form-experience, microcopy-guidelines, performance-targets, review-notes)
- **Prototype Extensions**: 7 new demo routes at `/demo/*`
  - `/demo` — Gallery index
  - `/demo/responsive` — Viewport toggle (xs→2xl), grid overlay
  - `/demo/keyboard` — Live focus log, component patterns, shortcut cheat sheet
  - `/demo/loading` — Route/component/action skeletons, offline banner, retry
  - `/demo/errors` — 9 error patterns (404, 403, 401, 500, network, timeout, validation, conflict, rate-limit)
  - `/demo/empty` — 30+ empty states, 5 variants (standard, first-time, filtered, permission, error)
  - `/demo/forms` — Validation, OTP, address, checkout, file upload, date picker, combobox, auto-save
  - `/demo/microcopy` — Searchable gallery: buttons, links, labels, errors, toasts, empty, tooltips, dialogs, loading, terminology
- **Prototype Infrastructure**: Lazy-loaded demo routes with Suspense, Demo workspace added to navigation config
- **Governance**: Sprint stops for UX review before Part 1D

## 0.2.0 - 2026-07-12
### Sprint 19 Part 1B: Enterprise Web Experience — IA & Navigation Blueprint
- **Documentation**: 10 new UI architecture docs (IA model, sitemap, navigation strategy, layout blueprint, route architecture, breadcrumb guidelines, role-based navigation, search foundation, layout standards, review notes)
- **Prototype**: React + Vite + TypeScript navigation prototype at `frontend/web-app`
  - Layout shell: global header, workspace sidebar, breadcrumb bar, content area, command palette
  - 12 workspaces, ~50 routes, role-based visibility (8 roles)
  - Single source of truth config: `src/config/navigation.ts`
  - Accessible (skip link, landmarks, focus management, keyboard palette)
  - Responsive (desktop, laptop, tablet, mobile)
- **Governance**: Sprint stops for UX review before Part 1C

## 0.1.0 - 2026-07-10
- Initialized the SporeKart engineering foundation scaffold
- Added placeholder services, platform, infrastructure, contracts, testing, and shared package structure
- Added governance, standards, CI/CD, Docker, and developer experience placeholders
