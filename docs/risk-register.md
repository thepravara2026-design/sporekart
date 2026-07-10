# Risk Register

| Area | Risk | Severity | Current Status | Mitigation |
|---|---|---|---|---|
| Infrastructure | Production PostgreSQL, Redis, and Kafka connectivity not validated | High | Open | Validate in staging and production-like environments |
| Security | Development-oriented defaults and incomplete hardening controls | High | Open | Replace defaults and enforce security policies |
| API | Contracts incomplete and unvalidated | High | Open | Publish and test OpenAPI artifacts for all services |
| Performance | No verified latency or throughput baseline | High | Open | Run load and stress tests before go-live |
| Business flow | No proven end-to-end journeys | High | Open | Execute integration and smoke tests for customer, grower, and admin flows |
| Operations | Backup, restore, and rollback not yet validated | High | Open | Run recovery drills and verify procedures |
| Documentation | Release package is strong but not fully operationalized | Medium | Open | Close the remaining documentation and runbook gaps |
