# Sprint E — Production Hardening Implementation Plan

**Program:** SporeKart Enterprise Release Program
**Phase:** Production Hardening Sprint E
**Source:** RC1 Release Audit (RC1_RELEASE_AUDIT/)
**Scope:** P0 blockers only — no features, no refactoring, no UX changes

---

## P0-01: No Real Authentication Provider

### Problem
`authClient.ts` is a UI-only mock stub. All auth methods (`sendOtp`, `verifyOtp`, `register`, `login`, `forgotPassword`, `socialLogin`) simulate success with fake delays. Any code can call `setActiveRole('administrator')` to bypass all security.

### Root Cause
The auth client was built per Sprint 21 Part 7 as a UX prototype but never replaced with a real Identity Provider integration. The file header comment explicitly states "MUST be replaced by the real Supabase Auth + RBAC integration."

### Risk
**CRITICAL (5×5)** — No real authentication means any user can access any route. The RequireAuth guard checks `activeRole !== 'guest'` but the role is trivially set via sessionStorage manipulation.

### Implementation Plan
1. Create `src/lib/supabase.ts` — Supabase client singleton using env vars
2. Create `src/features/auth/AuthService.ts` — Replaces `authClient.ts` with real Supabase Auth methods:
   - `signInWithOtp(phone/email)` → triggers Supabase magic link/OTP
   - `verifyOtp(phone/email, token)` → verifies and creates session
   - `signUp(email, password, metadata)` → email/password registration
   - `signOut()` → clears Supabase session
   - `getSession()` → returns current Supabase session with JWT
   - `onAuthStateChange()` → listener for session changes
   - `refreshSession()` → token refresh
3. Create `src/lib/httpClient.ts` — fetch wrapper that attaches Bearer JWT from Supabase session, handles 401 → refresh → retry
4. Create `src/lib/csrf.ts` — CSRF token generation and validation for state-changing requests
5. Update `App.tsx` — Replace mock `activeRole` with real auth state from `onAuthStateChange`; extract JWT claims into user metadata
6. Update `RequireAuth.tsx` — Verify JWT not expired before granting access
7. Update `context.ts` — Add `user`, `session`, `loading` to context
8. Rename `authClient.ts` → `authClient.mock.ts` (preserve for dev mode)
9. Create `src/features/auth/TokenManager.ts` — httpOnly cookie-based refresh token + in-memory access token

### Affected Modules
- `src/features/auth/authClient.ts` → `src/features/auth/AuthService.ts`
- `src/features/auth/RequireAuth.tsx`
- `src/features/auth/pages/LoginPage.tsx`
- `src/features/auth/pages/RegisterPage.tsx`
- `src/features/auth/pages/VerifyOtpPage.tsx`
- `src/App.tsx`
- `src/context.ts`
- `src/lib/supabase.ts` (NEW)
- `src/lib/httpClient.ts` (NEW)
- `src/lib/csrf.ts` (NEW)

### Testing Strategy
- Sign up flow end-to-end
- Sign in with OTP flow
- Session persistence across page reload
- Token refresh on expiry
- Logout clears session
- Unauthenticated access redirected to /login
- Invalid/expired JWT rejected

### Rollback Strategy
Revert `authClient.ts` to mock implementation; restore original `App.tsx` context logic.

### Estimated Risk: MEDIUM — Auth affects every page; careful regression testing required.

---

## P0-02: No Payment / Cart / Checkout Flow

### Problem
Cart, checkout, and payment gateway integration are entirely absent. The core e-commerce business model cannot function. No cart state, no checkout flow, no payment processing, no webhooks.

### Root Cause
Architecture was navigation/layout prototype with "No business functionality" per package.json.

### Risk
**CRITICAL (5×5)** — The platform's primary business function (buying products) is impossible. This is a P0 because without it the application cannot fulfill its core purpose.

### Implementation Plan
1. Create `src/features/cart/CartStore.ts` — Zustand-like cart state (add, remove, update, clear, persist to localStorage)
2. Create `src/features/cart/types.ts` — CartItem, CartState, CheckoutState types
3. Create `src/features/checkout/CheckoutService.ts` — Checkout flow:
   - `createCheckoutSession(cartItems, customerInfo)` → creates Stripe/Razorpay session
   - `handlePaymentSuccess(sessionId)` → processes successful payment
   - `handlePaymentFailure(error)` → handles failed payment
   - `handlePaymentCancellation()` → handles cancelled payment
4. Create `src/features/payment/PaymentGateway.ts` — Stripe integration:
   - `initStripe()` → loads Stripe.js
   - `createPaymentIntent(amount, currency, metadata)` → server call
   - `confirmPayment(paymentIntentId, paymentMethod)` → client confirmation
   - `handleWebhook(event)` → webhook signature verification
5. Create `src/features/cart/CartContext.tsx` — React context for cart state with CartProvider
6. Create `src/features/checkout/pages/CheckoutPage.tsx` — Checkout form (address, payment method, order review, place order)
7. Create `src/features/checkout/pages/CheckoutConfirmationPage.tsx` — Order confirmation with order ID
8. Create `src/features/checkout/pages/CheckoutErrorPage.tsx` — Error state with retry/cancel
9. Create `src/features/payment/webhook/verifyWebhook.ts` — Webhook signature verification utility
10. Create `src/features/cart/components/CartDrawer.tsx` — Slide-out cart drawer component
11. Add routes for `/cart`, `/checkout`, `/checkout/confirmation`, `/checkout/error` in App.tsx

### Affected Modules
- `src/features/cart/` (NEW — 5+ files)
- `src/features/checkout/` (NEW — 5+ files)
- `src/features/payment/` (NEW — 3+ files)
- `src/App.tsx` (add routes)
- `frontend/web-app/package.json` (add stripe dependency)

### Testing Strategy
- Add to cart → verify cart count updates
- Cart persists across page navigation
- Remove item updates totals
- Checkout flow: cart → address → payment → confirmation
- Empty cart → cannot proceed to checkout
- Payment success → redirect to confirmation
- Payment failure → error page with retry option
- Payment cancellation → return to cart

### Rollback Strategy
Revert cart/checkout routes from App.tsx; remove new files; restore package.json.

### Estimated Risk: HIGH — Payment integration involves real financial transactions; must handle edge cases.

---

## P0-03: No Content Security Policy / Security Headers

### Problem
No CSP, HSTS, X-Frame-Options, or X-Content-Type-Options headers configured. The app has zero defense against XSS at the network layer.

### Root Cause
Vite config was left at defaults with only `react()` plugin and server port/host.

### Risk
**CRITICAL (5×5)** — Without CSP, any XSS vulnerability can exfiltrate data and execute arbitrary scripts. Without HSTS, traffic can be intercepted via MITM.

### Implementation Plan
1. Update `vite.config.ts` — Add `headers` config with:
   - `Content-Security-Policy`: Restrict script-src to 'self', restrict style-src, restrict connect-src to API origins
   - `X-Content-Type-Options`: nosniff
   - `X-Frame-Options`: DENY
   - `Referrer-Policy`: strict-origin-when-cross-origin
   - `Permissions-Policy`: Restrict camera, microphone, geolocation
2. Create `infrastructure/nginx/default.conf` — Production nginx config with:
   - CSP headers
   - HSTS (`Strict-Transport-Security: max-age=31536000; includeSubDomains`)
   - HTTP → HTTPS redirect
   - Static asset caching rules
3. Create `infrastructure/nginx/security-headers.conf` — Reusable security headers snippet
4. Add `Strict-Transport-Security` header (preload ready)

### Affected Modules
- `frontend/web-app/vite.config.ts`
- `infrastructure/nginx/default.conf` (NEW)
- `infrastructure/nginx/security-headers.conf` (NEW)

### Testing Strategy
- Build app → verify CSP headers in dev server response
- Deploy with nginx → verify all security headers present
- Test that inline scripts are blocked by CSP
- Test that HTTPS redirect works
- Test HSTS header present

### Rollback Strategy
Revert `vite.config.ts` changes; remove nginx config files.

### Estimated Risk: LOW — Configuration-only; no runtime logic changes.

---

## P0-04: No Production Environment Configuration

### Problem
`.env.production.example` contains only `NODE_ENV=production` and `LOG_LEVEL=warn`. No API URLs, no DB config, no secrets, no service endpoints.

### Root Cause
Was never populated — prototype used only `.env` (dev defaults). No secrets management strategy.

### Risk
**CRITICAL (5×5)** — Cannot deploy to any environment without manual configuration.

### Implementation Plan
1. Create `.env.production` — All production API URLs, service endpoints, feature flags
2. Create `.env.staging` — Staging API URLs, service endpoints, feature flags
3. Create `.env.development` — Development API URLs (enhanced from current dev defaults)
4. Create `src/config/env.ts` — Runtime env validation utility that reads `import.meta.env` and throws on missing required vars
5. Create `src/config/env.d.ts` — TypeScript type augmentation for `import.meta.env`
6. Create `infrastructure/env-template.json` — Machine-readable env template for deployment CI
7. Create `.env.example` — Enhanced with all variables and documentation
8. Create `.env.production.example` — Complete production template with documentation

### Affected Modules
- `.env.production` (NEW)
- `.env.staging` (NEW)
- `.env.development` (NEW)
- `.env.example` (UPDATE)
- `.env.production.example` (UPDATE)
- `src/config/env.ts` (NEW)
- `src/config/env.d.ts` (NEW)
- `infrastructure/env-template.json` (NEW)

### Testing Strategy
- Build with empty required env → validation should fail with clear message
- Build with all env vars → build succeeds
- Verify env types are available in IDE autocomplete
- Verify production env works with docker-compose

### Rollback Strategy
Revert env files to originals; remove new config files.

### Estimated Risk: LOW — Config-only; no runtime logic changes if validation passes.

---

## P0-05: No Error Monitoring / APM

### Problem
No Sentry, DataDog, or any production error tracking. App has zero observability. Only `console.log` for logging.

### Root Cause
Error monitoring deferred (not in prototype scope).

### Risk
**CRITICAL (5×5)** — Production outages invisible; no way to debug customer issues in production.

### Implementation Plan
1. Add `@sentry/react` and `@sentry/tracing` to package.json
2. Create `src/lib/sentry.ts` — Sentry initialization:
   - `Sentry.init()` with DSN from env
   - Environment tag (dev/staging/prod)
   - Release version from build
   - Traces sample rate configurable via env
   - Replays for session debugging
3. Create `src/lib/logger.ts` — Structured logging utility:
   - `logger.info(msg, data?)`
   - `logger.warn(msg, data?)`
   - `logger.error(msg, error?, data?)`
   - `logger.debug(msg, data?)`
   - Sends errors to Sentry automatically
   - Adds request correlation IDs
   - Adds timestamp and environment to every log
4. Update `src/main.tsx` — Add Sentry initialization before app mount
5. Update `ErrorBoundary.tsx` — Report errors to Sentry via `Sentry.captureException`
6. Create `src/lib/correlationId.ts` — Request correlation ID generation and propagation

### Affected Modules
- `frontend/web-app/package.json` (add @sentry/react, @sentry/tracing)
- `src/lib/sentry.ts` (NEW)
- `src/lib/logger.ts` (NEW)
- `src/lib/correlationId.ts` (NEW)
- `src/main.tsx` (UPDATE — add Sentry.init)
- `src/components/ErrorBoundary.tsx` (UPDATE — add Sentry reporting)
- `.env.production` (add SENTRY_DSN)

### Testing Strategy
- App loads without Sentry DSN → graceful fallback (no crash)
- App loads with Sentry DSN → Sentry initialized
- Throw error in component → captured by Sentry
- `logger.error()` → captured by Sentry
- ErrorBoundary catches error → reports to Sentry

### Rollback Strategy
Remove Sentry deps from package.json; revert main.tsx; remove new files.

### Estimated Risk: LOW — Library integration; no business logic impact.

---

## P0-06: No SEO Infrastructure

### Problem
No `robots.txt`, `sitemap.xml`, `manifest.json`, no meta description/OG tags, no JSON-LD structured data.

### Root Cause
SEO was never part of navigation prototype scope.

### Risk
**HIGH** — Zero organic search discovery; search engines cannot index the application.

### Implementation Plan
1. Create `public/robots.txt` — Allow all crawlers, point to sitemap
2. Create `public/sitemap.xml` — List all public routes with lastmod dates
3. Create `public/manifest.json` — PWA manifest with name, short_name, icons, theme_color, start_url
4. Update `index.html`:
   - Add meta description
   - Add meta keywords
   - Add OG tags (title, description, url, image, type)
   - Add Twitter Card tags
   - Add canonical URL
   - Add JSON-LD structured data (Organization, WebApplication)
   - Add favicon/apple-touch-icon references
5. Create `public/favicon.ico` — (placeholder SVG favicon)
6. Create `public/apple-touch-icon.png` — (placeholder reference)
7. Create `src/lib/seo.ts` — SEO utility for dynamic meta tag updates per page

### Affected Modules
- `public/robots.txt` (NEW)
- `public/sitemap.xml` (NEW)
- `public/manifest.json` (NEW)
- `public/favicon.ico` / `public/apple-touch-icon.png` (NEW)
- `frontend/web-app/index.html` (UPDATE)
- `src/lib/seo.ts` (NEW)

### Testing Strategy
- `robots.txt` accessible at `/robots.txt`
- `sitemap.xml` accessible at `/sitemap.xml`
- `manifest.json` valid and references correct icons
- OG tags present on homepage
- JSON-LD valid JSON
- Lighthouse SEO audit passes

### Rollback Strategy
Revert index.html; remove new public/ files.

### Estimated Risk: LOW — Static files and HTML changes; no runtime logic.

---

## P1 Blockers (Also Eliminated in This Sprint)

### P1-01: Session in sessionStorage → httpOnly Cookies
Implementation: Replace `sessionStorage.setItem('sk_session_role')` with Supabase session which uses httpOnly cookies. Update App.tsx to derive auth state from Supabase session listener.

### P1-02: No HTTPS/HSTS
Implementation: Add HSTS to nginx config and Vite headers (part of P0-03).

### P1-05/P1-06: "Navigation Prototype" Branding
Implementation: Update `index.html` title, footer text, package.json description.

### P1-07: No Health Check Endpoint
Implementation: Create `/health` route that returns JSON status including version, uptime, db status.

### P1-04: Docker No App Service
Implementation: Add web-app service to docker-compose.yml.

---

## Implementation Order

### Wave 1 — Quick Wins (No Logic Risk)
1. P0-03: Security headers (vite.config.ts + nginx config)
2. P0-04: Production environment configuration
3. P0-06: SEO infrastructure
4. P1-05/P1-06: Production branding
5. P1-07: Health check endpoint
6. P1-04: Docker production service

### Wave 2 — Medium Risk
7. P0-05: Error monitoring (Sentry)
8. P1-01: Session security (tied to auth)
9. P1-02: HTTPS/HSTS (tied to headers)

### Wave 3 — Higher Risk
10. P0-01: Real authentication provider
11. P0-02: Payment integration

---

## Success Criteria

- [ ] Every P0 blocker resolved with verified implementation
- [ ] Build PASS
- [ ] TypeScript 0 errors
- [ ] No new warnings
- [ ] No broken auth flows
- [ ] No broken customer journeys
- [ ] Security headers verifiable via curl
- [ ] Sentry reporting verified
- [ ] Health endpoint returns valid JSON
- [ ] robots.txt, sitemap.xml, manifest.json accessible
- [ ] Environment configuration validates on build
- [ ] Docker compose starts all services
- [ ] No regression in existing functionality
- [ ] Repository clean
