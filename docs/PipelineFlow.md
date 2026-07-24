# Pipeline Flow

**SporeKart Enterprise Platform v2.0**  
**Document:** PipelineFlow.md  
**Last Updated:** 2026-07-24

---

## Complete Pipeline Flow

```
Developer Push
      │
      ▼
┌──────────────────────────────────────────────────────────────────┐
│                    CI PIPELINE (ci.yml)                           │
│                                                                  │
│  ┌──────────────┐   ┌──────────────────┐   ┌────────────────┐   │
│  │ Source       │   │ Dependency       │   │ Build & Test   │   │
│  │ Validation   │──▶│ Validation       │──▶│                │   │
│  │              │   │                  │   │ 16 services    │   │
│  │ Branch name  │   │ Maven enforcer   │   │ compile        │   │
│  │ Commits      │   │ Banned deps      │   │ Unit tests     │   │
│  │              │   │ Version check    │   │ Coverage       │   │
│  └──────────────┘   └──────────────────┘   └────────────────┘   │
│                                                    │            │
│                     ┌──────────────────┐           │            │
│                     │ Architecture     │◀──────────┘            │
│                     │ Validation       │                        │
│                     │                  │     ┌────────────────┐ │
│                     │ ArchUnit tests   │     │ Security Scan  │ │
│                     │ Circular deps    │     │                │ │
│                     │ Domain isolation │     │ Gitleaks       │ │
│                     └──────────────────┘     │ OWASP Dep Check│ │
│                                              └────────────────┘ │
│                                                    │            │
│                     ┌──────────────────┐           │            │
│                     │ Quality Gates    │◀──────────┘            │
│                     │                  │                        │
│                     │ All checks pass  │                        │
│                     │ ───────────────▶ │  Notify                │
│                     │ Merge PR         │                        │
│                     └──────────────────┘                        │
└──────────────────────────────────────────────────────────────────┘
                              │
                    Merge to sporetest
                              │
                              ▼
┌──────────────────────────────────────────────────────────────────┐
│                    CD PIPELINE (cd.yml)                           │
│                                                                  │
│  ┌──────────────┐   ┌──────────────────┐   ┌────────────────┐   │
│  │ Analyze      │──▶│ Build Artifacts  │──▶│ Containerize   │   │
│  │ Changes      │   │                  │   │                │   │
│  │ Changed      │   │ JAR packages     │   │ Docker build   │   │
│  │ services     │   │ Frontend build   │   │ ECR push       │   │
│  └──────────────┘   └──────────────────┘   └────────────────┘   │
│                                                    │            │
│                     ┌──────────────────┐           │            │
│                     │ Deploy Staging   │◀──────────┘            │
│                     │                  │                        │
│                     │ ECS update       │                        │
│                     │ Health check     │                        │
│                     │ Smoke tests      │                        │
│                     └──────────────────┘                        │
│                                                    │            │
│                     ┌──────────────────┐           │            │
│                     │ Approval Gate    │◀──────────┘            │
│                     │                  │                        │
│                     │ 2 approvers      │                        │
│                     │ 60 min timeout   │                        │
│                     └──────────────────┘                        │
│                                                    │            │
│                     ┌──────────────────┐           │            │
│                     │ Deploy           │◀──────────┘            │
│                     │ Production       │                        │
│                     │                  │                        │
│                     │ ECS update       │                        │
│                     │ Health check     │                        │
│                     │ Notify Slack     │                        │
│                     └──────────────────┘                        │
└──────────────────────────────────────────────────────────────────┘
```

---

## CI Pipeline Stages (ci.yml)

| Stage | Jobs | Parallel | Timeout | Artifacts |
|-------|------|----------|---------|-----------|
| Validation | source-validation, dependency-validation | ✅ Yes | 5 min | None |
| Build | build (all services) | ❌ Sequential | 20 min | JARs |
| Analysis | architecture-validation, security-scan | ✅ Yes | 10 min | Reports |
| Gate | quality-gates | ❌ Sequential | 2 min | Summary |
| Notify | notify | ❌ Sequential | 1 min | Slack |

## CD Pipeline Stages (cd.yml)

| Stage | Jobs | Parallel | Timeout | Artifacts |
|-------|------|----------|---------|-----------|
| Analysis | analyze-changes | ❌ Sequential | 2 min | Change list |
| Build | build-java-services, build-frontend | ✅ Yes | 15 min | JARs, dist |
| Container | containerize | ❌ Sequential | 10 min | Docker images |
| Stage | deploy-staging | ❌ Sequential | 10 min | Deployed env |
| Prod | deploy-production | ❌ Sequential | 15 min | Deployed env |
| Notify | notify | ❌ Sequential | 1 min | Slack |

---

## Pipeline Dependencies

```
PR open ──▶ CI Pipeline ──▶ Merge ──▶ CD Pipeline ──▶ Deploy
             │                          │
             ├── source-validation      ├── analyze-changes
             ├── dependency-validation  ├── build-java-services
             ├── build                  │   └── containerize
             ├── architecture-validation├── deploy-staging
             ├── security-scan          ├── [approval]
             └── quality-gates          └── deploy-production
```

---

## Pipeline Triggers

| Pipeline | Trigger | Branch Filter |
|----------|---------|---------------|
| CI | Pull request | sporetest, main |
| CI | Push | sporetest, main |
| CD | CI success | sporetest |
| CD | workflow_dispatch | Any |
| Release | workflow_dispatch | sporetest |
| Rollback | workflow_dispatch | Any |
| Quality Gates | Pull request | sporetest, main |

---

## Error Recovery

### CI Failure

1. Check workflow logs for specific failure
2. Fix code in feature branch
3. Push new commit — CI re-runs automatically

### CD Failure (Staging)

1. Rollback automatically to previous version
2. Investigate failure in CI build logs
3. Fix and redeploy through CI → CD flow

### CD Failure (Production)

1. Immediate rollback via Rollback workflow
2. Notify team via Slack
3. Root cause analysis within 1 hour
4. Fix in feature branch, CI gate, redeploy
