# RC1 Certification — Risk Register

**Date:** 2026-07-20
**Authority:** Enterprise Release Governance Board

---

## Risk Table

| ID | Risk | Likelihood | Impact | Level | Status |
|----|------|-----------|--------|-------|--------|
| RR-01 | No real auth provider — mock only; anyone can authenticate | 5 | 5 | **CRITICAL** | 🔴 OPEN |
| RR-02 | No payment/cart/checkout — e-commerce business model impossible | 5 | 5 | **CRITICAL** | 🔴 OPEN |
| RR-03 | No CSP headers — full XSS vulnerability surface | 5 | 5 | **CRITICAL** | 🔴 OPEN |
| RR-04 | No production env config — cannot deploy to production | 5 | 5 | **CRITICAL** | 🔴 OPEN |
| RR-05 | No error monitoring — production outages invisible | 5 | 4 | **CRITICAL** | 🔴 OPEN |
| RR-06 | No robots.txt/sitemap — zero organic search discovery | 5 | 3 | **HIGH** | 🟠 OPEN |
| RR-07 | sessionStorage for auth — XSS-vulnerable token storage | 4 | 5 | **HIGH** | 🟠 OPEN |
| RR-08 | No HTTPS/HSTS — traffic in clear text | 4 | 5 | **HIGH** | 🟠 OPEN |
| RR-09 | No health endpoint — k8s/orchestration cannot manage app | 5 | 4 | **HIGH** | 🟠 OPEN |
| RR-10 | Docker no app service — no production deployment path | 5 | 4 | **HIGH** | 🟠 OPEN |
| RR-11 | "Navigation Prototype" branding in production title/footer | 5 | 3 | **HIGH** | 🟠 OPEN |
| RR-12 | No rate limiting on auth — brute force/DoS vector | 4 | 4 | **HIGH** | 🟠 OPEN |
| RR-13 | Build CI only tests one service — frontend not tested | 3 | 3 | **MEDIUM** | 🟡 OPEN |
| RR-14 | Mock API keys in .env.mock — deployment confusion risk | 3 | 3 | **MEDIUM** | 🟡 OPEN |
| RR-15 | TypeScript strict mode disabled — latent type errors | 3 | 2 | **MEDIUM** | 🟡 OPEN |
| RR-16 | No product detail pages (/products/:id) — feature gap | 3 | 2 | **MEDIUM** | 🟡 OPEN |
| RR-17 | Flaky test rate 4% — CI reliability undermined | 3 | 2 | **MEDIUM** | 🟡 OPEN |
| RR-18 | No DB migration tooling — schema drift risk | 2 | 3 | **LOW** | 🟢 OPEN |

## Risk Summary

| Level | Count |
|-------|-------|
| CRITICAL | 6 |
| HIGH | 6 |
| MEDIUM | 5 |
| LOW | 1 |
| **Total Open** | **18** |

## Risk Trend

Previous register (Approval Gate D): 0 critical, 3 high (all process/CI).
Current register: **6 critical, 6 high** — this increase is because the Board performed a deeper independent audit of production readiness rather than relying on prior regression-only analysis.

## Mitigation Recommendations

1. **Auth provider integration** (RR-01): 1-2 weeks — Supabase Auth recommended
2. **Payment flow** (RR-02): 2-3 weeks — Stripe/Razorpay integration
3. **Security headers** (RR-03, RR-08): 2-3 days — nginx/CDN config
4. **Production env config** (RR-04): 1 day — Vault/DOppler/AWS Secrets Manager
5. **Error monitoring** (RR-05): 2-3 days — Sentry integration
6. **SEO infrastructure** (RR-06): 1-2 days — Generate robots/sitemap/manifest
7. **Session security** (RR-07): depends on auth provider choice
8. **Health endpoint** (RR-09): 0.5 day
9. **Docker config** (RR-10): 1 day
10. **Branding** (RR-11): 0.5 day
