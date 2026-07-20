# Production Readiness Review — Risk Register

**Reviewer:** Principal Technical Program Manager

---

## Risk Table

| ID | Risk | Likelihood | Impact | Level | Status | Mitigation |
|----|------|-----------|--------|-------|--------|------------|
| PRR-R01 | No cloud infrastructure provisioned — no compute, storage, or networking | 5 | 5 | **CRITICAL** | 🔴 OPEN | Complete terraform/cloud formation templates before deployment |
| PRR-R02 | No production database — schema exists but no DB provisioned | 5 | 5 | **CRITICAL** | 🔴 OPEN | Provision Supabase managed Postgres with production plan |
| PRR-R03 | No SSL certificates — HTTPS config references certs that don't exist | 5 | 5 | **CRITICAL** | 🔴 OPEN | Acquire SSL certs via certbot/cert-manager or CDN |
| PRR-R04 | No DNS configured — sporekart.com domains not routed | 5 | 5 | **CRITICAL** | 🔴 OPEN | Configure DNS provider with A/AAAA/CNAME records |
| PRR-R05 | Secrets in plaintext .env file — no secrets manager | 4 | 5 | **HIGH** | 🔴 OPEN | Implement Vault, Doppler, or AWS Secrets Manager |
| PRR-R06 | No rate limiting on auth — brute force/DoS vector | 4 | 4 | **HIGH** | 🔴 OPEN | Configure rate limiting at nginx or CDN layer |
| PRR-R07 | No CD/deployment pipeline — no automated production deployment | 4 | 4 | **HIGH** | 🔴 OPEN | Create GitHub Actions CD workflow |
| PRR-R08 | No backup/restore procedures — data loss risk | 4 | 5 | **HIGH** | 🔴 OPEN | Document and implement DB backup with RTO/RPO targets |
| PRR-R09 | No incident response plan — uncoordinated outage response | 3 | 5 | **HIGH** | 🔴 OPEN | Create incident response runbook with escalation matrix |
| PRR-R10 | No CDN configured — single-region latency for global users | 3 | 3 | **MEDIUM** | 🟠 OPEN | Configure CloudFront/CloudFlare CDN |
| PRR-R11 | No log aggregation — inability to search production logs | 3 | 3 | **MEDIUM** | 🟠 OPEN | Deploy ELK/Loki stack or use cloud logging |
| PRR-R12 | Missing SEO/PWA assets — 404 on robots, sitemap, manifest | 3 | 2 | **MEDIUM** | 🟠 OPEN | Create assets in `frontend/web-app/public/` |
| PRR-R13 | No database migration tooling — schema drift risk | 3 | 3 | **MEDIUM** | 🟠 OPEN | Configure Flyway/Prisma Migrate |
| PRR-R14 | No CORS configuration — API calls may fail | 3 | 3 | **MEDIUM** | 🟠 OPEN | Configure CORS in nginx or API gateway |
| PRR-R15 | Mock keys in .env.mock — deployment confusion risk | 2 | 2 | **LOW** | 🟢 OPEN | Add .env.mock to deploy-time exclusion list |
| PRR-R16 | Version inconsistency (package.json 0.1.0) — configuration drift | 2 | 1 | **LOW** | 🟢 OPEN | Update package.json to consistent production version |

## Risk Summary

| Level | Count | Risk Trend |
|-------|-------|------------|
| CRITICAL | 4 | 🔴 New — operational infrastructure gaps |
| HIGH | 5 | 🔴 New — deployment pipeline, secrets, rate limiting, backup, incident response |
| MEDIUM | 5 | 🟠 New — CDN, log aggregation, SEO, migrations, CORS |
| LOW | 2 | 🟢 Minor — mock keys, version string |
| **Total** | **16** | |

## Production-Blocking Classification

| Classification | Count | Risks |
|---------------|-------|-------|
| **Production Blocking** | **9** | PRR-R01 through PRR-R09 — must be closed before production deployment |
| Production Non-Blocking | 5 | PRR-R10 through PRR-R14 — can be addressed post-launch |
| Deferred | 2 | PRR-R15 through PRR-R16 — low priority |

---

**Risk Verdict: 9 production-blocking operational risks identified. Zero application defects. All risks are operational infrastructure gaps consistent with Phase 0 placeholder status.**
