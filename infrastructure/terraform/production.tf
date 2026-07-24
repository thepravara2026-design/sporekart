# SporeKart — AWS Production Infrastructure (Terraform)
# PRR-C01: Cloud Infrastructure Provisioning
# Provisioned: 20-Jul-2026

provider "aws" {
  region = var.aws_region
  tags = {
    Environment = "production"
    Project     = "sporekart"
    ManagedBy   = "terraform"
  }
}

# ---- Networking ----

resource "aws_vpc" "sporekart" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = { Name = "sporekart-prod-vpc" }
}

resource "aws_subnet" "public_a" {
  vpc_id                  = aws_vpc.sporekart.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "${var.aws_region}a"
  map_public_ip_on_launch = true
  tags = { Name = "sporekart-prod-public-a" }
}

resource "aws_subnet" "public_b" {
  vpc_id                  = aws_vpc.sporekart.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "${var.aws_region}b"
  map_public_ip_on_launch = true
  tags = { Name = "sporekart-prod-public-b" }
}

resource "aws_subnet" "private_a" {
  vpc_id            = aws_vpc.sporekart.id
  cidr_block        = "10.0.10.0/24"
  availability_zone = "${var.aws_region}a"
  tags = { Name = "sporekart-prod-private-a" }
}

resource "aws_subnet" "private_b" {
  vpc_id            = aws_vpc.sporekart.id
  cidr_block        = "10.0.11.0/24"
  availability_zone = "${var.aws_region}b"
  tags = { Name = "sporekart-prod-private-b" }
}

resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.sporekart.id
  tags = { Name = "sporekart-prod-igw" }
}

resource "aws_eip" "nat" {
  domain = "vpc"
  tags = { Name = "sporekart-prod-nat-eip" }
}

resource "aws_nat_gateway" "main" {
  allocation_id = aws_eip.nat.id
  subnet_id     = aws_subnet.public_a.id
  tags = { Name = "sporekart-prod-nat" }
}

# ---- Compute (ECS Fargate) ----

resource "aws_ecs_cluster" "sporekart" {
  name = "sporekart-prod"
  tags = { Name = "sporekart-prod-cluster" }
}

resource "aws_ecs_task_definition" "web_app" {
  family                   = "sporekart-web-app"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "512"
  memory                   = "1024"
  execution_role_arn       = aws_iam_role.ecs_execution.arn
  task_role_arn            = aws_iam_role.ecs_task.arn

  container_definitions = jsonencode([
    {
      name      = "web-app"
      image     = "${var.container_registry}/sporekart-web-app:${var.image_tag}"
      essential = true
      portMappings = [
        {
          containerPort = 4173
          protocol      = "tcp"
        }
      ]
      environment = [
        { name = "NODE_ENV", value = "production" }
      ]
      secrets = [
        { name = "SUPABASE_URL", valueFrom = "${aws_secretsmanager_secret.supabase_url.arn}" },
        { name = "SUPABASE_ANON_KEY", valueFrom = "${aws_secretsmanager_secret.supabase_anon_key.arn}" },
        { name = "VITE_SENTRY_DSN", valueFrom = "${aws_secretsmanager_secret.sentry_dsn.arn}" },
        { name = "VITE_STRIPE_PUBLISHABLE_KEY", valueFrom = "${aws_secretsmanager_secret.stripe_pk.arn}" }
      ]
      healthCheck = {
        command     = ["CMD-SHELL", "node -e \"require('http').get('http://localhost:4173/health', r => {process.exit(r.statusCode === 200 ? 0 : 1)})\""]
        interval    = 30
        timeout     = 10
        retries     = 3
        startPeriod = 10
      }
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.web_app.name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "web-app"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "web_app" {
  name            = "sporekart-web-app"
  cluster         = aws_ecs_cluster.sporekart.id
  task_definition = aws_ecs_task_definition.web_app.arn
  desired_count   = 2
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = [aws_subnet.private_a.id, aws_subnet.private_b.id]
    security_groups = [aws_security_group.web_app.id]
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.web_app.arn
    container_name   = "web-app"
    container_port   = 4173
  }

  health_check_grace_period_seconds = 30

  depends_on = [aws_lb_listener.https]
}

# ---- Load Balancer ----

resource "aws_lb" "web_app" {
  name               = "sporekart-prod-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb.id]
  subnets            = [aws_subnet.public_a.id, aws_subnet.public_b.id]
  tags = { Name = "sporekart-prod-alb" }
}

resource "aws_lb_target_group" "web_app" {
  name        = "sporekart-web-app-tg"
  port        = 4173
  protocol    = "HTTP"
  vpc_id      = aws_vpc.sporekart.id
  target_type = "ip"

  health_check {
    path                = "/health"
    healthy_threshold   = 2
    unhealthy_threshold = 3
    timeout             = 10
    interval            = 30
  }
}

resource "aws_lb_listener" "https" {
  load_balancer_arn = aws_lb.web_app.arn
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-TLS13-1-2-2021-06"
  certificate_arn   = var.ssl_certificate_arn

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.web_app.arn
  }
}

resource "aws_lb_listener" "http_redirect" {
  load_balancer_arn = aws_lb.web_app.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type = "redirect"
    redirect {
      protocol    = "HTTPS"
      port        = "443"
      status_code = "HTTP_301"
    }
  }
}

# ---- Security Groups ----

resource "aws_security_group" "alb" {
  name        = "sporekart-prod-alb-sg"
  description = "ALB security group"
  vpc_id      = aws_vpc.sporekart.id

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "HTTPS from anywhere"
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "HTTP redirect"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "web_app" {
  name        = "sporekart-prod-web-app-sg"
  description = "Web app security group"
  vpc_id      = aws_vpc.sporekart.id

  ingress {
    from_port       = 4173
    to_port         = 4173
    protocol        = "tcp"
    security_groups = [aws_security_group.alb.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# ---- Monitoring & Logging ----

resource "aws_cloudwatch_log_group" "web_app" {
  name              = "/ecs/sporekart-web-app"
  retention_in_days = 30
  tags = { Name = "sporekart-prod-logs" }
}

# ---- IAM ----

resource "aws_iam_role" "ecs_execution" {
  name = "sporekart-ecs-execution-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_execution_policy" {
  role       = aws_iam_role.ecs_execution.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role" "ecs_task" {
  name = "sporekart-ecs-task-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })
}

# ---- Secrets Manager ----

resource "aws_secretsmanager_secret" "supabase_url" {
  name = "sporekart/prod/supabase-url"
}

resource "aws_secretsmanager_secret" "supabase_anon_key" {
  name = "sporekart/prod/supabase-anon-key"
}

resource "aws_secretsmanager_secret" "sentry_dsn" {
  name = "sporekart/prod/sentry-dsn"
}

resource "aws_secretsmanager_secret" "stripe_pk" {
  name = "sporekart/prod/stripe-pk"
}

# ---- Outputs ----

output "alb_dns_name" {
  value = aws_lb.web_app.dns_name
}

output "ecs_cluster_name" {
  value = aws_ecs_cluster.sporekart.name
}

output "cloudwatch_log_group" {
  value = aws_cloudwatch_log_group.web_app.name
}
