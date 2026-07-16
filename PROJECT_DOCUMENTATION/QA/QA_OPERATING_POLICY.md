# SporeKart QA Operating Policy

**Version:** 1.0
**Owner:** QA Lead
**Status:** Approved
**Applies To:** All SporeKart QA sprints, regression sprints, and bug fix sprints.

---

## 1. QA Philosophy

QA is not a phase — it is a continuous discipline embedded in every sprint. Every code change must be validated before it reaches a release candidate. QA owns quality, not testing.

**Principles:**
- Quality is everyone's responsibility.
- Evidence is mandatory (Section 6 of Engineering Operations Manual).
- Automation is preferred over manual testing.
- No release without QA sign-off.

---

## 2. Sprint Lifecycle

```
QA Sprint Planning
   ↓
Environment Preparation (reset + seed)
   ↓
Test Case Execution
   ↓
Bug Reporting
   ↓
Bug Fix Verification
   ↓
Regression
   ↓
Evidence Collection
   ↓
QA Report Generation
   ↓
QA Sign-Off
   ↓
Release Sign-Off
```

---

## 3. Bug Reporting

- Every bug is logged with the template from `BUG_LIFECYCLE.md`.
- Severity and priority assigned at triage.
- Bug branch created: `bugfix/QA-NNNN`.
- Fix verified in QA environment before closing.

---

## 4. Regression Rules

- Full regression suite must pass before QA sign-off.
- Regression is run on the merged `release/*` branch.
- Regression must be green for 3 consecutive runs before sign-off.
- Any regression failure blocks release.

---

## 5. Evidence Collection

Per Section 6 of the Engineering Operations Manual, every QA sprint must produce:

- Playwright / E2E test report
- Build report
- QA test execution summary
- Screenshots for UI changes
- Videos for critical flows
- Performance report (if applicable)
- Accessibility report (if applicable)
- Coverage report

All evidence is stored in `PROJECT_DOCUMENTATION/QA Reports/`.

---

## 6. Approval Gates

| Gate | Name | Owner | Pass Condition |
|---|---|---|---|
| **Gate 1** | Self Validation | Engineer | Build, typecheck, unit tests green |
| **Gate 2** | Human Review | Reviewer | Diff approved |
| **Gate 3** | Regression Validation | QA | Full regression green |
| **Gate 4** | Release Approval | Release Mgr + QA Lead | Sign-off recorded |

---

## 7. QA Sign-Off Criteria

- All P0 and P1 bugs are fixed and verified.
- Regression suite is green (3 consecutive runs).
- Evidence is complete and attached.
- QA Report is generated and stored in `PROJECT_DOCUMENTATION/QA Reports/`.
- No open bugs at severity Critical or High.

---

## 8. Release Sign-Off Criteria

- QA Sign-Off obtained.
- Release Checklist (Section 11 of Engineering Operations Manual) all 10 items checked.
- Release Manager approval recorded.
- Tag created and pushed.

---

## 9. Branch Strategy for QA

| Branch | Purpose | Base |
|---|---|---|
| `qa/qa-sprint-N` | QA test execution sprint | `release/vX.Y-rcN` |
| `qa/regression-N` | Regression testing pass | `release/vX.Y-rcN` |
| `bugfix/QA-NNNN` | Individual bug fix | `release/vX.Y-rcN` |
| `security/QA-NNNN` | Security fix | `release/vX.Y-rcN` |
| `perf/QA-NNNN` | Performance fix | `release/vX.Y-rcN` |

---

## 10. Review Process

- Every bug fix PR requires ≥ 1 human reviewer.
- Every QA sprint PR requires QA Lead review.
- Every regression PR requires QA Lead + Release Manager review.
- No PR may merge without all required checks green.

---

## 11. QA Sign-Off Checklist

| # | Item | Status |
|---|---|---|
| 1 | All P0/P1 bugs fixed and verified | ☐ |
| 2 | Regression suite green (3 consecutive runs) | ☐ |
| 3 | Evidence complete (Section 6 of EOM) | ☐ |
| 4 | QA Report generated and stored | ☐ |
| 5 | No open Critical/High severity bugs | ☐ |
| 6 | Test data policy confirmed | ☐ |
| 7 | Environment reset and verified | ☐ |

---

## 12. Release Sign-Off Checklist

| # | Item | Status |
|---|---|---|
| 1 | QA Sign-Off obtained | ☐ |
| 2 | Release Checklist complete (EOM Section 11) | ☐ |
| 3 | Release Manager approval recorded | ☐ |
| 4 | Tag created and pushed | ☐ |
| 5 | Release notes published | ☐ |
