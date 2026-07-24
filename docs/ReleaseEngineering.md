# Release Engineering

**SporeKart Enterprise Platform v2.0**  
**Document:** ReleaseEngineering.md  
**Last Updated:** 2026-07-24

---

## Versioning Strategy

SporeKart follows **Semantic Versioning 2.0.0**:

```
MAJOR.MINOR.PATCH
  │      │      └─ Bug fixes, patches (backward-compatible)
  │      └──────── New features (backward-compatible)
  └─────────────── Breaking changes (not backward-compatible)
```

Current version: `2.0.0-SNAPSHOT`

### Version Sources of Truth

- **Root `pom.xml`**: `project.version` — definitive source
- **All service POMs**: Inherit or reference root version
- **Docker images**: Tagged with `vMAJOR.MINOR.PATCH` + `git-sha`
- **Git tags**: `vMAJOR.MINOR.PATCH` format

---

## Release Process

### 1. Trigger Release

Use the GitHub Actions release workflow:
- Navigate to Actions → Release — Enterprise Release Orchestration
- Select release type: `major`, `minor`, or `patch`
- Optionally run as dry run first

### 2. Automated Steps

| Step | Action | Responsible |
|------|--------|-------------|
| 1 | Validate current branch is `sporetest` | Workflow |
| 2 | Calculate new version from latest tag | Workflow |
| 3 | Generate changelog from conventional commits | Workflow |
| 4 | Update version in all POM files | Workflow |
| 5 | Create release branch `release/vX.Y.Z` | Workflow |
| 6 | Create git tag `vX.Y.Z` | Workflow |
| 7 | Build all artifacts with `-Prelease` profile | Workflow |
| 8 | Upload JARs as release artifacts | Workflow |
| 9 | Create GitHub Release with changelog | Workflow |

### 3. Manual Steps (Approval)

| Step | Approver | Action |
|------|----------|--------|
| 1 | Engineering Lead | Review release notes |
| 2 | QA Lead | Verify smoke tests pass |
| 3 | DevOps Lead | Approve deployment window |

### 4. Post-Release

| Action | Details |
|--------|---------|
| Deploy to staging | CD workflow picks up new tag |
| Deploy to production | Requires manual approval in CD workflow |
| Update sporetest branch | Merge release branch back to sporetest |
| Announce release | Slack #releases channel |

---

## Branch Strategy

```
main (production-ready)
  │
  └── sporetest (integration branch)
        │
        ├── feature/* (new features)
        ├── bugfix/* (bug fixes)
        ├── hotfix/* (urgent production fixes)
        └── release/* (release preparation)
```

### Branch Naming Rules

| Pattern | Example | Source | Target |
|---------|---------|--------|--------|
| `feature/*` | `feature/p13.5-cicd-release-platform` | sporetest | sporetest |
| `bugfix/*` | `bugfix/fix-login-redirect` | sporetest | sporetest |
| `hotfix/*` | `hotfix/critical-security-patch` | main | main + sporetest |
| `release/*` | `release/v2.1.0` | sporetest | main + sporetest |

---

## Commit Message Convention

Uses [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

### Types

| Type | Usage | Example |
|------|-------|---------|
| `feat` | New feature | `feat(cart): add bulk add endpoint` |
| `fix` | Bug fix | `fix(auth): handle expired token gracefully` |
| `refactor` | Code change without feature/fix | `refactor(core): extract validation logic` |
| `test` | Adding/modifying tests | `test(platform): add archunit rules` |
| `docs` | Documentation | `docs(ci): document pipeline architecture` |
| `ci` | CI/CD changes | `ci(actions): add security scan workflow` |
| `build` | Build system changes | `build(maven): add parent pom` |
| `chore` | Maintenance | `chore(release): bump version to 2.1.0` |

---

## Artifact Naming Convention

| Artifact | Pattern | Example |
|----------|---------|---------|
| JAR | `{artifactId}-{version}.jar` | `cart-service-2.1.0.jar` |
| Docker image | `{registry}/{repo}:{tag}` | `123456.dkr.ecr.us-east-1.amazonaws.com/sporekart-api:v2.1.0` |
| Git tag | `v{version}` | `v2.1.0` |
| Release artifact | `release-{version}` | `release-2.1.0` |

---

## Rollback Compatibility

Every release must maintain backward compatibility for at least one minor version:

- **Database**: Schema changes must be backward-compatible (additive only in minor/patch)
- **API**: Endpoint contracts must not break (additive only)
- **Events**: Event schemas must support version headers
- **Configuration**: Config keys must not be removed without deprecation notice
