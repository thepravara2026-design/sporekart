# Artifact Management

**SporeKart Enterprise Platform v2.0**  
**Document:** ArtifactManagement.md  
**Last Updated:** 2026-07-24

---

## Artifact Overview

Every build produces immutable, versioned artifacts. Each artifact carries metadata enabling full traceability from source commit to production deployment.

---

## Artifact Types

| Type | Format | Storage | Retention | Immutable |
|------|--------|---------|-----------|-----------|
| JAR | `{artifact}-{version}.jar` | GitHub Actions Artifacts + ECR | 90 days | ✅ |
| Docker Image | `{registry}/{repo}:{tag}` | Amazon ECR | Indefinite | ✅ |
| Frontend Build | `dist/` (static files) | S3 + CloudFront | 90 days | ✅ |
| Migration Scripts | `V{num}__{desc}.sql` | Git (versioned) | Indefinite | ✅ |
| Release Notes | Markdown | GitHub Releases | Indefinite | ✅ |
| Test Reports | XML + HTML | GitHub Actions Artifacts | 30 days | ✅ |
| Coverage Reports | HTML + XML | GitHub Actions Artifacts | 30 days | ✅ |
| Security Reports | HTML + JSON | GitHub Actions Artifacts | 90 days | ✅ |

---

## Artifact Naming

### JAR Artifacts

```
{artifactId}-{version}.jar
  │           │
  │           └─ Semver from pom.xml
  └───────────── Maven artifact ID

Examples:
  cart-service-2.1.0.jar
  gateway-service-2.1.0.jar
  shared-platform-2.1.0.jar
```

### Docker Images

```
{account}.dkr.ecr.{region}.amazonaws.com/{repository}:{tag}

  repository options:
    sporekart-api         - Java backend services
    sporekart-web-app     - Frontend application
    sporekart-worker      - Background workers
    sporekart-ai-service  - AI platform

  tag options:
    v2.1.0                - Semantic version
    sha-{commit-sha}      - Git commit SHA (7 chars)
    latest                - Latest stable
    staging               - Latest staging deployment
    pr-{number}           - PR preview
```

---

## Artifact Metadata

Every JAR includes embedded metadata:

```properties
# META-INF/MANIFEST.MF
Implementation-Title: SporeKart Cart Service
Implementation-Version: 2.1.0
Implementation-Vendor: SporeKart
Build-Time: 2026-07-24T11:30:00Z

# git.properties
git.commit.id=abc123def456
git.commit.time=2026-07-24T10:00:00Z
git.branch=feature/p13.5-cicd-release-platform
```

Access at runtime:

```bash
# Build info
curl -s https://sporekart.com/actuator/info | jq

# Git info
curl -s https://sporekart.com/actuator/info | jq '.git'
```

---

## Artifact Flow Pipeline

```
┌──────────┐   ┌──────────┐   ┌──────────┐   ┌──────────┐
│  Source  │──▶│  Build   │──▶│ Package  │──▶│  Store   │
│  Commit  │   │          │   │          │   │          │
└──────────┘   └──────────┘   └──────────┘   └──────────┘
                               │              │
                               │ JAR          ├─ GitHub Artifacts
                               │ Dockerfile   ├─ ECR (Docker)
                               │ Frontend     └─ S3 (Frontend)
                               │
                               ▼
                         ┌──────────┐
                         │  Deploy  │
                         │          │
                         │ ECS pull │
                         │ image    │
                         │ from ECR │
                         └──────────┘
```

---

## Versioning

### Snapshot Versions

```
{version}-SNAPSHOT
  Example: 2.1.0-SNAPSHOT
  Usage: Development branches
  Mutability: Mutable (overwritable)
  Retention: Replaced on next build
```

### Release Versions

```
{version}
  Example: 2.1.0
  Usage: Releases and tags
  Mutability: IMMUTABLE
  Retention: Indefinite
```

---

## Artifact Cleanup Policy

| Artifact | Location | Retention | Cleanup Trigger |
|----------|----------|-----------|-----------------|
| Snapshot JARs | GitHub Actions | 90 days | Automated (GitHub) |
| Release JARs | GitHub Releases | Indefinite | Manual |
| Docker images (tagged) | ECR | Indefinite | Manual |
| Docker images (untagged) | ECR | 14 days | ECR lifecycle policy |
| Frontend builds | S3 | 90 days | S3 lifecycle policy |
| Build logs | GitHub Actions | 400 days | Automated (GitHub) |
| Test reports | GitHub Actions | 90 days | Automated (GitHub) |

---

## Artifact Verification

Every artifact is verified before deployment:

### JAR Verification

```bash
# Verify JAR integrity
jar -tf cart-service-2.1.0.jar | head -20

# Check manifest
unzip -p cart-service-2.1.0.jar META-INF/MANIFEST.MF

# Verify Spring Boot executable
java -jar cart-service-2.1.0.jar --version 2>&1 || true
```

### Docker Image Verification

```bash
# Check image size
docker images sporekart-api:v2.1.0 --format "{{.Size}}"

# Verify HEALTHCHECK
docker inspect sporekart-api:v2.1.0 | jq '.[0].Config.Healthcheck'

# Security scan
docker scout quickview sporekart-api:v2.1.0
```

---

## Release Artifact Bundle

Each release produces:

```
release-v2.1.0/
├── jars/
│   ├── admin-service-2.1.0.jar
│   ├── analytics-service-2.1.0.jar
│   ├── cart-service-2.1.0.jar
│   ├── catalog-service-2.1.0.jar
│   ├── ... (all 17 services)
│   ├── gateway-service-2.1.0.jar
│   └── shared-platform-2.1.0.jar
├── docker/
│   ├── sporekart-api:v2.1.0
│   ├── sporekart-web-app:v2.1.0
│   └── Dockerfile.*
├── migrations/  (all Flyway scripts at this version)
├── release-notes.md
└── checksums.sha256
```
