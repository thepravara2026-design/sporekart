# Container Standards

**SporeKart Enterprise Platform v2.0**  
**Document:** ContainerStandards.md  
**Last Updated:** 2026-07-24

---

## Base Image Policy

| Service Type | Base Image | Justification |
|-------------|------------|---------------|
| Java (Spring Boot) | `eclipse-temurin:21-jre-alpine` | Small footprint, security patched, LTS |
| Java (build stage) | `eclipse-temurin:21-jdk-alpine` | Full JDK for compilation only |
| Node.js (frontend) | `node:20-alpine` | LTS, minimal |
| Web server (nginx) | `nginx:1.27-alpine` | Latest stable, minimal |
| CI/CD runner | `eclipse-temurin:21-jdk-alpine` | JDK required for Maven build |

---

## Multi-Stage Build Pattern

All production images MUST use multi-stage builds:

```
┌──────────────────────────────────────────────────────┐
│                   STAGE 1: BUILDER                    │
│  Base: eclipse-temurin:21-jdk-alpine                  │
│  ├── Install dependencies                              │
│  ├── Compile source (mvn package)                     │
│  ├── Run tests (mvn test)                             │
│  └── Output: JAR file                                 │
└──────────────────────────────────────────────────────┘
                          │
                          ▼
┌──────────────────────────────────────────────────────┐
│                   STAGE 2: RUNTIME                    │
│  Base: eclipse-temurin:21-jre-alpine                  │
│  ├── Copy JAR from builder stage                      │
│  ├── Create non-root user (sporekart)                 │
│  ├── Set HEALTHCHECK                                  │
│  ├── Set resource labels                              │
│  └── CMD: java -jar app.jar                           │
└──────────────────────────────────────────────────────┘
```

---

## Dockerfile Template (Java)

```dockerfile
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build
COPY . .
RUN mvn package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine AS runtime
RUN addgroup -S sporekart && adduser -S sporekart -G sporekart && \
    apk add --no-cache wget curl
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
RUN chown -R sporekart:sporekart /app
USER sporekart
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
HEALTHCHECK --interval=30s --timeout=10s --retries=3 --start-period=30s \
  CMD wget -qO- http://localhost:8080/actuator/health/liveness || exit 1
LABEL com.sporekart.service="${SERVICE_NAME}" \
      com.sporekart.version="2.0.0" \
      com.sporekart.managed-by="terraform"
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

---

## Non-Negotiable Rules

| Rule | Enforcement | Rationale |
|------|-------------|-----------|
| ✅ Multi-stage build | CI pipeline check | Separate build deps from runtime |
| ✅ No root user | Dockerfile check | Defense-in-depth; container breakout prevention |
| ✅ HEALTHCHECK | Dockerfile check | Orchestrator needs health signals |
| ✅ Specific base image tag | Policy | `alpine` (no tag) = unpredictable |
| ✅ Pin system packages | Dockerfile check | No `apk upgrade`; use specific versions |
| ✅ .dockerignore | CI check | Exclude node_modules, target, .git |
| ❌ No `latest` tag in prod | CI gate | Use semantic version or git SHA |
| ❌ No `:slim` variants | Policy | Use `:jre-alpine` for consistency |
| ❌ No `sudo` or `setuid` | CI check | Security risk |
| ❌ No hardcoded secrets | CI scan | Use Secrets Manager / Vault |

---

## Image Optimization

| Technique | Before | After | Savings |
|-----------|--------|-------|---------|
| Multi-stage build (no JDK in runtime) | 450MB | 180MB | 60% |
| Alpine base (vs Ubuntu) | 350MB | 180MB | 49% |
| JRE-only (vs JDK) | 450MB | 180MB | 60% |
| Layer squashing | 200MB | 150MB | 25% |
| .dockerignore | 400MB+ | 180MB | 55%+ |

---

## .dockerignore Template

```
node_modules/
.git/
.gitignore
*.md
target/
build/
dist/
.cache/
.idea/
.vscode/
*.log
.env
.env.*
docker-compose*.yml
apache-maven-*/
```

---

## Image Tagging Convention

```
{registry}/{repository}:{tag}

Tags:
- v2.1.0          — Semantic version (release)
- sha-a1b2c3d     — Git commit SHA (CI)
- staging         — Latest staging deployment
- production      — Latest production deployment
- pr-123          — PR preview build

Examples:
123456789012.dkr.ecr.us-east-1.amazonaws.com/sporekart-gateway:v2.1.0
123456789012.dkr.ecr.us-east-1.amazonaws.com/sporekart-api:sha-a1b2c3d
```

---

## Image Security

| Check | Tool | Gate |
|-------|------|------|
| Base image CVE scan | Docker Scout / Trivy | Critical: fail build |
| Dependency CVE scan | OWASP Dependency Check | High: fail build |
| Secret leak detection | Gitleaks | Any: fail build |
| Non-root user check | Dockerfile lint | Missing: fail build |
| HEALTHCHECK presence | Dockerfile lint | Missing: warning |
| Image size | CI pipeline | >500MB: warning |
