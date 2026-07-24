# Deployment Guide

**SporeKart Enterprise Platform v2.0**  
**Document:** DeploymentGuide.md  
**Last Updated:** 2026-07-24

---

## Architecture

```
                         ┌──────────────┐
                         │   Route 53   │
                         │  sporekart   │
                         │    .com      │
                         └──────┬───────┘
                                │
                         ┌──────▼───────┐
                         │   CloudFront  │
                         │    CDN + WAF  │
                         └──────┬───────┘
                                │
                    ┌───────────┴───────────┐
                    │                       │
             ┌──────▼──────┐        ┌──────▼──────┐
             │    ALB      │        │  S3 Static  │
             │  (public)   │        │  (frontend) │
             └──────┬──────┘        └─────────────┘
                    │
             ┌──────▼──────┐
             │    ECS      │
             │  (Fargate)  │
             │             │
             │  Gateway    │
             │  Identity   │
             │  Catalog    │
             │  Cart       │
             │  Order      │
             │  Payment    │
             │  AI Service │
             │  ...        │
             └──────┬──────┘
                    │
             ┌──────▼──────┐
             │    RDS      │
             │ PostgreSQL  │
             └─────────────┘
```

---

## Prerequisites

- AWS CLI configured with appropriate permissions
- Access to ECR repository
- Kubernetes context or ECS CLI configured
- GitHub Actions secrets configured (see below)

### Required GitHub Secrets

| Secret | Purpose |
|--------|---------|
| `AWS_ACCOUNT_ID` | AWS account ID for ECR |
| `AWS_DEPLOY_ROLE_ARN` | IAM role for deployment |
| `SLACK_DEPLOY_WEBHOOK` | Slack notification webhook |
| `DEPLOY_APPROVAL_TOKEN` | Manual approval gate token |

---

## Deployment Environments

| Environment | ECS Cluster | URL | Auto-deploy |
|-------------|------------|-----|-------------|
| Development | sporekart-dev | dev.sporekart.com | PR preview |
| Staging | sporekart-staging | staging.sporekart.com | CI success on sporetest |
| Production | sporekart-prod | sporekart.com | Manual approval |

---

## Deployment Methods

### Method 1: GitHub Actions (Recommended)

Trigger the **CD — Enterprise Deployment Pipeline**:
1. Go to Actions → CD — Enterprise Deployment Pipeline
2. Click "Run workflow"
3. Select environment (staging or production)
4. Enter image tag (optional, defaults to git SHA)
5. For production: 2 approvers must approve within 60 minutes

### Method 2: Command Line

```bash
# Deploy to staging
./scripts/deploy.sh staging v2.1.0

# Deploy to production
./scripts/deploy.sh production v2.1.0
```

### Method 3: AWS CLI

```bash
aws ecs update-service \
  --cluster sporekart-staging \
  --service sporekart-api \
  --force-new-deployment \
  --region us-east-1

aws ecs wait services-stable \
  --cluster sporekart-staging \
  --services sporekart-api \
  --region us-east-1
```

---

## Deployment Validation

After deployment, the pipeline automatically runs:

1. **Health check**: `GET /actuator/health` → 200
2. **Auth endpoint**: `GET /auth/login` → 200
3. **Security headers**: CSP, HSTS, X-Frame-Options present
4. **Database migration**: Flyway reports successful migration

### Manual Validation

```bash
# Health check
curl -sSf -o /dev/null -w "%{http_code}" https://sporekart.com/actuator/health

# Check deployed version
curl -s https://sporekart.com/actuator/info | jq '.app.version'

# Check Flyway migrations
curl -s https://sporekart.com/actuator/flyway | jq '.migrations[-1]'
```

---

## Blue-Green Deployment

The ECS service uses blue-green deployment pattern:

1. New task definition registered with new image tag
2. ECS provisions new tasks (green) alongside existing (blue)
3. ALB health check determines when green is ready
4. Traffic shifts to green tasks
5. Blue tasks are scaled down

### Feature Flags

For canary releases, feature flags are managed via:

- **Application config**: `application-feature.yml`
- **Runtime toggle**: `@ConditionalOnProperty` annotations
- **Admin UI**: Feature flag dashboard at `/admin/features`

---

## Database Migrations

Flyway manages all schema changes:

```bash
# Check pending migrations
curl -s https://staging.sporekart.com/actuator/flyway | jq '.migrations | .[] | select(.state == "PENDING")'

# Manual migration (emergency only)
aws ecs run-task --cluster sporekart-staging \
  --task-definition sporekart-migration \
  --region us-east-1
```

**Rules**:
- Never modify a migration that has run in production
- New changes = new V{next}__{description}.sql
- Always test migration on staging before production
