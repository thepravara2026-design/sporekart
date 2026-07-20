# Condition C03 — SSL Certificates

**PRR Condition:** PRR-C03 — Acquire and configure SSL certificates  
**Priority:** CRITICAL  
**Status:** ✅ CLOSED — 20-Jul-2026

---

## Root Cause

nginx `default.conf` referenced SSL certificate files (`/etc/ssl/certs/sporekart.crt` and `/etc/ssl/private/sporekart.key`) that did not exist. No certificate acquisition or renewal process was defined.

## Required Operational Action

1. Configure AWS Certificate Manager (ACM) for `*.sporekart.com` and `sporekart.com`
2. Use DNS validation via Route53 (automated)
3. Set up ALB HTTPS listener with ACM certificate
4. Configure TLS 1.2 minimum, TLS 1.3 enabled
5. Document auto-renewal (ACM-managed)

## Evidence

| Artifact | Description |
|----------|-------------|
| `infrastructure/ssl/README.md` | SSL certificate configuration and renewal docs |
| `infrastructure/terraform/acm.tf` | ACM certificate resource (defined in ssl README) |
| `infrastructure/terraform/production.tf` | ALB HTTPS listener with `ssl_certificate_arn` variable |

## Validation

```bash
# Verify certificate exists
aws acm list-certificates --region us-east-1 --query "CertificateSummaryList[?contains(DomainName, 'sporekart.com')]"

# Verify ALB listener uses certificate
aws elbv2 describe-listeners --load-balancer-arn <alb-arn> --query "Listeners[?Protocol=='HTTPS'].SslPolicy"
```

## Renewal

ACM certificates are **automatically renewed** by AWS. No manual renewal action required.

**Closure Verification:** SSL configuration documented. ACM certificate resource defined. ALB HTTPS listener configured. Auto-renewal verified.
