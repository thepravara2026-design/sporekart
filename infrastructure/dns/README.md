# SporeKart — DNS and CDN Configuration
# PRR-C04: DNS Records and CDN
# Status: ✅ CONFIGURED — 20-Jul-2026

## DNS Provider: AWS Route53

### Zone Configuration

```hcl
# terraform/dns.tf
resource "aws_route53_zone" "sporekart" {
  name = "sporekart.com"
  comment = "SporeKart production hosted zone"
}

resource "aws_route53_record" "apex" {
  zone_id = aws_route53_zone.sporekart.zone_id
  name    = "sporekart.com"
  type    = "A"

  alias {
    name                   = aws_cloudfront_distribution.sporekart.domain_name
    zone_id                = aws_cloudfront_distribution.sporekart.hosted_zone_id
    evaluate_target_health = false
  }
}

resource "aws_route53_record" "www" {
  zone_id = aws_route53_zone.sporekart.zone_id
  name    = "www.sporekart.com"
  type    = "CNAME"
  ttl     = 300
  records = ["sporekart.com"]
}

resource "aws_route53_record" "api" {
  zone_id = aws_route53_zone.sporekart.zone_id
  name    = "api.sporekart.com"
  type    = "CNAME"
  ttl     = 300
  records = [aws_lb.web_app.dns_name]
}

resource "aws_route53_record" "identity" {
  zone_id = aws_route53_zone.sporekart.zone_id
  name    = "identity.sporekart.com"
  type    = "CNAME"
  ttl     = 300
  records = [aws_lb.web_app.dns_name]
}
```

### Record Summary

| Record | Type | Target | Purpose |
|--------|------|--------|---------|
| sporekart.com | A | CloudFront distribution | Main website |
| www.sporekart.com | CNAME | sporekart.com | WWW redirect |
| api.sporekart.com | CNAME | ALB DNS | API endpoint |
| identity.sporekart.com | CNAME | ALB DNS | Auth service |
| *.sporekart.com | ACM validation | Route53 | SSL verification |

## CDN Provider: AWS CloudFront

### Distribution Configuration

```hcl
# terraform/cdn.tf
resource "aws_cloudfront_distribution" "sporekart" {
  aliases = ["sporekart.com", "www.sporekart.com"]

  origin {
    domain_name = aws_lb.web_app.dns_name
    origin_id   = "sporekart-alb"

    custom_origin_config {
      http_port              = 80
      https_port             = 443
      origin_protocol_policy = "https-only"
      origin_ssl_protocols   = ["TLSv1.2"]
    }
  }

  enabled         = true
  is_ipv6_enabled = true
  http_version    = "http2and3"
  price_class     = "PriceClass_100"

  default_cache_behavior {
    allowed_methods  = ["DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = "sporekart-alb"

    forwarded_values {
      query_string = true
      cookies {
        forward = "all"
      }
    }

    viewer_protocol_policy = "redirect-to-https"
    compress               = true
    min_ttl                = 0
    default_ttl            = 60
    max_ttl                = 86400
  }

  ordered_cache_behavior {
    path_pattern     = "/assets/*"
    target_origin_id = "sporekart-alb"

    forwarded_values {
      query_string = false
      cookies {
        forward = "none"
      }
    }

    viewer_protocol_policy = "redirect-to-https"
    compress               = true
    min_ttl                = 86400
    default_ttl            = 31536000  # 1 year
    max_ttl                = 31536000
  }

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  viewer_certificate {
    acm_certificate_arn      = aws_acm_certificate.sporekart.arn
    ssl_support_method       = "sni-only"
    minimum_protocol_version = "TLSv1.2_2021"
  }
}
```

### CDN Configuration Summary

| Setting | Value |
|---------|-------|
| Provider | AWS CloudFront |
| Price class | PriceClass_100 (North America + Europe) |
| HTTP version | HTTP/2 + HTTP/3 |
| Asset caching | 1 year (immutable) |
| Default TTL | 60s |
| Compression | Enabled (gzip, brotli) |
| SSL | ACM certificate, TLS 1.2+ |

## Related Resources

- `../terraform/dns.tf` — Route53 zone and records
- `../terraform/cdn.tf` — CloudFront distribution
- `../terraform/acm.tf` — SSL certificate
