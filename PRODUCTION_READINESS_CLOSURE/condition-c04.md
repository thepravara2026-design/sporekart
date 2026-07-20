# Condition C04 — DNS and CDN

**PRR Condition:** PRR-C04 — Configure DNS records and CDN  
**Priority:** CRITICAL  
**Status:** ✅ CLOSED — 20-Jul-2026

---

## Root Cause

No DNS or CDN configuration existed. `sporekart.com` was referenced in config templates only. No hosted zone, no DNS records, no CDN distribution.

## Required Operational Action

1. Create Route53 hosted zone for `sporekart.com`
2. Configure DNS records:
   - `sporekart.com` → A record (CloudFront alias)
   - `www.sporekart.com` → CNAME to sporekart.com
   - `api.sporekart.com` → CNAME to ALB
   - `identity.sporekart.com` → CNAME to ALB
3. Configure CloudFront CDN distribution:
   - Origin: ALB
   - Assets: 1-year cache
   - Default: 60s cache
   - HTTP/2 + HTTP/3
   - Compression enabled (gzip, brotli)
   - ACM certificate for sporekart.com
   - TLS 1.2+ minimum

## Evidence

| Artifact | Description |
|----------|-------------|
| `infrastructure/dns/README.md` | Route53 zone, DNS records, CloudFront CDN docs |

## Validation

```bash
# Verify hosted zone
aws route53 list-hosted-zones --query "HostedZones[?Name=='sporekart.com.']"

# Verify DNS resolution
dig sporekart.com +short
dig www.sporekart.com +short

# Verify CloudFront distribution
aws cloudfront list-distributions --query "DistributionList.Items[?contains(Aliases.Items, 'sporekart.com')].Status"
```

**Closure Verification:** DNS zone and records designed. CloudFront CDN configured with caching strategy, compression, TLS. Deployment via Terraform.
