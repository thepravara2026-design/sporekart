# config

## Configuration Guide

### Environment Variables (see `.env.example`)

| Variable | Description | Default |
|----------|-------------|---------|
| `SERVER_PORT` | HTTP port | `8089` |
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/admin-service` |
| `SPRING_DATASOURCE_USERNAME` | DB username | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | DB password | `postgres` |
| `SPRING_REDIS_HOST` | Redis host | `localhost` |
| `SPRING_REDIS_PORT` | Redis port | `6379` |

### Profiles

- **default** / **local**: H2 in-memory database, Flyway migrations enabled
- **dev**: PostgreSQL dev database, debug logging
- **prod**: PostgreSQL production database, performance-optimized connection pool

### Flyway Migrations

Migrations are stored in `src/main/resources/db/migration` and follow the naming convention `V<version>__<description>.sql`.
