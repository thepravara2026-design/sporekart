# CI/CD Architecture

**SporeKart Enterprise Platform v2.0**  
**Document:** CICDArchitecture.md  
**Last Updated:** 2026-07-24

---

## Overview

The SporeKart CI/CD platform provides fully automated validation, build, test, security, and deployment across all 17 microservices and frontend applications. Every commit is validated before merge. Every deployment is reproducible and traceable.

---

## Pipeline Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                      SOURCE CONTROL                              │
│  Branch naming │ Commit conventions │ Protected branches         │
└───────────────────────────┬─────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                    CI — CONTINUOUS INTEGRATION                    │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐        │
│  │ Source   │→ │ Deps     │→ │ Build    │→ │ Unit     │        │
│  │ Validate │  │ Validate │  │          │  │ Tests    │        │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘        │
│                     ┌──────────┐  ┌──────────┐                  │
│                     │ Arch     │  │ Security │                  │
│                     │ Validate │  │ Scan     │                  │
│                     └──────────┘  └──────────┘                  │
│                     ┌──────────┐                                │
│                     │ Quality  │→ Notify                        │
│                     │ Gates    │                                │
│                     └──────────┘                                │
└───────────────────────────┬─────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│              CD — CONTINUOUS DELIVERY                            │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐        │
│  │ Analyze  │→ │ Build    │→ │ Container│→ │ Deploy   │        │
│  │ Changes  │  │ Artifacts│  │ Build    │  │ Staging  │        │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘        │
│                                   ┌──────────┐                  │
│                                   │ Approval  │→ Deploy Prod    │
│                                   │ Gate      │                  │
│                                   └──────────┘                  │
└─────────────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                    RELEASE MANAGEMENT                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐        │
│  │ Version  │→ │ Change   │→ │ Git Tag  │→ │ GitHub   │        │
│  │ Bump     │  │ log      │  │          │  │ Release  │        │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘        │
└─────────────────────────────────────────────────────────────────┘
```

---

## Workflow Files

| Workflow | File | Trigger | Purpose |
|----------|------|---------|---------|
| CI Pipeline | `.github/workflows/ci.yml` | PR/push to sporetest/main | Validate, build, test, scan every change |
| CD Pipeline | `.github/workflows/cd.yml` | CI success + manual | Build containers, deploy to staging/prod |
| Release | `.github/workflows/release.yml` | Manual | Version bump, changelog, git tag, GitHub Release |
| Rollback | `.github/workflows/rollback.yml` | Manual | Deterministic rollback to any version |
| Quality Gates | `.github/workflows/quality-gates.yml` | PR + manual | Architecture, coverage, static analysis, migrations |

---

## Quality Gates

Every PR must pass:

1. **Source Validation**: Branch naming, commit message convention
2. **Dependency Validation**: No banned dependencies, version consistency
3. **Build**: All 17 services compile with zero errors
4. **Unit Tests**: All tests pass (JUnit 5 + AssertJ)
5. **Architecture**: No circular dependencies, domain isolation, clean architecture
6. **Security**: No secrets in code, no critical vulnerabilities (CVSS < 7)
7. **Migration Integrity**: Flyway migration ordering, no gaps

---

## Artifact Flow

```
Source Code
    ↓
Maven Build → JAR artifacts (versioned, immutable)
    ↓
Docker Build → Container images (tagged with git SHA + semver)
    ↓
ECR Registry → Immutable image storage
    ↓
ECS Deploy → Staging → Production
```

---

## Performance Targets

| Stage | Target | Instrumentation |
|-------|--------|----------------|
| Full CI Pipeline | <15 min | GitHub Actions duration tracking |
| Build + Unit Tests | <8 min | Maven build timestamps |
| Container Build | <5 min | Docker build timing |
| Deploy to Staging | <10 min | ECS service stability |
| Deploy to Production | <15 min (incl. approval) | ECS service stability |

---

## Tools

- **CI/CD**: GitHub Actions
- **Build**: Maven 3.9+ with Java 21
- **Container**: Docker, Amazon ECR
- **Orchestration**: Amazon ECS (Fargate)
- **Static Analysis**: PMD, SpotBugs, Checkstyle
- **Security**: Gitleaks (secrets), OWASP Dependency Check, SAST
- **Quality**: JaCoCo (coverage), ArchUnit (architecture), Maven Enforcer
- **Notifications**: Slack (deployments), GitHub Checks (PR status)
