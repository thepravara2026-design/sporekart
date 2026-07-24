# DNS Configuration for sporekart.com
# Route53 hosted zone and records

resource "aws_route53_zone" "sporekart" {
  name = "sporekart.com"

  tags = {
    Name        = "sporekart-prod-dns"
    Environment = var.environment
    ManagedBy   = "terraform"
  }
}

# Main site — CloudFront alias
resource "aws_route53_record" "sporekart_root" {
  zone_id = aws_route53_zone.sporekart.zone_id
  name    = "sporekart.com"
  type    = "A"

  alias {
    name                   = aws_cloudfront_distribution.sporekart.domain_name
    zone_id                = aws_cloudfront_distribution.sporekart.hosted_zone_id
    evaluate_target_health = false
  }
}

# www subdomain — CloudFront alias
resource "aws_route53_record" "sporekart_www" {
  zone_id = aws_route53_zone.sporekart.zone_id
  name    = "www.sporekart.com"
  type    = "CNAME"
  ttl     = 300
  records = ["sporekart.com"]
}

# API subdomain — ALB alias
resource "aws_route53_record" "sporekart_api" {
  zone_id = aws_route53_zone.sporekart.zone_id
  name    = "api.sporekart.com"
  type    = "A"

  alias {
    name                   = aws_lb.web_app.dns_name
    zone_id                = aws_lb.web_app.zone_id
    evaluate_target_health = true
  }
}

# Identity subdomain — ALB alias
resource "aws_route53_record" "sporekart_identity" {
  zone_id = aws_route53_zone.sporekart.zone_id
  name    = "identity.sporekart.com"
  type    = "A"

  alias {
    name                   = aws_lb.web_app.dns_name
    zone_id                = aws_lb.web_app.zone_id
    evaluate_target_health = true
  }
}

# Health check subdomain
resource "aws_route53_record" "sporekart_health_check" {
  zone_id = aws_route53_zone.sporekart.zone_id
  name    = "health.sporekart.com"
  type    = "A"

  alias {
    name                   = aws_lb.web_app.dns_name
    zone_id                = aws_lb.web_app.zone_id
    evaluate_target_health = true
  }
}

# Staging subdomain
resource "aws_route53_record" "sporekart_staging" {
  zone_id = aws_route53_zone.sporekart.zone_id
  name    = "staging.sporekart.com"
  type    = "A"

  alias {
    name                   = aws_lb.web_app_staging.dns_name
    zone_id                = aws_lb.web_app_staging.zone_id
    evaluate_target_health = true
  }
}
