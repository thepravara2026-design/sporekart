# Technology Baseline

This repository adopts the following mandatory technology baseline for all platform, service, and architectural decisions.

## Frontend

- React
- Vite
- TypeScript
- TailwindCSS
- TanStack Query
- React Hook Form
- Zod

## Backend

- Java 21 LTS
- Spring Boot 3.x
- Spring Security
- Spring Validation
- Spring Data JPA
- Spring Actuator
- Flyway
- OpenAPI 3.1
- AsyncAPI
- Maven
- JUnit 5
- Mockito
- Testcontainers
- Lombok (optional)
- MapStruct

## Database

- Supabase PostgreSQL as a managed PostgreSQL service only
- Allowed Supabase capabilities:
  - PostgreSQL
  - Storage
  - Backups
  - Point-in-Time Recovery
- Explicitly not used for business authorization or application auth:
  - Supabase Auth
  - Supabase Edge Functions
  - Supabase Realtime
  - Supabase Row Level Security for business authorization

## Authentication and Authorization

- Spring Security
- JWT
- Refresh tokens
- OTP authentication
- RBAC
- BCrypt password encoder
- Method-level security
- Audit logging

## Platform Services

- Cache: Redis
- Messaging: Kafka
- Observability: OpenTelemetry, Prometheus, Grafana
- Search: OpenSearch
- Containerization: Docker
- Deployment readiness: Kubernetes-ready

## Architectural Guardrails

1. Every new service and architectural proposal must align with this baseline.
2. Any decision that introduces a conflicting stack must be explicitly justified and documented.
3. Security, observability, and deployment readiness are required from the initial service skeleton.
4. Supabase is used strictly as managed PostgreSQL storage infrastructure and related backup capabilities.
5. Business authorization remains within the application layer and Spring Security.
