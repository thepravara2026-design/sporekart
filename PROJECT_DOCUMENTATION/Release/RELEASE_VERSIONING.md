# SporeKart Release Versioning Standard

**Version:** 1.0
**Owner:** Release Manager
**Status:** Approved
**Applies To:** All SporeKart releases (Development, QA, Bug Fix, Security, AI, HRMS, CRM, ERP, Mobile, Production)

---

## 1. Versioning Strategy

SporeKart follows **Semantic Versioning 2.0** with a Release Candidate suffix for pre-production releases.

```
vMAJOR.MINOR.PATCH[-rcN]
```

| Component | When to Increment | Example |
|---|---|---|
| **MAJOR** | Breaking API/contract changes, architecture overhaul, incompatible DB schema | `v2.0.0` |
| **MINOR** | New feature, non-breaking addition | `v1.1.0` |
| **PATCH** | Bug fix, security fix, performance fix, no new behavior | `v1.0.1` |
| **-rcN** | Pre-release candidate (N = 1, 2, 3...) | `v1.0.0-rc1` |

---

## 2. Release Types

| Type | Version Pattern | Branch Source | Branch Name | Tag Name |
|---|---|---|---|---|
| **Release Candidate** | `vMAJOR.MINOR.PATCH-rcN` | `sporetest` | `release/vMAJOR.MINOR-rcN` | `vMAJOR.MINOR.PATCH-rcN` |
| **Production Release** | `vMAJOR.MINOR.PATCH` | `main` | `main` | `vMAJOR.MINOR.PATCH` |
| **Hotfix Release** | `vMAJOR.MINOR.PATCH+1` | `main` | `hotfix/description` | `vMAJOR.MINOR.PATCH+1` |
| **QA Sprint** | N/A | `release/vMAJOR.MINOR-rcN` | `qa/qa-sprint-N` | N/A |
| **Regression** | N/A | `release/vMAJOR.MINOR-rcN` | `qa/regression-N` | N/A |

---

## 2. Release Candidate Lifecycle

```
sporetest (development)
  │
  └── release/v1.0-rc1  (cut from sporetest)
         │
         ├── qa/qa-sprint-1
         ├── bugfix/QA-0001
         ├── security/QA-0002
         ├── qa/regression-1
         │
         └── (if unstable) → release/v1.0-rc2 (cut from sporetest, replaces rc1)
```

- Each RC is a frozen snapshot. No new features.
- If RC is rejected, cut `release/v1.0-rc(N+1)` from `sporetest`.
- Only one active RC at a time.

---

## 3. Tag Naming

| Type | Tag Pattern | Example |
|---|---|---|
| Release Candidate | `vMAJOR.MINOR.PATCH-rcN` | `v1.0.0-rc1` |
| Production Release | `vMAJOR.MINOR.PATCH` | `v1.0.0` |
| Hotfix | `vMAJOR.MINOR.PATCH` (incremented) | `v1.0.1` |
| Broken State (rollback) | `broken/vMAJOR.MINOR-<timestamp>` | `broken/v1.0-20260716T120000Z` |

---

## 3. Branch-to-Version Mapping

| Branch Pattern | Version Example | Tag Example |
|---|---|---|
| `release/v1.0-rc1` | `v1.0.0-rc1` | `v1.0.0-rc1` |
| `main` (after merge) | `v1.0.0` | `v1.0.0` |
| `hotfix/login-fix` | `v1.0.1` | `v1.0.1` |
| `release/v1.1-rc1` | `v1.1.0-rc1` | `v1.1.0-rc1` |

---

## 4. Release Candidate Lifecycle

```
sporetest
  │
  └── release/v1.0-rc1  (cut, frozen, tagged v1.0.0-rc1)
         │
         ├── qa/qa-sprint-1
         ├── bugfix/QA-0001
         ├── qa/regression-1
         │
         └── (if unstable) → release/v1.0-rc2 (cut from sporetest, replaces rc1)
```

- Only one active RC at a time.
- If RC is rejected, increment `rcN` and re-cut from `sporetest`.
- When RC passes all gates, merge to `main` and tag `vMAJOR.MINOR.PATCH`.

---

## 5. Production Release Lifecycle

```
sporetest
  │
  └── release/v1.0-rc1  (frozen, QA begins)
         │
         ├── qa/qa-sprint-1
         ├── bugfix/QA-0001
         ├── qa/regression-1
         │
         └── (QA passes) → merge to main → tag v1.0.0
```

- Only one active RC at a time.
- If RC is rejected, increment `rcN` and re-cut from `sporetest`.
- When RC passes all gates, merge to `main` and tag.

---

## 6. Rollback Versioning

| Scenario | Action | Tag |
|---|---|---|
| Bug fix rollback | Revert commit, re-deploy | Same version, new patch |
| RC rollback | Abandon RC, cut new RC | `v1.0.0-rc2` |
| Production rollback | Deploy previous tag | `v0.9.0` (previous) |
| Broken state marker | Tag for forensic reference | `broken/v1.0.0-20260716T120000Z` |

---

## 7. Tag Naming Rules

- Tags are immutable. Never delete or overwrite a tag.
- Every production release gets a signed tag.
- Every RC gets a tag for traceability.
- Broken state tags are for reference only — never deployed.

---

## 8. Version Lifecycle Diagram

```
v1.0.0-rc1  (cut from sporetest, QA begins)
  │
  ├── QA passes → merge to main → tag v1.0.0
  │
  └── QA fails → cut v1.0.0-rc2 from sporetest

v1.0.0 (production)
  │
  └── hotfix needed → cut hotfix from main → tag v1.0.1
```

---

## 9. Rollback Versioning

| Scenario | Action | New Version |
|---|---|---|
| RC rollback | Abandon RC, cut new RC from sporetest | `v1.0.0-rc2` |
| Production rollback | Revert to previous tag | `v0.9.0` (previous) |
| Hotfix rollback | Revert hotfix, deploy new hotfix | `v1.0.2` |

---

## 10. Tag Rules

- Tags are immutable. Never delete or overwrite.
- Every production release gets a signed tag.
- Every RC gets a tag for traceability.
- Broken state tags are for reference only — never deployed.

---

## 11. Branch-to-Version Mapping

| Branch | Version During Development | Tag on Completion |
|---|---|---|
| `sporetest` | Unversioned (pre-release) | N/A |
| `release/v1.0-rc1` | `v1.0.0-rc1` | `v1.0.0-rc1` |
| `main` | `v1.0.0` | `v1.0.0` |
| `hotfix/*` | `v1.0.1` | `v1.0.1` |
