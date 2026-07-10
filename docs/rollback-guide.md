# Rollback Guide

## Trigger Conditions
- Failed health checks after deployment
- Critical regression in checkout, payment, or admin flows
- Database migration failure or data integrity issue

## Rollback Procedure
1. Redeploy the previous release image tag.
2. Revert environment variables to the previous known-good values.
3. Restore the previous database snapshot if required.
4. Re-run smoke tests and confirm health endpoints.

## Validation
- Confirm services return healthy
- Confirm monitoring and alerting are stable
- Confirm key user journeys are operational
