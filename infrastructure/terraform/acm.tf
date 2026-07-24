# ACM Certificate for sporekart.com
# Managed DNS validation via Route53

resource "aws_acm_certificate" "sporekart" {
  domain_name       = "sporekart.com"
  subject_alternative_names = [
    "*.sporekart.com",
    "api.sporekart.com",
    "identity.sporekart.com"
  ]
  validation_method = "DNS"

  lifecycle {
    create_before_destroy = true
  }

  tags = {
    Name        = "sporekart-prod-ssl"
    Environment = var.environment
    ManagedBy   = "terraform"
  }
}

resource "aws_route53_record" "sporekart_cert_validation" {
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
  validation_record_fqdns = [for record in aws_route53_record.sporekart_cert_validation : record.fqdn]
}
