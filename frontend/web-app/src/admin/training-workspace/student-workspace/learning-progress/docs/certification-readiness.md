# Learning Progress Platform — Certification Readiness

## Overview

The Certification Readiness module assesses student eligibility for course certification based on attendance, assignments, assessments, and competency requirements.

## Readiness Criteria

| Metric | Requirement | Weight |
|--------|------------|--------|
| Attendance | ≥ 80% | 25% |
| Assignment Completion | ≥ 80% | 25% |
| Assessment Score | ≥ 70% | 25% |
| Competency Count | ≥ 3 | 25% |

## Data Shape

Each CertificationReadiness record includes:
- Actual vs. required values for all 4 criteria
- Overall eligibility percentage
- isReady boolean (true when all criteria met)
- Pending requirements list (shown when not ready)

## Visualization

- **CertificationReadinessPage**: Summary widgets + grid of CertificationReadinessCard components
- **CertificationReadinessCard**: Color-coded (green=ready, yellow=not ready), shows all 4 criteria with progress bars, pending requirements in red
- Dashboard shows ready/not-ready counts and average eligibility

## Mock Data

- 30 certification readiness records
- ~30% ready, ~70% not ready with specific pending requirements
