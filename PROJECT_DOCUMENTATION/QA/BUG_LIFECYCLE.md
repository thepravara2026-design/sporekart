# SporeKart Bug Lifecycle

**Version:** 1.0
**Owner:** QA Lead
**Status:** Approved
**Applies To:** All SporeKart QA sprints, bug fix sprints, and regression sprints.

---

## 1. Bug Lifecycle

```
Bug Found
   ↓
Triaged
   ↓
Verified
   ↓
Prioritized
   ↓
Assigned
   ↓
In Progress
   ↓
Fixed
   ↓
Code Review
   ↓
Regression
   ↓
QA Verified
   ↓
Closed
```

### Phase Definitions

| Phase | Owner | Description |
|---|---|---|
| **Bug Found** | Reporter | Bug logged in tracking system with repro steps, environment, severity, evidence. |
| **Triaged** | QA Lead | Bug confirmed as valid; duplicates merged; initial severity assigned. |
| **Verified** | QA Lead | Bug reproduced in QA environment; evidence attached. |
| **Prioritized** | QA Lead + Product | Priority assigned based on severity + business impact. |
| **Assigned** | QA Lead | Bug assigned to engineer with target branch (`bugfix/QA-NNNN`). |
| **In Progress** | Engineer | Fix being implemented on `bugfix/QA-NNNN` branch. |
| **Fixed** | Engineer | Code complete; self-validated; evidence generated. |
| **Code Review** | Reviewer | Diff reviewed per Engineering Operations Manual (Gate 2). |
| **Regression** | QA | Full regression suite run on merged fix. |
| **QA Verified** | QA Lead | Bug confirmed fixed in QA environment. |
| **Closed** | QA Lead | Bug closed; linked to release notes. |

---

## 2. Severity Levels

| Severity | Definition | Examples |
|---|---|---|
| **Critical** | System down, data loss, security breach, payment failure | Login broken, DB corruption, PII leak |
| **High** | Major feature broken, no workaround | Checkout fails, report shows wrong data |
| **Medium** | Feature partially broken, workaround exists | UI misalignment, non-critical validation |
| **Low** | Cosmetic, minor UX, documentation | Typo, tooltip missing, color contrast |

---

## 3. Priority Levels

| Priority | Definition | SLA |
|---|---|---|
| **P0** | Blocking release — must fix before next RC | 24 hours |
| **P1** | Critical — must fix in current sprint | 3 days |
| **P2** | Normal — fix in current or next sprint | 1 sprint |
| **P3** | Low — backlog, fix when scheduled | No SLA |

---

## 4. Bug Numbering Convention

```
QA-0001
QA-0002
QA-0003
...
```

**Rules:**
- Sequential, zero-padded to 4 digits.
- Never reuse a bug ID (even if the bug is invalid/duplicate).
- IDs are assigned by the QA tracking system at triage time.
- The numbering is global across all sprints and releases.

---

## 5. Bug Report Template

```
Bug ID:       QA-XXXX
Severity:     Critical | High | Medium | Low
Priority:     P0 | P1 | P2 | P3
Status:       New | Triaged | Verified | In Progress | Fixed | QA Verified | Closed
Found In:     release/vX.Y-rcN
Found By:     [Name]
Environment:  QA | Dev
Module:       [frontend/web-app | services/order-service | ...]
Description:  [Clear, concise description]
Steps to Reproduce:
  1. ...
  2. ...
Expected:     ...
Actual:       ...
Evidence:     [Screenshot | Video | Trace | Log]
```

---

## 6. Bug Branch Naming

Every bug fix must be on a branch named after the bug ID:

```
bugfix/QA-0001
bugfix/QA-0002
```

---

## 7. Bug Closure Criteria

- Fix merged to the active `release/*` branch.
- Regression suite green.
- QA Lead has verified the fix in QA environment.
- Evidence (screenshot/video/trace) attached to the bug record.
