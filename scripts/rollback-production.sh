#!/usr/bin/env bash
# SporeKart — Production Rollback Script
# PRR-C08: Rollback Capability
# Status: ✅ CONFIGURED — 20-Jul-2026

set -euo pipefail

# Configuration
AWS_REGION="${AWS_REGION:-us-east-1}"
ECS_CLUSTER="${ECS_CLUSTER:-sporekart-prod}"
ECS_SERVICE="${ECS_SERVICE:-sporekart-web-app}"
PREVIOUS_TAG="${1:?Usage: $0 <previous-image-tag>}"

echo "=== SporeKart Production Rollback ==="
echo "Rolling back to image tag: $PREVIOUS_TAG"
echo "Cluster: $ECS_CLUSTER"
echo "Service: $ECS_SERVICE"
echo ""

# Step 1: Get ECR registry
ECR_REGISTRY=$(aws ecr describe-repositories \
    --repository-names sporekart-web-app \
    --region "$AWS_REGION" \
    --query 'repositories[0].repositoryUri' \
    --output text | sed 's|/sporekart-web-app||')

echo "ECR Registry: $ECR_REGISTRY"

# Step 2: Register new task definition with previous image
TASK_DEF_ARN=$(aws ecs describe-task-definition \
    --task-definition sporekart-web-app \
    --region "$AWS_REGION" \
    --query 'taskDefinition.taskDefinitionArn' \
    --output text)

echo "Current task definition: $TASK_DEF_ARN"

# Get current task definition JSON and update image
CURRENT_TD=$(aws ecs describe-task-definition \
    --task-definition sporekart-web-app \
    --region "$AWS_REGION")

NEW_TD=$(echo "$CURRENT_TD" | jq --arg IMG "$ECR_REGISTRY/sporekart-web-app:$PREVIOUS_TAG" \
    '.taskDefinition | .containerDefinitions[0].image = $IMG | {family, taskRoleArn, executionRoleArn, networkMode, containerDefinitions, volumes, placementConstraints, requiresCompatibilities, cpu, memory}')

NEW_TD_ARN=$(aws ecs register-task-definition \
    --region "$AWS_REGION" \
    --cli-input-json "$(echo "$NEW_TD" | jq '{family, taskRoleArn, executionRoleArn, networkMode, containerDefinitions, volumes, placementConstraints, requiresCompatibilities, cpu, memory}')" \
    --query 'taskDefinition.taskDefinitionArn' \
    --output text)

echo "New task definition: $NEW_TD_ARN"

# Step 3: Update service to use new task definition
echo "Updating ECS service..."
aws ecs update-service \
    --cluster "$ECS_CLUSTER" \
    --service "$ECS_SERVICE" \
    --task-definition "$NEW_TD_ARN" \
    --force-new-deployment \
    --region "$AWS_REGION" > /dev/null

echo "Waiting for service stability..."
aws ecs wait services-stable \
    --cluster "$ECS_CLUSTER" \
    --services "$ECS_SERVICE" \
    --region "$AWS_REGION"

# Step 4: Verify rollback
echo "Verifying health endpoint..."
HEALTH_STATUS=$(curl -sSf -o /dev/null -w "%{http_code}" https://sporekart.com/health 2>/dev/null || echo "000")

if [ "$HEALTH_STATUS" = "200" ]; then
    echo "✅ Rollback successful — health endpoint returns 200"
else
    echo "❌ Rollback verification failed — health endpoint returned $HEALTH_STATUS"
    exit 1
fi

echo ""
echo "=== Rollback Complete ==="
echo "Deployed image: $ECR_REGISTRY/sporekart-web-app:$PREVIOUS_TAG"
