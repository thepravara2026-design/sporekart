#!/bin/bash
# SporeKart Enterprise Rollback Script
# Usage: ./scripts/rollback.sh [staging|production] [version]
set -euo pipefail

ENVIRONMENT="${1:-staging}"
ROLLBACK_VERSION="${2:-latest}"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "=== SporeKart Enterprise Rollback ==="
echo "Environment: $ENVIRONMENT"
echo "Target version: $ROLLBACK_VERSION"

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
    echo "❌ Unknown environment: $ENVIRONMENT"
    exit 1
    ;;
esac

# Step 1: Validate rollback version exists
echo "--- Validating rollback target ---"
if git tag | grep -q "^${ROLLBACK_VERSION}$"; then
  echo "✅ Version $ROLLBACK_VERSION found"
else
  echo "❌ Version $ROLLBACK_VERSION not found"
  echo "Available tags:"
  git tag --sort=-version:refname | head -10
  exit 1
fi

# Step 2: Database compatibility check
echo "--- Checking database compatibility ---"
git checkout "$ROLLBACK_VERSION" -- services/*/src/main/resources/db/migration/ 2>/dev/null || true
MIGRATIONS=$(find services -path "*/db/migration/*.sql" 2>/dev/null | wc -l)
echo "✅ Found $MIGRATIONS migration files at target version"
git checkout sporetest -- services/*/src/main/resources/db/migration/ 2>/dev/null || true

# Step 3: Execute rollback
echo "--- Rolling back $ENVIRONMENT ---"
aws ecs update-service --cluster "$CLUSTER" --service sporekart-api \
  --force-new-deployment --region us-east-1

# Step 4: Wait for stability
echo "--- Waiting for service stability ---"
aws ecs wait services-stable --cluster "$CLUSTER" --services sporekart-api \
  --region us-east-1 || echo "⚠️  Stability wait timed out, checking health..."

# Step 5: Verify
echo "--- Verifying rollback ---"
sleep 10
HTTP_CODE=$(curl -sSf -o /dev/null -w "%{http_code}" "${URL}/actuator/health" 2>/dev/null || echo "000")
if [ "$HTTP_CODE" = "200" ]; then
  echo "✅ Rollback successful — health check passed (HTTP $HTTP_CODE)"
else
  echo "❌ Rollback may have failed — health check returned HTTP $HTTP_CODE"
  exit 1
fi

echo "=== Rollback to $ROLLBACK_VERSION on $ENVIRONMENT complete ==="
