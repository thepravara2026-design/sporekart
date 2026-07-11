# docs

## Service Documentation

- **API Specification**: OpenAPI 3.0 spec available at `/v3/api-docs` (dev) or via API gateway in production.
- **Swagger UI**: Available at `/swagger-ui.html` when `springdoc.swagger-ui.enabled=true`.
- **Architecture**: Admin service is a Spring Boot 3.3 microservice providing enterprise administration, operations dashboard, user management, and system configuration for SporeKart.
- **Key Endpoints**:
  - `GET /api/admin/health` - Service health
  - `GET /api/admin/info` - Service info
  - `POST /api/admin/users` - User management
  - `GET /api/admin/audit-logs` - Audit log retrieval
- **External Dependencies**: PostgreSQL, Redis, Kafka
