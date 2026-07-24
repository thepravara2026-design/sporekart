# Staging Environment Infrastructure
# Mirrors production with reduced capacity

resource "aws_ecs_cluster" "sporekart_staging" {
  name = "sporekart-staging"

  setting {
    name  = "containerInsights"
    value = "enabled"
  }

  tags = {
    Name        = "sporekart-staging"
    Environment = "staging"
    ManagedBy   = "terraform"
  }
}

resource "aws_ecs_task_definition" "web_app_staging" {
  family                   = "sporekart-web-app-staging"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"
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
        { name = "ENVIRONMENT", value = "staging" }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.web_app_staging.name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])

  tags = {
    Name        = "sporekart-web-app-staging"
    Environment = "staging"
    ManagedBy   = "terraform"
  }
}

resource "aws_ecs_service" "web_app_staging" {
  name            = "sporekart-web-app"
  cluster         = aws_ecs_cluster.sporekart_staging.id
  task_definition = aws_ecs_task_definition.web_app_staging.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = aws_subnet.private[*].id
    security_groups = [aws_security_group.web_app_staging.id]
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.web_app_staging.arn
    container_name   = "web-app"
    container_port   = 4173
  }

  tags = {
    Name        = "sporekart-web-app-staging"
    Environment = "staging"
    ManagedBy   = "terraform"
  }
}

resource "aws_lb" "web_app_staging" {
  name               = "sporekart-staging-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb_staging.id]
  subnets            = aws_subnet.public[*].id

  tags = {
    Name        = "sporekart-staging-alb"
    Environment = "staging"
    ManagedBy   = "terraform"
  }
}

resource "aws_lb_target_group" "web_app_staging" {
  name        = "sporekart-staging-tg"
  port        = 4173
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = aws_vpc.sporekart.id

  health_check {
    path                = "/health"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 3
  }
}

resource "aws_lb_listener" "web_app_staging_https" {
  load_balancer_arn = aws_lb.web_app_staging.arn
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-TLS13-1-2-2021-06"
  certificate_arn   = var.ssl_certificate_arn

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.web_app_staging.arn
  }
}

resource "aws_lb_listener" "web_app_staging_http" {
  load_balancer_arn = aws_lb.web_app_staging.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type = "redirect"
    redirect {
      port        = "443"
      protocol    = "HTTPS"
      status_code = "HTTP_301"
    }
  }
}

resource "aws_security_group" "web_app_staging" {
  name        = "sporekart-staging-web-app-sg"
  description = "Security group for staging web app"
  vpc_id      = aws_vpc.sporekart.id

  ingress {
    from_port       = 4173
    to_port         = 4173
    protocol        = "tcp"
    security_groups = [aws_security_group.alb_staging.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "alb_staging" {
  name        = "sporekart-staging-alb-sg"
  description = "Security group for staging ALB"
  vpc_id      = aws_vpc.sporekart.id

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_cloudwatch_log_group" "web_app_staging" {
  name              = "/ecs/sporekart-web-app-staging"
  retention_in_days = 7
}
