# SporeKart Enterprise Platform v1.0 Release Notes

## Release Summary
SporeKart Enterprise Platform v1.0 is prepared as a production-readiness release candidate. The release focuses on platform certification, deployment readiness, operational documentation, and hardening of the existing service foundation.

## Included
- Admin operations service foundation
- Analytics reporting and SEO service foundation
- Notification service foundation
- Flyway migration scaffolding
- Actuator health and observability hooks
- Release and deployment documentation

## Known Limitations
- Production secrets and environment variables remain to be injected via deployment environment
- Real integrations with Supabase PostgreSQL, Redis, and Kafka are not yet wired into production profiles
- Advanced security policies such as rate limiting and CSP are scaffolded but require production tuning

## Rollback Guidance
- Revert to the previous deployment tag
- Restore the previous database snapshot if a migration issue occurs
- Re-enable the previous release environment variables
