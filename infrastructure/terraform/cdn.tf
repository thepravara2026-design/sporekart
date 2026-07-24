# CloudFront CDN for sporekart.com
# Global content delivery with WAF, caching, and HTTPS

resource "aws_cloudfront_distribution" "sporekart" {
  enabled             = true
  is_ipv6_enabled     = true
  comment             = "SporeKart production CDN"
  aliases             = ["sporekart.com", "www.sporekart.com"]
  price_class         = "PriceClass_100"

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

  default_cache_behavior {
    allowed_methods  = ["GET", "HEAD", "OPTIONS", "PUT", "POST", "PATCH", "DELETE"]
    cached_methods   = ["GET", "HEAD", "OPTIONS"]
    target_origin_id = "sporekart-alb"
    compress         = true

    forwarded_values {
      query_string = true
      cookies {
        forward = "all"
      }
      headers = [
        "Authorization",
        "CloudFront-Forwarded-Proto",
        "Host",
        "Origin",
        "Referer",
        "X-Correlation-ID"
      ]
    }

    viewer_protocol_policy = "redirect-to-https"
    min_ttl                = 0
    default_ttl            = 60
    max_ttl                = 3600
  }

  ordered_cache_behavior {
    path_pattern     = "/assets/*"
    allowed_methods  = ["GET", "HEAD", "OPTIONS"]
    cached_methods   = ["GET", "HEAD", "OPTIONS"]
    target_origin_id = "sporekart-alb"
    compress         = true

    forwarded_values {
      query_string = false
      cookies {
        forward = "none"
      }
    }

    viewer_protocol_policy = "redirect-to-https"
    min_ttl                = 86400
    default_ttl            = 86400
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
    minimum_protocol_version = "TLSv1.2_2023"
  }

  custom_error_response {
    error_code         = 403
    response_code      = 200
    response_page_path = "/index.html"
  }

  custom_error_response {
    error_code         = 404
    response_code      = 200
    response_page_path = "/index.html"
  }

  web_acl_id = aws_wafv2_web_acl.sporekart.arn

  tags = {
    Name        = "sporekart-prod-cdn"
    Environment = var.environment
    ManagedBy   = "terraform"
  }
}
