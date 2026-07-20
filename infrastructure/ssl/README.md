# SporeKart — SSL/TLS Certificate Configuration
# PRR-C03: SSL Certificate Acquisition and Renewal
# Operator: AWS Certificate Manager (ACM)
# Status: ✅ CONFIGURED — 20-Jul-2026

## Certificate Details

| Field | Value |
|-------|-------|
| Domain | `*.sporekart.com`, `sporekart.com` |
| Provider | AWS Certificate Manager (ACM) |
| Validation | DNS validation via Route53 |
| Renewal | Automatic (ACM-managed) |
| Algorithm | RSA 2048 |

## ACM Configuration

```hcl
# terraform/acm.tf
resource "aws_acm_certificate" "sporekart" {
  domain_name       = "sporekart.com"
  subject_alternative_names = ["*.sporekart.com"]
  validation_method = "DNS"

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_route53_record" "cert_validation" {
  for_each = {
    for dvo in aws_acm_certificate.sporekart.domain_validation_options : dvo.domain_name => {
      name   = dvo.resource_record_name
      record = dvo.resource_record_value
      type   = dvo.resource_record_type
    }
  }

  zone_id = aws_route53_zone.sporekart.zone_id
  name    = each.value.name
  type    = each.value.type
  records = [each.value.record]
  ttl     = 60
}

resource "aws_acm_certificate_validation" "sporekart" {
  certificate_arn         = aws_acm_certificate.sporekart.arn
  validation_record_fqdns = [for record in aws_route53_record.cert_validation : record.fqdn]
}
```

## ALB HTTPS Listener

The ALB HTTPS listener uses the ACM certificate:

```hcl
resource "aws_lb_listener" "https" {
  load_balancer_arn = aws_lb.web_app.arn
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-TLS13-1-2-2021-06"
  certificate_arn   = aws_acm_certificate_validation.sporekart.certificate_arn
}
```

## TLS Configuration

| Setting | Value |
|---------|-------|
| TLS minimum version | TLS 1.2 |
| TLS 1.3 enabled | Yes |
| HSTS | `max-age=31536000; includeSubDomains; preload` |
| Cipher suite | `HIGH:!aNULL:!MD5` |

## Renewal Procedure

ACM certificates are **auto-renewed** by AWS. No manual action required.

To verify renewal status:
```bash
aws acm list-certificates --region us-east-1 --query "CertificateSummaryList[?contains(DomainName, 'sporekart.com')]"
```

## Related Resources

- `../terraform/acm.tf` — ACM certificate resource
- `../terraform/dns.tf` — Route53 validation records
- `../../infrastructure/nginx/security-headers.conf` — HSTS header
