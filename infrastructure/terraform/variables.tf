# SporeKart — Terraform Variables
# PRR-C01: Cloud Infrastructure Provisioning

variable "aws_region" {
  description = "AWS region for production deployment"
  type        = string
  default     = "us-east-1"
}

variable "container_registry" {
  description = "ECR repository URL (e.g., 123456789012.dkr.ecr.us-east-1.amazonaws.com)"
  type        = string
}

variable "image_tag" {
  description = "Container image tag to deploy"
  type        = string
  default     = "latest"
}

variable "ssl_certificate_arn" {
  description = "ARN of ACM certificate for *.sporekart.com"
  type        = string
}

variable "environment" {
  description = "Deployment environment"
  type        = string
  default     = "production"
}

variable "alert_email" {
  description = "Email for CloudWatch alarm notifications"
  type        = string
  default     = ""
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}
