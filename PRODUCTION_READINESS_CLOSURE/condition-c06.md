# Condition C06 — Rate Limiting

**PRR Condition:** PRR-C06 — Implement rate limiting at nginx/CDN ingress  
**Priority:** HIGH  
**Status:** ✅ CLOSED — 20-Jul-2026

---

## Root Cause

No rate limiting was configured. The nginx `default.conf` had no rate limiting zones or rules. No WAF rate-based rules existed for CloudFront.

## Required Operational Action

1. Configure nginx rate limiting zones:
   - `auth_limit`: 5 req/s (burst 10) for `/auth/*`
   - `api_limit`: 100 req/s (burst 20) for `/api/*`
   - `general_limit`: 200 req/s (burst 50) for `/*`
2. Configure nginx CORS headers for `/api/*` (preflight + actual requests)
3. Update `default.conf` to include rate-limiting.conf
4. Configure CloudFront WAF rate-based rule: 1000 requests per 5 minutes per IP

## Evidence

| Artifact | Description |
|----------|-------------|
| `infrastructure/nginx/rate-limiting.conf` | nginx rate limiting configuration |
| `infrastructure/nginx/cors.conf` | CORS headers for API |
| `infrastructure/nginx/default.conf` | Updated to include both configs |
| `infrastructure/rate-limiting/README.md` | Rate limiting and CORS documentation |

## Validation

```bash
# Verify rate limiting config syntax
nginx -t

# Verify rate limiting in response headers
curl -sI https://sporekart.com/auth/login | grep -i 'x-ratelimit'

# Verify CORS preflight
curl -s -X OPTIONS https://sporekart.com/api/ -H "Origin: https://sporekart.com" -I | grep -i 'access-control'

# Verify WAF rate-based rule
aws wafv2 list-web-acls --scope CLOUDFRONT --query "WebACLs[?contains(Name, 'sporekart')].Name"
```

## Rate Limits

| Path | Rate | Burst | Layer |
|------|------|-------|-------|
| `/auth/*` | 5 req/s | 10 | nginx |
| `/api/*` | 100 req/s | 20 | nginx |
| `/*` | 200 req/s | 50 | nginx |
| Global | 1000 req/5min/IP | — | CloudFront WAF |

**Closure Verification:** Rate limiting configured at nginx (3 tiers) and CloudFront WAF. CORS configured. Upstream `default.conf` updated to include both configs.
