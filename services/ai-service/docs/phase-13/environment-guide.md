# Environment Configuration Guide

## Environment Types

| Environment | Profile | Database | Secrets | SSL | Purpose |
|-------------|---------|----------|---------|-----|---------|
| Local | local | H2 in-memory | None | No | Developer workstation |
| Development | dev | PostgreSQL | Required | No | Shared dev server |
| Test | test | H2 in-memory | None | No | CI/CD testing |
| Staging | stage | PostgreSQL | Required | Yes | Pre-production validation |
| Production | prod | PostgreSQL | Required | Yes | Live production |
| Docker | docker | PostgreSQL | Optional | No | Docker Compose |
| Cloud | cloud | PostgreSQL | Required | Yes | Cloud deployment |

## Standard Environment Variables

### Database
| Variable | Description | Required |
|----------|-------------|----------|
| AI_DATABASE_URL | JDBC connection URL | Production only |
| AI_DB_USERNAME | Database username | Production only |
| AI_DB_PASSWORD | Database password | Production only |

### Redis
| Variable | Description | Required |
|----------|-------------|----------|
| AI_REDIS_HOST | Redis hostname | Production only |
| AI_REDIS_PORT | Redis port | Production only |
| AI_REDIS_PASSWORD | Redis password | Production only |

### Kafka
| Variable | Description | Required |
|----------|-------------|----------|
| AI_KAFKA_SERVERS | Kafka bootstrap servers | Production only |

## Applying Profiles

```bash
# Single profile
--spring.profiles.active=prod

# Multiple profiles
--spring.profiles.active=prod,cloud

# Environment variable
SPRING_PROFILES_ACTIVE=prod,cloud

# Application default (application.yml)
spring.profiles.active: prod
```
