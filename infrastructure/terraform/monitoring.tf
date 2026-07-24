# Monitoring Infrastructure for SporeKart
# CloudWatch dashboards, alarms, and metric filters

# Production dashboard
resource "aws_cloudwatch_dashboard" "sporekart_prod" {
  dashboard_name = "sporekart-prod"

  dashboard_body = jsonencode({
    widgets = [
      {
        type = "metric"
        properties = {
          metrics = [
            ["AWS/ECS", "CPUUtilization", { stat = "Average" }],
            ["AWS/ECS", "MemoryUtilization", { stat = "Average" }]
          ]
          period = 300
          stat   = "Average"
          region = var.aws_region
          title  = "ECS Resource Utilization"
        }
      },
      {
        type = "metric"
        properties = {
          metrics = [
            ["AWS/ApplicationELB", "RequestCount", { stat = "Sum" }],
            ["AWS/ApplicationELB", "TargetResponseTime", { stat = "p99" }]
          ]
          period = 300
          stat   = "Sum"
          region = var.aws_region
          title  = "ALB Request Metrics"
        }
      },
      {
        type = "metric"
        properties = {
          metrics = [
            ["AWS/ApplicationELB", "HTTPCode_Target_5XX_Count", { stat = "Sum" }],
            ["AWS/ApplicationELB", "HTTPCode_Target_4XX_Count", { stat = "Sum" }]
          ]
          period = 300
          stat   = "Sum"
          region = var.aws_region
          title  = "ALB Error Rates"
        }
      }
    ]
  })
}

# High CPU alarm
resource "aws_cloudwatch_metric_alarm" "ecs_high_cpu" {
  alarm_name          = "sporekart-prod-high-cpu"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = "3"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/ECS"
  period              = "300"
  statistic           = "Average"
  threshold           = "80"
  alarm_description   = "ECS CPU utilization > 80% for 15 minutes"
  alarm_actions       = [aws_sns_topic.sporekart_alerts.arn]

  dimensions = {
    ClusterName = "sporekart-prod"
  }
}

# High error rate alarm
resource "aws_cloudwatch_metric_alarm" "alb_high_5xx" {
  alarm_name          = "sporekart-prod-high-5xx"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = "3"
  metric_name         = "HTTPCode_Target_5XX_Count"
  namespace           = "AWS/ApplicationELB"
  period              = "300"
  statistic           = "Sum"
  threshold           = "50"
  alarm_description   = "ALB 5xx errors > 50 in 15 minutes"
  alarm_actions       = [aws_sns_topic.sporekart_alerts.arn]

  dimensions = {
    LoadBalancer = aws_lb.web_app.arn_suffix
  }
}

# SNS topic for alerts
resource "aws_sns_topic" "sporekart_alerts" {
  name = "sporekart-prod-alerts"

  tags = {
    Name        = "sporekart-prod-alerts"
    Environment = var.environment
    ManagedBy   = "terraform"
  }
}

# SNS subscription (configure via variable)
resource "aws_sns_topic_subscription" "sporekart_alerts_email" {
  count     = var.alert_email != "" ? 1 : 0
  topic_arn = aws_sns_topic.sporekart_alerts.arn
  protocol  = "email"
  endpoint  = var.alert_email
}
