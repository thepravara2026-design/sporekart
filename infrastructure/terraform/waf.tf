# WAF Web ACL for sporekart.com
# Rate limiting and IP-based blocking at the CDN edge

resource "aws_wafv2_web_acl" "sporekart" {
  name        = "sporekart-prod-waf"
  description = "WAF ACL for SporeKart production"
  scope       = "CLOUDFRONT"

  default_action {
    allow {}
  }

  # Rate limiting: 1000 requests per 5 minutes per IP
  rule {
    name     = "rate-limit"
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
      metric_name                = "sporekart-rate-limit"
      sampled_requests_enabled   = true
    }
  }

  # Block common malicious patterns
  rule {
    name     = "block-common-attacks"
    priority = 2

    action {
      block {}
    }

    statement {
      managed_rule_group_statement {
        name        = "AWSManagedRulesCommonRuleSet"
        vendor_name = "AWS"
      }
    }

    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "sporekart-common-attacks"
      sampled_requests_enabled   = true
    }
  }

  # Block SQL injection
  rule {
    name     = "block-sqli"
    priority = 3

    action {
      block {}
    }

    statement {
      managed_rule_group_statement {
        name        = "AWSManagedRulesSQLiRuleSet"
        vendor_name = "AWS"
      }
    }

    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "sporekart-sqli"
      sampled_requests_enabled   = true
    }
  }

  # Block known bad IPs
  rule {
    name     = "block-bad-ips"
    priority = 4

    action {
      block {}
    }

    statement {
      ip_set_reference_statement {
        arn = aws_wafv2_ip_set.sporekart_blacklist.arn
      }
    }

    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "sporekart-bad-ips"
      sampled_requests_enabled   = true
    }
  }

  visibility_config {
    cloudwatch_metrics_enabled = true
    metric_name                = "sporekart-waf"
    sampled_requests_enabled   = true
  }

  tags = {
    Name        = "sporekart-prod-waf"
    Environment = var.environment
    ManagedBy   = "terraform"
  }
}

# IP set for known malicious IPs (populated via automated feeds)
resource "aws_wafv2_ip_set" "sporekart_blacklist" {
  name               = "sporekart-prod-blacklist"
  description        = "Known malicious IP addresses"
  scope              = "CLOUDFRONT"
  ip_address_version = "IPV4"
  addresses          = []
}

# WAF logging to S3
resource "aws_wafv2_web_acl_logging_configuration" "sporekart" {
  log_destination_configs = [aws_cloudwatch_log_group.waf.arn]
  resource_arn            = aws_wafv2_web_acl.sporekart.arn
}

resource "aws_cloudwatch_log_group" "waf" {
  name              = "/aws/waf/sporekart-prod"
  retention_in_days = 30

  tags = {
    Name        = "sporekart-prod-waf-logs"
    Environment = var.environment
    ManagedBy   = "terraform"
  }
}
