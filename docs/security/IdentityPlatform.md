# Identity Platform

**Version:** 2.0.0
**Last Updated:** 2026-07-23
**Module:** identity-service

---

## Overview

The SporeKart Identity Platform provides a comprehensive authentication and authorization system built on Spring Boot 3.3.3 with Java 21. It follows hexagonal architecture (ports/adapters) with JPA/Hibernate persistence, Flyway migrations, Redis caching, and Kafka event publishing.

---

## Features

### Authentication
- JWT-based authentication with HMAC-SHA256 signing (Nimbus JOSE + JWT)
- Dual token system: access (15min) + refresh (7 day) tokens
- Refresh token rotation — old token blacklisted on each refresh
- Clock skew tolerance (30 seconds) for distributed environments
- In-memory token blacklisting (10,000 entry FIFO eviction)
- OTP flow with 6-digit cryptographically-random codes
- OTP rate limiting: 3 requests per 60-second window
- OTP expiry: 5 minutes, max 5 verification attempts

### Session Management
- Server-side sessions created on every login
- 10 concurrent session limit per user (oldest eviction)
- 60-minute session TTL with activity tracking
- Session revocation (single or all)
- Scheduled cleanup of expired sessions

### Device Tracking
- Device auto-registration on login via fingerprint (User-Agent + IP hash)
- Device trust and suspicious flags
- OS and browser extraction from User-Agent header
- Device CRUD API

### Authorization
- 6 roles: CUSTOMER, GROWER, SUPPORT, OPERATIONS, ADMIN, SUPER_ADMIN
- 10 fine-grained permissions with role-based mapping
- Permission checks at service layer
- Route-level authorization in Spring Security

### Workspace Isolation
- Multi-workspace support with owner isolation
- Workspace membership tracking (`workspace_members` join table)
- Session-bound workspace context

### Audit Trail
- All auth events logged: login, logout, refresh, OTP
- Admin audit query API with event type filtering
- Audit statistics: total logins, logouts, refreshes, OTP logins

---

## Architecture Diagram

```
┌──────────────────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                                     │
│  Web App / Mobile App / API Clients                                      │
└────────────────────────────────┬─────────────────────────────────────────┘
                                 │ HTTP (Bearer JWT)
                                 ▼
┌──────────────────────────────────────────────────────────────────────────┐
│                        API GATEWAY / LB                                 │
│  Route: /api/v1/identity/*  →  identity-service:8080                   │
└────────────────────────────────┬─────────────────────────────────────────┘
                                 │
                                 ▼
┌──────────────────────────────────────────────────────────────────────────┐
│                    INTERFACES LAYER (REST Controllers)                  │
│                                                                         │
│  AuthController  SessionController  DeviceController                    │
│  UserController  WorkspaceController  AdminController                   │
└────────────────────────────────┬─────────────────────────────────────────┘
                                 │
                                 ▼
┌──────────────────────────────────────────────────────────────────────────┐
│                    APPLICATION LAYER (Services)                         │
│                                                                         │
│  ┌──────────────┐  ┌──────────────┐  ┌────────────────┐                │
│  │ IdentitySvc  │  │ SessionSvc   │  │ DeviceSvc      │                │
│  │ (Orchestrator)│  │              │  │                │                │
│  └──────┬───────┘  └──────┬───────┘  └───────┬────────┘                │
│         │                 │                   │                         │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌───────▼────────┐                │
│  │ JwtSvc       │  │ OtpSvc       │  │ PermissionSvc  │                │
│  └──────┬───────┘  └──────┬───────┘  └───────┬────────┘                │
│         │                 │                   │                         │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌───────▼────────┐                │
│  │ WorkspaceSvc │  │ AuditSvc     │  │                │                │
│  └──────┬───────┘  └──────┬───────┘  └────────────────┘                │
└────────────────────────────────┬─────────────────────────────────────────┘
                                 │
                    ┌────────────┼────────────────┬───────────────┐
                    ▼            ▼                ▼               ▼
┌─────────────────────┐  ┌────────────┐  ┌──────────────┐  ┌──────────────┐
│ DOMAIN LAYER        │  │ JPA/       │  │ Redis        │  │ Kafka        │
│ (Models + Ports)    │  │ Hibernate  │  │ (Caching)    │  │ (Events)     │
│                     │  │ Adapters   │  │              │  │              │
│ UserAccount         │  │            │  │              │  │              │
│ Session             │  │ Repository │  │              │  │              │
│ Device              │  │ Impls      │  │              │  │              │
│ OtpCode             │  │            │  │              │  │              │
│ RefreshToken        │  │            │  │              │  │              │
│ Workspace           │  │            │  │              │  │              │
│ AuditEvent          │  │            │  │              │  │              │
└─────────────────────┘  └─────┬──────┘  └──────────────┘  └──────────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │    PostgreSQL DB     │
                    │                      │
                    │  users               │
                    │  sessions            │
                    │  devices             │
                    │  workspaces          │
                    │  workspace_members   │
                    │  roles               │
                    │  permissions         │
                    │  user_roles          │
                    │  role_permissions    │
                    │  refresh_tokens      │
                    │  otp_requests        │
                    │  audit_events        │
                    └──────────────────────┘
```

---

## Data Flow

### Login Flow

```
Client                    AuthController           IdentityService
  │                            │                        │
  │  POST /auth/login          │                        │
  │  {username, password}      │                        │
  │───────────────────────────►│                        │
  │                            │  loginWithSession()    │
  │                            │───────────────────────►│
  │                            │                        ├──► Find user by email
  │                            │                        ├──► Verify password (BCrypt)
  │                            │                        ├──► Validate account status
  │                            │                        ├──► Register/update device
  │                            │                        ├──► Create session
  │                            │                        ├──► Resolve permissions
  │                            │                        ├──► Generate access token
  │                            │                        ├──► Generate refresh token
  │                            │                        │
  │                            │◄───────────────────────┤
  │                            │  LoginResponse         │
  │◄───────────────────────────┤                        │
  │  200 {accessToken,         │                        │
  │  refreshToken, sessionId,  │                        │
  │  roles, permissions, ...}  │                        │
```

### Refresh Flow

```
Client                    AuthController           IdentityService
  │                            │                        │
  │  POST /auth/refresh        │                        │
  │  {refreshToken}            │                        │
  │───────────────────────────►│                        │
  │                            │  refreshToken()        │
  │                            │───────────────────────►│
  │                            │                        ├──► Validate as refresh token
  │                            │                        ├──► Extract subject
  │                            │                        ├──► Blacklist old token
  │                            │                        ├──► Generate new access token
  │                            │                        ├──► Generate new refresh token
  │                            │                        │
  │                            │◄───────────────────────┤
  │◄───────────────────────────┤                        │
  │  200 {accessToken,         │                        │
  │  refreshToken, ...}        │                        │
```

---

## Deployment

### Requirements

| Component | Technology |
|-----------|------------|
| Runtime | Java 21 (JVM) |
| Framework | Spring Boot 3.3.3 |
| Database | PostgreSQL 14+ |
| Cache | Redis 6+ |
| Messaging | Kafka 3+ |
| Build | Maven 3.9+ |
| Container | Docker (multi-stage build) |

### Build

```bash
mvn -f services/identity-service/pom.xml clean package
```

### Run

```bash
java -jar services/identity-service/target/identity-service-0.1.0-SNAPSHOT.jar
```

### Docker

```bash
docker build -f services/identity-service/Dockerfile -t sporekart/identity-service .
docker run -p 8080:8080 sporekart/identity-service
```

### Health Endpoint

```
GET /actuator/health
GET /actuator/health/liveness
GET /actuator/health/readiness
```

---

## Configuration Reference

### Application Configuration

```yaml
spring:
  application:
    name: identity-service
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        jdbc:
          time_zone: UTC
  flyway:
    enabled: true
    locations: classpath:db/migration
  redis:
    host: ${SPRING_REDIS_HOST:localhost}
    port: ${SPRING_REDIS_PORT:6379}
  kafka:
    bootstrap-servers: ${SPRING_KAFKA_BOOTSTRAP_SERVERS:localhost:9092}

app:
  security:
    jwt:
      secret: ${APP_JWT_SECRET}
      issuer: ${APP_JWT_ISSUER:https://identity.sporekart.example}
      expiration-minutes: ${APP_JWT_EXPIRATION_MINUTES:15}
      refresh-expiration-minutes: ${APP_JWT_REFRESH_EXPIRATION_MINUTES:10080}
      clock-skew-seconds: ${APP_JWT_CLOCK_SKEW_SECONDS:30}
      blacklist-size: ${APP_JWT_BLACKLIST_SIZE:10000}
    cors:
      allowed-origins: ${APP_CORS_ALLOWED_ORIGINS:http://localhost:3000}
```

### Environment Variables

| Variable | Default | Required | Description |
|----------|---------|----------|-------------|
| `SPRING_DATASOURCE_URL` | — | Yes | PostgreSQL JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | — | Yes | DB user |
| `SPRING_DATASOURCE_PASSWORD` | — | Yes | DB password |
| `SPRING_REDIS_HOST` | `localhost` | No | Redis host |
| `SPRING_REDIS_PORT` | `6379` | No | Redis port |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | No | Kafka brokers |
| `APP_JWT_SECRET` | — | Yes | HMAC key (min 32 bytes) |
| `APP_JWT_ISSUER` | `https://identity.sporekart.example` | No | JWT issuer |
| `APP_JWT_EXPIRATION_MINUTES` | `15` | No | Access token TTL |
| `APP_JWT_REFRESH_EXPIRATION_MINUTES` | `10080` | No | Refresh token TTL |
| `APP_JWT_CLOCK_SKEW_SECONDS` | `30` | No | Clock skew tolerance |
| `APP_JWT_BLACKLIST_SIZE` | `10000` | No | Blacklist max entries |
| `APP_CORS_ALLOWED_ORIGINS` | `http://localhost:3000` | No | CORS origins |

### Migrations

| Migration | Description |
|-----------|-------------|
| `V1__platform_foundation.sql` | Initial platform schema |
| `V2__iam_foundation.sql` | IAM roles, permissions, users |
| `V3__sprint16_uuid_backfill.sql` | UUID primary key migration |
| `V4__sprint16_pk_switch_identity.sql` | Primary key switch |
| `V5__sprint16_pk_cleanup.sql` | PK cleanup |
| `V6__enterprise_auth_platform.sql` | Sessions, devices, workspaces, enhanced OTP/refresh |

### Docker Compose

```yaml
services:
  identity-service:
    build: services/identity-service
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/sporekart
      SPRING_DATASOURCE_USERNAME: sporekart
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
      APP_JWT_SECRET: ${JWT_SECRET}
    depends_on:
      - postgres
      - redis
```
