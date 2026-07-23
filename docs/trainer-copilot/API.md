# Trainer Copilot — API Reference

Base URL: `https://api.sporekart.example/v1/trainer-copilot`

All endpoints require JWT authentication via the `Authorization: Bearer <token>` header.

---

## Chat

### POST /chat

Send a conversational message to the Trainer Copilot.

```json
// Request
{
  "message": "Create a beginner lesson on Docker Compose",
  "context": {
    "batchId": "batch_101",
    "userId": "trainer_7"
  }
}

// Response
{
  "reply": "Sure! I can generate a beginner-level lesson on Docker Compose. Would you like it as a lecture, demonstration, or lab exercise?",
  "suggestions": ["LECTURE", "DEMONSTRATION", "LAB_EXERCISE"],
  "conversationId": "conv_a1b2c3"
}
```

---

## Lessons

### POST /lessons/generate

Generate a lesson plan.

```json
// Request
{
  "topic": "Kubernetes Pod Basics",
  "lessonType": "DEMONSTRATION",
  "durationMinutes": 45,
  "difficulty": "INTERMEDIATE",
  "learningObjectives": [
    "Define a Kubernetes Pod",
    "Create a Pod using YAML",
    "Inspect Pod status and logs"
  ],
  "prerequisites": ["Docker basics", "kubectl installed"],
  "language": "en"
}

// Response
{
  "lessonId": "lgen_x1y2z3",
  "topic": "Kubernetes Pod Basics",
  "lessonType": "DEMONSTRATION",
  "difficulty": "INTERMEDIATE",
  "durationMinutes": 45,
  "sections": [
    {
      "title": "Pod Concept Overview",
      "durationMinutes": 10,
      "activities": [
        {"type": "explanation", "content": "What is a Pod?"}
      ]
    },
    {
      "title": "Writing a Pod YAML",
      "durationMinutes": 15,
      "activities": [
        {"type": "live-coding", "content": "Write pod.yaml"},
        {"type": "checkpoint", "content": "Validate YAML"}
      ]
    },
    {
      "title": "Deploy and Inspect",
      "durationMinutes": 15,
      "activities": [
        {"type": "live-coding", "content": "kubectl apply"},
        {"type": "checkpoint", "content": "kubectl get pods"}
      ]
    },
    {
      "title": "Q&A and Recap",
      "durationMinutes": 5,
      "activities": [
        {"type": "discussion", "content": "Best practices"}
      ]
    }
  ],
  "totalActivities": 6,
  "checkpoints": 2,
  "createdAt": "2026-07-23T09:00:00Z"
}
```

---

## Assessments

### POST /assessments/generate

Generate an assessment.

```json
// Request
{
  "topic": "Docker Networking",
  "assessmentType": "MCQ",
  "difficulty": "INTERMEDIATE",
  "questionCount": 10,
  "timeLimitMinutes": 15,
  "passingScore": 70,
  "learningObjectives": [
    "Understand bridge, host, and overlay networks",
    "Configure container networking"
  ]
}

// Response
{
  "assessmentId": "assess_b2c3d4e5",
  "topic": "Docker Networking",
  "assessmentType": "MCQ",
  "difficulty": "INTERMEDIATE",
  "timeLimitMinutes": 15,
  "passingScore": 70,
  "questions": [
    {
      "id": "q1",
      "type": "MCQ",
      "difficulty": "BEGINNER",
      "text": "Which Docker network driver provides isolation?",
      "options": [
        {"key": "A", "text": "bridge"},
        {"key": "B", "text": "host"},
        {"key": "C", "text": "overlay"},
        {"key": "D", "text": "macvlan"}
      ],
      "correctAnswer": "A",
      "explanation": "The bridge driver creates an internal isolated network."
    }
  ],
  "totalQuestions": 10,
  "estimatedDurationMinutes": 12,
  "createdAt": "2026-07-23T09:00:00Z"
}
```

### POST /assessments/evaluate

Evaluate submitted answers.

```json
// Request
{
  "assessmentId": "assess_b2c3d4e5",
  "studentId": "student_42",
  "answers": [
    {"questionId": "q1", "response": "A"},
    {"questionId": "q2", "response": "C"}
  ]
}

// Response
{
  "assessmentId": "assess_b2c3d4e5",
  "studentId": "student_42",
  "score": 85,
  "passed": true,
  "totalQuestions": 10,
  "correctCount": 7,
  "incorrectCount": 2,
  "partialCount": 1,
  "sectionResults": [
    {"section": "Docker Networking", "score": 85, "status": "PASSED"}
  ],
  "evaluatedAt": "2026-07-23T09:15:00Z"
}
```

---

## Batches

### GET /batches

List all batches.

```json
// Response
{
  "batches": [
    {
      "batchId": "batch_101",
      "name": "DevOps Fundamentals — July 2026",
      "trainerId": "trainer_7",
      "studentCount": 24,
      "status": "ACTIVE",
      "startDate": "2026-07-01",
      "endDate": "2026-08-15"
    }
  ],
  "totalCount": 1
}
```

### POST /batches

Create a new batch.

```json
// Request
{
  "name": "Kubernetes Advanced — Aug 2026",
  "trainerId": "trainer_7",
  "studentIds": ["student_1", "student_2"],
  "startDate": "2026-08-01",
  "endDate": "2026-09-15"
}

// Response
{
  "batchId": "batch_102",
  "name": "Kubernetes Advanced — Aug 2026",
  "trainerId": "trainer_7",
  "studentIds": ["student_1", "student_2"],
  "status": "ACTIVE",
  "startDate": "2026-08-01",
  "endDate": "2026-09-15",
  "createdAt": "2026-07-23T09:00:00Z"
}
```

### GET /batches/{batchId}

Get batch details.

```json
// Response
{
  "batchId": "batch_101",
  "name": "DevOps Fundamentals — July 2026",
  "trainerId": "trainer_7",
  "studentCount": 24,
  "status": "ACTIVE",
  "startDate": "2026-07-01",
  "endDate": "2026-08-15",
  "createdAt": "2026-06-15T09:00:00Z"
}
```

---

## Analytics

### GET /analytics/batches/{batchId}

Get batch-level analytics.

```json
// Response
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
    {"studentId": "student_1", "name": "Alice", "score": 94}
  ],
  "analyticsPeriod": {
    "start": "2026-07-01",
    "end": "2026-07-23"
  }
}
```

### GET /analytics/students/{studentId}

Get individual student analytics.

```json
// Response
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

### GET /analytics/trends

Get trend data.

| Query param | Type | Required | Description |
|---|---|---|---|
| `batchId` | string | yes | Batch identifier |
| `metric` | string | yes | Metric name (`averageScore`, `attendanceRate`, `completionRate`) |
| `period` | integer | no | Lookback period in days (default: 30) |

```json
// Response
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

### GET /analytics/certification/{studentId}

Get certification status for a student.

```json
// Response
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

---

## Health

### GET /health

Service health check.

```json
// Response
{
  "status": "UP",
  "service": "trainer-copilot-service",
  "version": "1.0.0",
  "timestamp": "2026-07-23T09:00:00Z"
}
```
