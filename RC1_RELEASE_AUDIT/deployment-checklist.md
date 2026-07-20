# Deployment Checklist — SporeKart v1.0 RC1

**Note:** This deployment checklist is prepared as part of the RC1 release certification. However, since the RC1 decision is **NO-GO**, this checklist documents what WOULD be required for production deployment and should be completed before re-certification.

---

## Pre-Deployment

### Environment Configuration
- [ ] `NODE_ENV=production` verified
- [ ] All API endpoint URLs configured (not mock)
- [ ] Real authentication provider credentials configured
- [ ] Payment gateway keys configured (live, not test)
- [ ] Database connection strings configured
- [ ] Redis connection configured
- [ ] Kafka connection configured
- [ ] Secrets stored in vault (not in .env files)
- [ ] Feature flags set for production

### Build & Artifacts
- [ ] Production build completed (`npm run build`)
- [ ] TypeScript clean (0 errors)
- [ ] Bundle size within budget
- [ ] Source maps not deployed (or protected)
- [ ] Static assets fingerprinted (Vite handles this)
- [ ] Docker image built and tagged
- [ ] Docker image pushed to registry

### Security
- [ ] CSP headers configured
- [ ] HSTS header configured
- [ ] X-Frame-Options: DENY configured
- [ ] X-Content-Type-Options: nosniff configured
- [ ] Referrer-Policy configured
- [ ] Permissions-Policy configured
- [ ] HTTPS enforced (HTTP → HTTPS redirect)
- [ ] Cookies set to httpOnly, Secure, SameSite
- [ ] Rate limiting configured on auth endpoints
- [ ] CORS configured (allowed origins whitelist)
- [ ] API keys rotated from mock/test values
- [ ] `.env.mock` excluded from deployment

### Infrastructure
- [ ] Health check endpoint (`/health`) returns 200
- [ ] Readiness probe configured (k8s/Docker)
- [ ] Liveness probe configured (k8s/Docker)
- [ ] Resource limits configured (CPU/memory)
- [ ] Auto-scaling configured (min/max replicas)
- [ ] Load balancer configured
- [ ] CDN configured for static assets
- [ ] Database provisioned and migrated
- [ ] Redis provisioned and configured
- [ ] Kafka provisioned and configured

### Monitoring & Observability
- [ ] Sentry/APM configured and verified
- [ ] Error alerts configured
- [ ] Performance alerts configured
- [ ] Uptime monitoring configured
- [ ] Log aggregation configured
- [ ] Dashboard (Grafana/Datadog) configured

### SEO & Public
- [ ] `robots.txt` deployed and correct
- [ ] `sitemap.xml` generated and submitted
- [ ] `manifest.json` deployed
- [ ] Meta tags on all pages (title, description, OG)
- [ ] Canonical URLs configured
- [ ] favicon.ico deployed
- [ ] apple-touch-icon deployed
- [ ] 404 page returns proper HTTP status
- [ ] SSL certificate valid and auto-renewing

### Data
- [ ] Database backup strategy configured
- [ ] Backup schedule verified
- [ ] Point-in-time recovery tested
- [ ] Data retention policies configured
- [ ] GDPR/CCPA compliance measures in place

---

## During Deployment

### Rollout
- [ ] Canary deployment (10% traffic) → verify
- [ ] 50% traffic → verify
- [ ] 100% traffic
- [ ] Smoke tests pass after deployment
- [ ] Monitor error rates (should not increase)
- [ ] Monitor response times (should not increase)

### Verification
- [ ] Login flow works end-to-end
- [ ] Registration flow works end-to-end
- [ ] Payment flow works end-to-end
- [ ] Admin console accessible
- [ ] Search returns results
- [ ] All critical customer journeys pass
- [ ] API health endpoints return OK

---

## Post-Deployment

- [ ] Monitor logs for errors (first 24 hours)
- [ ] Monitor performance metrics
- [ ] Verify analytics data collection
- [ ] Run accessibility scan
- [ ] Run Lighthouse audit
- [ ] Verify CDN caching behavior
- [ ] Check SSL certificate
- [ ] Verify backup is running
- [ ] Send deployment notification to stakeholders
- [ ] Update status page

---

*This checklist should be completed and signed off before any production deployment. Current status: ❌ Cannot deploy (RC1 rejected).*
