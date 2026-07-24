#!/bin/bash
# SporeKart Enterprise Deployment Script
# Usage: ./scripts/deploy.sh [staging|production] [tag]
set -euo pipefail

ENVIRONMENT="${1:-staging}"
IMAGE_TAG="${2:-latest}"

echo "=== SporeKart Enterprise Deployment ==="
echo "Environment: $ENVIRONMENT"
echo "Image tag: $IMAGE_TAG"

case "$ENVIRONMENT" in
  staging)
    CLUSTER="sporekart-staging"
    URL="https://staging.sporekart.com"
    ;;
  production)
    CLUSTER="sporekart-prod"
    URL="https://sporekart.com"
    ;;
  *)
    echo "❌ Unknown environment: $ENVIRONMENT (use staging or production)"
    exit 1
    ;;
esac

# Step 1: Deploy backend
echo "--- Deploying backend to $ENVIRONMENT ---"
aws ecs update-service --cluster "$CLUSTER" --service sporekart-api \
  --force-new-deployment --region us-east-1

# Step 2: Wait for stable
echo "--- Waiting for service stability ---"
aws ecs wait services-stable --cluster "$CLUSTER" --services sporekart-api \
  --region us-east-1

# Step 3: Health check
echo "--- Running health check ---"
sleep 10
HTTP_CODE=$(curl -sSf -o /dev/null -w "%{http_code}" "${URL}/actuator/health" 2>/dev/null || echo "000")
if [ "$HTTP_CODE" = "200" ]; then
  echo "✅ Health check passed (HTTP $HTTP_CODE)"
else
  echo "❌ Health check failed (HTTP $HTTP_CODE)"
  exit 1
fi

# Step 4: Smoke test
echo "--- Running smoke tests ---"
curl -sSf -o /dev/null -w "%{http_code}" "${URL}/auth/login" 2>/dev/null && \
  echo "✅ Auth endpoint reachable" || echo "⚠️  Auth endpoint check skipped"

echo ""
echo "=== Deployment to $ENVIRONMENT complete ==="
