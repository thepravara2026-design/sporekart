# Sporekart — Terraform Outputs
# PRR-C01: Cloud Infrastructure Provisioning

output "alb_dns_name" {
  description = "DNS name of the application load balancer"
  value       = aws_lb.web_app.dns_name
}

output "ecs_cluster_id" {
  description = "ECS cluster ID"
  value       = aws_ecs_cluster.sporekart.id
}

output "ecs_task_definition_arn" {
  description = "ARN of the web app task definition"
  value       = aws_ecs_task_definition.web_app.arn
}

output "cloudwatch_log_group" {
  description = "CloudWatch log group for web app"
  value       = aws_cloudwatch_log_group.web_app.name
}

output "vpc_id" {
  description = "Production VPC ID"
  value       = aws_vpc.sporekart.id
}

output "secrets_manager_arns" {
  description = "ARNs of Secrets Manager secrets"
  value = {
    supabase_url  = aws_secretsmanager_secret.supabase_url.arn
    supabase_key  = aws_secretsmanager_secret.supabase_anon_key.arn
    sentry_dsn    = aws_secretsmanager_secret.sentry_dsn.arn
    stripe_pk     = aws_secretsmanager_secret.stripe_pk.arn
  }
}
