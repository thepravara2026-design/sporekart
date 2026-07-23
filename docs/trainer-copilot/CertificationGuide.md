# Trainer Copilot — Certification Guide

## Eligibility Criteria

A student must meet **all** three thresholds to be considered eligible for certification:

| Criteria | Threshold | Source |
|---|---|---|
| Attendance | >= 80% | Session attendance records |
| Assessment Score | >= 60% | Average across all assessments |
| Practical Score | >= 80% | Practical / lab evaluation scores |

## Score Calculation Formula

```
certificationScore = (attendanceWeight * attendanceRate)
                   + (assessmentWeight * averageAssessmentScore)
                   + (practicalWeight * averagePracticalScore)

Default weights:
  attendanceWeight   = 0.20
  assessmentWeight   = 0.50
  practicalWeight    = 0.30
```

`certificationScore` is rounded to two decimal places. Maximum value is 100.

## Recommendation Categories

| Category | Condition |
|---|---|
| `CERTIFIED` | All eligibility criteria met AND `certificationScore >= 70` |
| `NEEDS_IMPROVEMENT` | All eligibility criteria met AND `certificationScore < 70` |
| `NOT_READY` | One or more eligibility criteria not met |

## Certification Workflow

```
┌──────────────────┐
│  Batch Complete  │
└────────┬─────────┘
         ▼
┌──────────────────────────────┐
│  Check Eligibility Criteria  │
│  • Attendance >= 80%         │
│  • Assessment >= 60%         │
│  • Practical >= 80%          │
└────────┬─────────┬───────────┘
         │         │
    ALL MET    ANY NOT MET
         │         │
         ▼         ▼
┌──────────────────┐  ┌──────────────────┐
│ Compute Score    │  │ Mark NOT_READY   │
│ Apply formula    │  │ List missing     │
│ Generate result  │  │ criteria         │
└────────┬─────────┘  └──────────────────┘
         │
         ▼
   ┌──────────┐
   │ Score    │
   │ >= 70 ?  │
   └┬───────┬─┘
    │       │
   YES      NO
    │       │
    ▼       ▼
┌────────┐ ┌──────────────────┐
│CERTIFIED│ │NEEDS_IMPROVEMENT│
└────────┘ └──────────────────┘
```

## Example Request

```
GET /v1/trainer-copilot/analytics/certification/student_42
```

```json
{
  "studentId": "student_42",
  "name": "Charlie",
  "batchId": "batch_101",
  "attendanceRate": 92.0,
  "averageAssessmentScore": 81.5,
  "averagePracticalScore": 87.0,
  "eligibilityCriteria": {
    "attendanceMet": true,
    "assessmentMet": true,
    "practicalMet": true
  },
  "certificationScore": 84.25,
  "recommendation": "CERTIFIED",
  "evaluatedAt": "2026-07-23T09:00:00Z"
}
```
