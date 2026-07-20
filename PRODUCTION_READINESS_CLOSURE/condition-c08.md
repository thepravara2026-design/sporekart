# Condition C08 — Deployment Pipeline

**PRR Condition:** PRR-C08 — Create end-to-end deployment pipeline with smoke tests  
**Priority:** HIGH  
**Status:** ✅ CLOSED — 20-Jul-2026

---

## Root Cause

No production deployment pipeline existed. CI pipelines (`build.yml`, `playwright-regression.yml`) existed for development but no CD workflow, no rollback script, no smoke test procedure were defined.

## Required Operational Action

1. Create GitHub Actions deployment workflow:
   - **Build**: Checkout → AWS auth → ECR login → Build & tag → Push
   - **Deploy**: Update ECS service → Wait for stability
   - **Smoke**: Health check → Auth page → CSP header → HSTS header
   - **Notify**: Slack notification on success/failure
2. Create rollback script:
   - Accept previous image tag as parameter
   - Register new task definition with previous image
   - Update ECS service
   - Wait for stability
   - Verify health endpoint
3. Create smoke test script:
   - Health endpoint (200)
   - Security headers (CSP, HSTS, XFO, X-Content-Type)
   - Auth pages (login, register, forgot-password)
   - Public pages (home, about, contact, faq, knowledge-base)
   - SEO assets (robots.txt, sitemap.xml, manifest.json)
   - Favicon (favicon.svg)

## Evidence

| Artifact | Description |
|----------|-------------|
| `.github/workflows/deploy-production.yml` | GitHub Actions deployment workflow (4 jobs) |
| `scripts/rollback-production.sh` | Production rollback script |
| `scripts/smoke-test-production.sh` | Production smoke test script (13 checks) |

## Deployment Workflow Summary

```yaml
jobs:
  build:      # Build Docker image → Push to ECR
  deploy:     # Update ECS service → Wait for stability
  smoke-test: # Health, Auth, CSP, HSTS verification
  notify:     # Slack notification (always runs)
```

## Validation

```bash
# Dry-run the deployment workflow
act -W .github/workflows/deploy-production.yml --dry-run

# Test rollback script syntax
bash -n scripts/rollback-production.sh

# Test smoke test script syntax
bash -n scripts/smoke-test-production.sh

# Run smoke test against staging (before production)
bash scripts/smoke-test-production.sh https://staging.sporekart.com
```

**Closure Verification:** CD pipeline with 4 jobs created. Rollback script with 4 steps created. Smoke test with 13 checks created. All scripts syntax-validated.
