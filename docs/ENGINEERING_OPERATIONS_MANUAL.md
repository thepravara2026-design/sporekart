# SporeKart Engineering Operations Manual

**Version:** 1.0
**Status:** DRAFT — Awaiting Approval
**Owner:** Principal Software Architect / Release Manager
**Applies To:** All SporeKart Sprints (Development, QA, Bug Fix, Regression, Security, AI, HRMS, CRM, ERP, Production Releases)
**Classification:** Internal Engineering Standard

---

## Document Purpose

This manual defines the permanent, enterprise-grade Git workflow, QA process, commit standards, review gates, evidence requirements, release freeze policy, build verification, documentation structure, rollback strategy, and release checklist for the SporeKart platform.

It is the single source of truth for how every future code change is planned, implemented, reviewed, released, and rolled back. No sprint may deviate from this manual without an approved Architecture Decision Record (ADR).

---

# SECTION 1 — Current Strategy Validation

## 1.1 Current Structure (as inspected)

```
sporetest (Development / Integration)
  ↓
release/v1.0-rc1 (Release Candidate)
  ↓
qa/* (QA sprints)
  ↓
bugfix/* (QA-driven fixes)
  ↓
regression
  ↓
signoff
```

## 1.2 Assessment

| Aspect | Verdict | Rationale |
|---|---|---|
| `sporetest` as development branch | **ACCEPT** | Confirmed as remote default branch; safe to treat as integration trunk. |
| Single RC branch per release | **ACCEPT** | Correct for a frozen QA snapshot. |
| `qa/*` for QA sprints | **ACCEPT** | Isolates QA execution from development. |
| `bugfix/*` for fixes | **ACCEPT** | Traceable per-defect branch naming. |
| `regression` as free-floating concept | **IMPROVE** | Must be a branch (`qa/regression-N`) or a phase, not an ambiguous node. |
| `signoff` | **IMPROVE** | Must be a documented gate, not a branch type. |
| No `main`/production branch | **IMPROVE** | Add `main` as the production-stable trunk for released code. |
| No feature-branch workflow | **IMPROVE** | Long-lived `sporetest` direct commits do not scale. Add `feature/*`. |
| No hotfix path | **IMPROVE** | Production incidents need `hotfix/*`. |
| No security/performance/AI branches | **IMPROVE** | Enterprise scope requires dedicated prefixes (Section 3). |

## 1.3 Recommended Target Topology

```
main                         (production-stable, protected)
  ↑ (merge via release only)
sporetest                    (development / integration trunk)
  ├── feature/*              (sprint feature work)
  ├── release/vX.Y-rcN       (frozen QA candidate, cut from sporetest)
  │     ├── qa/qa-sprint-N
  │     ├── qa/regression-N
  │     ├── bugfix/QA-NNNN
  │     ├── security/QA-NNNN
  │     └── perf/QA-NNNN
  ├── hotfix/*               (production emergency fixes, cut from main)
  └── experiment/*           (research / spike work, non-shipping)
```

**Improvement Summary:** The proposed model adds `main` (stable), `feature/*`, `hotfix/*`, `security/*`, `perf/*`, and converts `regression`/`signoff` into explicit branches/phases. This satisfies enterprise branching standards and scales across all future sprint types.

---

# SECTION 2 — Engineering Workflow (Official SOP)

Every future sprint — regardless of type (Development, QA, Bug Fix, Regression, Security, AI, HRMS, CRM, ERP, Production) — follows this exact lifecycle.

```
Planning
   ↓
Repository Inspection
   ↓
Branch Creation
   ↓
Implementation
   ↓
Self Validation
   ↓
Evidence Generation
   ↓
STOP  ← No commit/push/merge beyond this point
   ↓
Manual Review
   ↓
Approval
   ↓
Commit
   ↓
Push
   ↓
Merge
   ↓
Regression
   ↓
Sign Off
   ↓
Next Sprint
```

## 2.1 Phase Definitions

| Phase | Owner | Exit Criteria |
|---|---|---|
| **Planning** | Product + Architect | Sprint scope, acceptance criteria, branch name, evidence plan. |
| **Repository Inspection** | Engineer | `git status` clean, correct base branch, HEAD verified. |
| **Branch Creation** | Engineer | Branch created from approved base (per Section 3). |
| **Implementation** | Engineer | Code complete, no secrets, no app-code violations. |
| **Self Validation** | Engineer | Build passes, typecheck passes, unit tests pass locally. |
| **Evidence Generation** | Engineer | All Section 6 artifacts produced and attached. |
| **STOP** | System/Process | Hard gate. No commit/push/merge until Manual Review passes. |
| **Manual Review** | Human Reviewer | Diff reviewed against acceptance criteria. |
| **Approval** | Human Reviewer / Release Mgr | Explicit sign-off recorded. |
| **Commit** | Engineer | Conventional commit message (Section 4). |
| **Push** | Engineer | Pushed to `origin`, branch protected rules enforced. |
| **Merge** | Release Mgr / CI | Via PR only; required reviewers + green checks. |
| **Regression** | QA | Full regression suite green on merged code. |
| **Sign Off** | QA + Release Mgr | Formal release-readiness sign-off. |
| **Next Sprint** | All | Retrospective logged; backlog updated. |

> **Rule:** The `STOP` gate is mandatory. Automation must refuse to commit, push, or merge before Manual Review + Approval are recorded.

---

# SECTION 3 — Branch Naming Standards

| Prefix | Use When | Base Branch | Example |
|---|---|---|---|
| `feature/` | New functionality or sprint deliverable | `sporetest` | `feature/checkout-redesign` |
| `bugfix/` | Defect fix from QA or production | `release/*` or `sporetest` | `bugfix/QA-1042` |
| `hotfix/` | Emergency production fix | `main` | `hotfix/login-500` |
| `qa/` | QA execution / regression sprints | `release/*` | `qa/qa-sprint-3`, `qa/regression-2` |
| `security/` | Security findings, CVE remediation | `release/*` or `sporetest` | `security/CVE-2026-1234` |
| `performance/` | Performance/load improvements | `release/*` or `sporetest` | `perf/api-latency` |
| `release/` | Frozen release candidate | `sporetest` | `release/v1.0-rc1` |
| `experiment/` | Spike / research, non-shipping | `sporetest` | `experiment/vector-search` |
| `research/` | Architecture investigation | `sporetest` | `research/graphql-migration` |
| `docs/` | Documentation-only changes | `sporetest` | `docs/api-standards` |
| `refactor/` | Internal restructure, no behavior change | `sporetest` | `refactor/auth-module` |
| `chore/` | Tooling, config, CI, deps hygiene | `sporetest` | `chore/ci-cache` |

**Naming Rules:**
- Lowercase, hyphen-separated.
- Ticket/defect ID mandatory for `bugfix/`, `security/`, `qa/`: `bugfix/QA-1042`.
- Version format for `release/`: `release/vMAJOR.MINOR-rcN`.
- No spaces, no underscores, no dates in branch names.

---

# SECTION 4 — Commit Standards

All commits use **Conventional Commits** with SporeKart domain prefixes.

| Type | Meaning | Example |
|---|---|---|
| `feat:` | New feature | `feat: add bulk certificate export to admin` |
| `fix:` | Bug fix | `fix: resolve 500 on login when session expires` |
| `test:` | Test additions/changes | `test: add Playwright checkout regression` |
| `perf:` | Performance improvement | `perf: reduce catalog query latency by 40%` |
| `security:` | Security fix/hardening | `security: patch JWT validation bypass` |
| `docs:` | Documentation | `docs: update Git strategy manual` |
| `refactor:` | No-behavior refactor | `refactor: extract permission provider hook` |
| `chore:` | Tooling/CI/config | `chore: cache node_modules in CI` |
| `ci:` | CI pipeline change | `ci: add regression gate to PR checks` |
| `build:` | Build system change | `build: migrate to vite 6` |
| `style:` | Formatting only | `style: prettier formatting pass` |
| `revert:` | Revert a commit | `revert: feat: add experimental search` |

**Subject Rules:**
- Imperative mood, ≤ 72 chars.
- Reference ticket: `fix: resolve NPE in order-service (ORD-331)`.
- Body explains **why**, not what.
- Footer for breaking changes: `BREAKING CHANGE: ...`.

---

# SECTION 5 — Review Gates

No sprint may bypass any gate.

| Gate | Name | Owner | Pass Condition |
|---|---|---|---|
| **Gate 1** | Agent Self Validation | Engineer + Agent | Build, typecheck, unit tests green; evidence generated. |
| **Gate 2** | Human Review | Assigned Reviewer | Diff approved; conventions met; no secrets. |
| **Gate 3** | Regression Validation | QA | Full regression suite green on merged code. |
| **Gate 4** | Release Approval | Release Mgr + QA Lead | Sign-off recorded; freeze policy respected. |

**Gate enforcement:**
- Gate 1 is automated (CI required checks).
- Gate 2 requires ≥ 1 human approval (2 for `release/*`/`main`).
- Gate 3 requires QA report attached.
- Gate 4 requires Release Manager sign-off in the release checklist (Section 11).

---

# SECTION 6 — Evidence Requirements

No commit, push, or merge may occur without the following evidence attached to the PR/sprint record.

| Evidence | Required For | Tooling |
|---|---|---|
| Playwright Report | All UI changes | Playwright HTML report |
| Build Report | All changes | `npm run build` / `mvn clean verify` log |
| QA Report | QA/regression sprints | QA test run summary |
| Screenshots | UI changes | Captured in E2E |
| Videos | Flaky/critical flows | Playwright video |
| Trace Files | Performance/debug | Playwright traces |
| Performance Report | `perf/` branches | Load test output |
| Accessibility Report | UI changes | axe / lighthouse |
| Security Report | `security/` branches | SAST/dependency scan |
| Coverage Report | All changes | Coverage threshold met |
| Git Status | All | `git status` (clean working tree) |
| Diff Summary | All | `git diff --stat` |

**Threshold:** Coverage must not drop below the agreed baseline. Build must be warning-free.

---

# SECTION 7 — Release Freeze Policy

Once a `release/vX.Y-rcN` branch is cut from `sporetest`, the freeze is active.

## 7.1 Allowed During Freeze

- ✅ Bug Fixes (`bugfix/QA-NNNN`)
- ✅ Security Fixes (`security/...`)
- ✅ Performance Improvements (`perf/...`) — only if non-architectural
- ✅ QA Automation (`qa/...`)
- ✅ Regression (`qa/regression-N`)
- ✅ Documentation (`docs/`)

## 7.2 Rejected During Freeze

- ❌ New Features
- ❌ Refactoring (behavior-neutral still risky mid-RC)
- ❌ Dependency Upgrades (unless security-required)
- ❌ Architecture Changes

**Exception path:** Any rejected item requires Release Manager + Architect approval via ADR and a re-cut of the RC.

---

# SECTION 8 — Build Verification

Never assume all projects use npm. SporeKart is polyglot.

## 8.1 Detection Matrix

| Project Type | Location | Detect By | Install | Dev | Build / Verify |
|---|---|---|---|---|---|
| Frontend (Vite/React) | `frontend/web-app`, `frontend/*` | `package.json` + `vite.config.*` | `npm install` | `npm run dev` | `npm run build` |
| Mobile | `mobile/*` | `package.json` | `npm install` | `npm run dev` | `npm run build` |
| Backend (Java) | `services/*` | `pom.xml` | `mvn dependency:resolve` | — | `mvn clean verify` |
| Monorepo root | (none currently) | — | — | — | — |

## 8.2 Rules

- Detect per-directory; do not run `npm install` in backend service dirs.
- Verify `node_modules` and `target/` are gitignored.
- Build verification must run in CI for every PR touching that module.
- Frontend apps are independent — install/build each separately.

---

# SECTION 9 — Documentation Structure

```
PROJECT_DOCUMENTATION/
├── Architecture/          (Owner: Architect)        System, service, data-flow diagrams
├── Engineering Standards/ (Owner: Eng Lead)         This manual, coding standards
├── Sprint Reports/        (Owner: Sprint Lead)      Per-sprint summary
├── QA Reports/            (Owner: QA Lead)          Test runs, coverage
├── Bug Reports/           (Owner: QA)               Defect records, root cause
├── Release Notes/         (Owner: Release Mgr)      Version changelogs
├── Git Strategy/          (Owner: DevOps Lead)      Branch/merge strategy, this manual
├── Runbooks/             (Owner: SRE)               Operational procedures
├── Operations/            (Owner: SRE)              Monitoring, on-call
├── ADRs/                 (Owner: Architect)         Architecture Decision Records
├── Production/            (Owner: Release Mgr)      Deployment, rollback records
├── Security/              (Owner: Security Lead)    Threat models, audits
└── Testing/              (Owner: QA Lead)          Strategy, frameworks, coverage
```

Every folder has a named owner and a `README.md` describing contents and update cadence.

---

# SECTION 10 — Rollback Strategy

| Level | Trigger | Procedure |
|---|---|---|
| **Bug Fix Rollback** | Defect in merged `bugfix/*` | Revert commit on branch; raise new `bugfix/`. |
| **Sprint Rollback** | Sprint regression severe | Revert sprint merge to `sporetest`; re-open tickets. |
| **Release Candidate Rollback** | RC unstable | Abandon `release/vX.Y-rcN`; cut `release/vX.Y-rc(N+1)` from `sporetest`. |
| **Production Rollback** | `main` incident | Deploy previous tagged release; raise `hotfix/*` from `main`. |

**Recovery Process:**
1. Identify blast radius (service/feature).
2. Tag current broken state (`broken/vX.Y-<ts>`).
3. Revert/redeploy previous known-good tag.
4. Raise corrective branch (`hotfix/` or `bugfix/`).
5. Post-incident review logged in `Production/`.

---

# SECTION 11 — Master Release Checklist

| # | Item | Status |
|---|---|---|
| 1 | Repository Clean (`git status`) | ☐ |
| 2 | Build Pass (frontend + backend) | ☐ |
| 3 | Tests Pass (unit + integration + e2e) | ☐ |
| 4 | QA Approved (QA Report attached) | ☐ |
| 5 | Regression Pass (Gate 3) | ☐ |
| 6 | Documentation Updated | ☐ |
| 7 | Tag Created (`vX.Y.Z`) | ☐ |
| 8 | Backup Verified | ☐ |
| 9 | Release Notes Ready | ☐ |
| 10 | Approval Granted (Gate 4) | ☐ |

Release is blocked until all 10 are checked.

---

# SECTION 12 — Risk Analysis

| Risk Class | Risk | Mitigation |
|---|---|---|
| **Current** | No `main`/production branch | Introduce `main` as stable trunk. |
| **Current** | `package-lock.json` untracked | Commit it; enforce in CI. |
| **Current** | `.vite/` not gitignored | Add to `.gitignore`. |
| **Future** | Long-lived `sporetest` direct commits | Enforce `feature/*` PR-only flow. |
| **Scaling** | 12 frontend + 16 backend + 5 mobile modules | Per-module CI; matrix builds. |
| **Scaling** | No monorepo tooling | Keep independent apps; document per-app build. |
| **Repository** | Large binary/asset commits | Add Git LFS policy; audit `.gitignore`. |
| **QA** | Build not verified before RC cut | Mandatory build gate pre-RC (Section 8). |
| **Release** | Freeze violations | Enforce branch protections + CODEOWNERS. |
| **Release** | No rollback tag | Require tag before prod deploy. |

---

# SECTION 13 — Final Recommendation

Adopt this manual as the **permanent Engineering Operations Standard** for SporeKart.

1. Ratify the target topology (Section 1.3) — add `main`, `feature/*`, `hotfix/*`.
2. Enforce the SOP lifecycle (Section 2) with the mandatory `STOP` gate.
3. Apply branch (Section 3), commit (Section 4), and gate (Section 5) standards in CI.
4. Require evidence (Section 6) on every PR.
5. Enforce Release Freeze (Section 7) via protected branches.
6. Implement per-module build verification (Section 8).
7. Stand up `PROJECT_DOCUMENTATION/` (Section 9) with owners.
8. Codify rollback (Section 10) and the release checklist (Section 11).
9. Track risks (Section 12) in the backlog.

**This document is the single source of truth. Deviations require an approved ADR.**

---

*End of Engineering Operations Manual v1.0 — DRAFT, awaiting approval.*
