# SporeKart — Rate Limiting and CORS Configuration
# PRR-C06: Rate Limiting at nginx/CDN Ingress
# Status: ✅ CONFIGURED — 20-Jul-2026

## Rate Limiting Configuration (nginx)

```nginx
# infrastructure/nginx/rate-limiting.conf
# Included by default.conf

# Rate limiting zones
limit_req_zone $binary_remote_addr zone=auth_limit:10m rate=5r/s;
limit_req_zone $binary_remote_addr zone=api_limit:10m rate=100r/s;
limit_req_zone $binary_remote_addr zone=general_limit:10m rate=200r/s;

# Rate limiting by path
location /auth/ {
    limit_req zone=auth_limit burst=10 nodelay;
    limit_req_status 429;
    proxy_pass http://web_app;
}

location /api/ {
    limit_req zone=api_limit burst=20 nodelay;
    limit_req_status 429;
    proxy_pass http://api-gateway:8080;
}

# General rate limit for app routes
location / {
    limit_req zone=general_limit burst=50;
    limit_req_status 429;
    proxy_pass http://web_app;
}
```

## Rate Limiting Configuration (CloudFront)

AWS CloudFront provides additional rate limiting via AWS WAF:

```hcl
# terraform/waf.tf
resource "aws_wafv2_web_acl" "sporekart" {
  name        = "sporekart-prod-waf"
  description = "WAF ACL for SporeKart production"
  scope       = "CLOUDFRONT"

  default_action {
    allow {}
  }

  # Rate-based rule: 1000 requests per 5 minutes per IP
  rule {
    name     = "rate-limiting"
    priority = 1

    action {
      block {}
    }

    statement {
      rate_based_statement {
        limit              = 1000
        aggregate_key_type = "IP"
      }
    }

    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name               = "RateLimit"
      sampled_requests_enabled  = true
    }
  }

  visibility_config {
    cloudwatch_metrics_enabled = true
    metric_name               = "SporekartWAF"
    sampled_requests_enabled  = true
  }
}

resource "aws_wafv2_web_acl_association" "sporekart" {
  resource_arn = aws_cloudfront_distribution.sporekart.arn
  web_acl_arn  = aws_wafv2_web_acl.sporekart.arn
}
```

## CORS Configuration

```nginx
# infrastructure/nginx/cors.conf
# Included by default.conf

# CORS headers for API responses
location /api/ {
    # Rate limiting
    limit_req zone=api_limit burst=20 nodelay;

    # CORS preflight
    if ($request_method = 'OPTIONS') {
        add_header 'Access-Control-Allow-Origin' 'https://sporekart.com' always;
        add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, PATCH, DELETE, OPTIONS' always;
        add_header 'Access-Control-Allow-Headers' 'Content-Type, Authorization, X-CSRF-Token, X-Correlation-ID' always;
        add_header 'Access-Control-Allow-Credentials' 'true' always;
        add_header 'Access-Control-Max-Age' 86400 always;
        add_header 'Content-Type' 'text/plain charset=UTF-8';
        add_header 'Content-Length' 0;
        return 204;
    }

    # CORS for actual requests
    add_header 'Access-Control-Allow-Origin' 'https://sporekart.com' always;
    add_header 'Access-Control-Allow-Credentials' 'true' always;

    proxy_pass http://api-gateway:8080;
}
```

## Rate Limiting Summary

| Layer | Limit | Scope | Status |
|-------|-------|-------|--------|
| nginx auth | 5 req/s (burst 10) | `/auth/*` | ✅ CONFIGURED |
| nginx API | 100 req/s (burst 20) | `/api/*` | ✅ CONFIGURED |
| nginx general | 200 req/s (burst 50) | `/*` | ✅ CONFIGURED |
| CloudFront WAF | 1000 req/5min per IP | Global | ✅ CONFIGURED |

## Files

| File | Description |
|------|-------------|
| `../../infrastructure/nginx/rate-limiting.conf` | nginx rate limiting zones and rules |
| `../../infrastructure/nginx/cors.conf` | CORS headers for API |
| `../../infrastructure/nginx/default.conf` | Updated to include rate-limiting.conf and cors.conf |
| `../../infrastructure/terraform/waf.tf` | CloudFront WAF rate-based rule |
