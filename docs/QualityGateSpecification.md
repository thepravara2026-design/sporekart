# Quality Gate Specification

**SporeKart Enterprise Platform v2.0**  
**Document:** QualityGateSpecification.md  
**Last Updated:** 2026-07-24

---

## Overview

Quality Gates are automated enforcement points in the CI/CD pipeline that prevent merging or deploying code that does not meet defined quality thresholds.

---

## Gate Definitions

### Gate 1: Source Validation

| Check | Rule | Failure Action |
|-------|------|----------------|
| Branch naming | Must match `feature/*`, `bugfix/*`, `hotfix/*`, `release/*` | PR blocked |
| Commit messages | Must follow Conventional Commits format | Warning logged |
| File encoding | All files must be UTF-8 | Warning logged |
| Line endings | LF line endings required | Warning logged |

### Gate 2: Build Integrity

| Check | Rule | Failure Action |
|-------|------|----------------|
| Compilation | Zero errors across all 17 services | Pipeline fails |
| Dependency convergence | No version conflicts | Pipeline fails |
| Banned dependencies | No log4j, commons-logging, etc. | Pipeline fails |
| Java version | Java 21 required | Pipeline fails |

### Gate 3: Test Execution

| Check | Rule | Failure Action |
|-------|------|----------------|
| Unit tests | Zero failures | Pipeline fails |
| Integration tests | Zero failures | Pipeline fails |
| Test coverage | >80% instruction, >70% branch | Warning (soft gate) |

### Gate 4: Architecture Validation

| Check | Rule | Failure Action |
|-------|------|----------------|
| Circular dependencies | Zero circular dependencies | Pipeline fails |
| Domain isolation | `domain/` must not import Spring/JPA/web | Architecture test fails |
| Package layering | No controller → repository direct calls | Architecture test fails |
| Module boundaries | No cross-service source dependencies | Architecture test fails |

### Gate 5: Security

| Check | Rule | Failure Action |
|-------|------|----------------|
| Secrets in code | Zero secrets detected by gitleaks | Pipeline fails |
| Dependency vulnerabilities | Zero CRITICAL/HIGH (CVSS >= 7) | Pipeline fails |
| Hardcoded credentials | No passwords in config files | Pipeline fails |
| .env files | No `.env` files tracked in git | Warning logged |

### Gate 6: Migration Integrity

| Check | Rule | Failure Action |
|-------|------|----------------|
| Migration ordering | V1, V2, V3... no gaps | Pipeline fails |
| Migration naming | Must follow `V{number}__{description}.sql` | Pipeline fails |
| Duplicate versions | No duplicate version numbers | Pipeline fails |

### Gate 7: Container Quality

| Check | Rule | Failure Action |
|-------|------|----------------|
| Docker build | Zero build failures | Pipeline fails |
| Image size | <500MB for backend, <200MB for frontend | Warning logged |
| Health check | HEALTHCHECK instruction present | Warning logged |
| Non-root user | Container must not run as root | Pipeline fails |

---

## Quality Gate Pipeline Order

```
PR Created
  │
  ├── Gate 1: Source Validation ──── FAST (<30s)
  │
  ├── Gate 2: Build Integrity ────── BUILD (<5min)
  │
  ├── Gate 3: Test Execution ─────── BUILD (<10min)
  │
  ├── Gate 4: Architecture ───────── BUILD (<3min)
  │
  ├── Gate 5: Security ───────────── BUILD (<5min)
  │
  ├── Gate 6: Migration ──────────── FAST (<1min)
  │
  └── Gate 7: Container ──────────── BUILD (<5min)
```

---

## Gate Configuration

### Soft Gates (Warning Only)

Gates that log warnings but do not block:

- Test coverage < 80%
- Code style violations (Checkstyle)
- PMD warnings
- Large image size
- .env files tracked in git

### Hard Gates (Blocking)

Gates that fail the pipeline:

- Compile errors
- Test failures
- Security vulnerabilities (CVSS >= 7)
- Secrets in code
- Circular dependencies
- Architecture violations
- Container build failures

---

## Quality Dashboard

Metrics tracked for each merge to sporetest and main:

| Metric | Target | Source |
|--------|--------|--------|
| Build pass rate | >99% | GitHub Actions |
| Test pass rate | 100% | Surefire reports |
| Coverage (instruction) | >80% | JaCoCo reports |
| Coverage (branch) | >70% | JaCoCo reports |
| Critical vulnerabilities | 0 | OWASP Dependency Check |
| Architecture violations | 0 | ArchUnit |
| Build time (p50) | <10 min | GitHub Actions |
| Deployment time (p50) | <10 min | GitHub Actions |

---

## Exception Process

If a quality gate must be bypassed:

1. Create an issue with label `quality-gate-exception`
2. Include: gate name, reason, risk assessment, remediation plan
3. Get approval from Engineering Lead + Architecture Review Board
4. Reference issue in PR description with `#quality-exception-{num}`
5. Exception expires after 30 days — must be resolved or renewed
