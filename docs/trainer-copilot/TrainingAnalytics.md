# Trainer Copilot — Training Analytics

## Analytics Metrics

### Attendance

| Metric | Description |
|---|---|
| `attendanceRate` | Percentage of sessions attended out of total |
| `consecutiveAbsences` | Number of missed sessions in a row |
| `attendanceTrend` | Weekly trend direction (`IMPROVING`, `DECLINING`, `STABLE`) |

### Performance

| Metric | Description |
|---|---|
| `averageScore` | Mean score across all assessments |
| `highestScore` | Best score achieved |
| `lowestScore` | Lowest score achieved |
| `scoreDistribution` | Bucketed histogram of scores |
| `improvementRate` | Percentage change in scores over time |

### Weak / Strong Topics

| Metric | Description |
|---|---|
| `strongTopics` | Topics where average score >= 80% |
| `weakTopics` | Topics where average score < 60% |
| `topicMastery` | Per-topic mastery percentage |
| `recommendedReview` | Topics flagged for revision |

### Completion

| Metric | Description |
|---|---|
| `courseCompletionRate` | Percentage of curriculum completed |
| `pendingLessons` | Count of uncompleted lessons |
| `onTrack` | Whether the student is on schedule (`true`/`false`) |

### Certification Readiness

| Metric | Description |
|---|---|
| `eligibilityStatus` | `ELIGIBLE`, `NEEDS_IMPROVEMENT`, `NOT_READY` |
| `readinessScore` | Composite score (0–100) |
| `missingCriteria` | List of unmet eligibility criteria |

## Batch Analytics

```
GET /v1/trainer-copilot/analytics/batches/{batchId}
```

```json
{
  "batchId": "batch_101",
  "batchName": "DevOps Fundamentals — July 2026",
  "totalStudents": 24,
  "averageAttendance": 87.5,
  "averageScore": 76.3,
  "strongTopics": ["Docker basics", "CI/CD pipelines"],
  "weakTopics": ["Kubernetes networking", "Terraform state"],
  "completionRate": 72.0,
  "certificationReadyCount": 14,
  "certificationNotReadyCount": 10,
  "topPerformers": [
    {"studentId": "student_1", "name": "Alice", "score": 94},
    {"studentId": "student_2", "name": "Bob", "score": 91}
  ],
  "analyticsPeriod": {
    "start": "2026-07-01",
    "end": "2026-07-23"
  }
}
```

## Individual Student Analytics

```
GET /v1/trainer-copilot/analytics/students/{studentId}
```

```json
{
  "studentId": "student_42",
  "name": "Charlie",
  "batchId": "batch_101",
  "attendanceRate": 92.0,
  "consecutiveAbsences": 0,
  "averageScore": 81.5,
  "highestScore": 95,
  "lowestScore": 65,
  "strongTopics": ["Docker basics", "Git branching"],
  "weakTopics": ["Kubernetes ingress"],
  "topicMastery": {
    "Docker basics": 94,
    "Kubernetes ingress": 52,
    "CI/CD pipelines": 78
  },
  "completionRate": 88.0,
  "pendingLessons": 3,
  "onTrack": true,
  "certificationReadiness": {
    "eligibilityStatus": "ELIGIBLE",
    "readinessScore": 83
  },
  "scoreTrend": [
    {"date": "2026-07-01", "score": 72},
    {"date": "2026-07-08", "score": 78},
    {"date": "2026-07-15", "score": 85},
    {"date": "2026-07-22", "score": 81}
  ]
}
```

## Trend Analysis

```
GET /v1/trainer-copilot/analytics/trends?batchId={batchId}&metric={metric}&period={days}
```

```json
{
  "batchId": "batch_101",
  "metric": "averageScore",
  "period": 30,
  "dataPoints": [
    {"date": "2026-06-24", "value": 71.2},
    {"date": "2026-07-01", "value": 73.5},
    {"date": "2026-07-08", "value": 75.0},
    {"date": "2026-07-15", "value": 76.3},
    {"date": "2026-07-22", "value": 76.3}
  ],
  "trendDirection": "IMPROVING",
  "changePercent": 7.2
}
```
