# Trainer Copilot — Architecture

## Service Overview

- **Service name:** `trainer-copilot-service`
- **Port:** 8101
- **Protocol:** HTTP/2 (gRPC for internal, REST for external)
- **Containerisation:** Docker
- **Language / runtime:** Go 1.22+
- **Deployment:** Kubernetes (SporeKart Platform)

## Integration with Enterprise Copilot Framework

The Trainer Copilot registers as a copilot plugin within the Enterprise Copilot Framework. Every request is authenticated and authorised by the framework's sidecar proxy before reaching the service.

### Authentication flow

1. Client presents a JWT issued by the Identity Service.
2. The API gateway validates the JWT and forwards it to Trainer Copilot.
3. The service extracts the `sub` (user ID) and `roles` claims for internal RBAC checks.

### Observability

- Traces propagated via OpenTelemetry (W3C TraceContext).
- Structured logs written to stdout, collected by Fluentd.
- Metrics exposed at `/metrics` for Prometheus scraping.

## Engine Components

```
┌─────────────────────────────────────────────────────┐
│                Trainer Copilot                      │
│  ┌───────────┐ ┌──────────┐ ┌──────────────────┐   │
│  │Training   │ │ Lesson   │ │ Assessment       │   │
│  │Planner    │ │Generator │ │ Engine           │   │
│  └─────┬─────┘ └────┬─────┘ └───────┬──────────┘   │
│  ┌─────┴─────┐ ┌────┴─────┐ ┌───────┴──────────┐   │
│  │Student    │ │Certifi-  │ │Practical         │   │
│  │Analytics  │ │cation    │ │Cultivation       │   │
│  └─────┬─────┘ └────┬─────┘ └───────┬──────────┘   │
│  ┌─────┴─────┐ ┌────┴─────┐                       │
│  │Batch      │ │Knowledge │                       │
│  │Management │ │Retrieval │                       │
│  └───────────┘ └──────────┘                       │
└─────────────────────────────────────────────────────┘
```

### TrainingPlanner

- Generates course roadmaps from learning objectives.
- Suggests session duration, sequencing, and break schedules.
- Output: `CoursePlan` object with structured session list.

### LessonGenerator

- Creates lesson content for five types: `LECTURE`, `DEMONSTRATION`, `LAB_EXERCISE`, `WORKSHOP`, `REVIEW`.
- Accepts parameters: topic, duration, difficulty, prerequisites, learning objectives.
- Output: `LessonPlan` with sections, timing, activities, and materials.

### Assessment Engine

- Generates questions for seven types: `MCQ`, `SHORT_ANSWER`, `LONG_ANSWER`, `SCENARIO`, `PRACTICAL`, `LAB`, `CERTIFICATION`.
- Supports three difficulty levels: `BEGINNER`, `INTERMEDIATE`, `ADVANCED`.
- Produces auto-graded answer keys for objective types and rubrics for subjective types.
- Output: `Assessment` object with questions, answer key, and time estimate.

### StudentAnalytics

- Computes per-batch and per-student metrics.
- Tracks attendance trends, topic mastery, and certification readiness.
- Output: `BatchAnalytics`, `StudentAnalytics`, `TrendData` objects.

### Certification

- Evaluates eligibility against thresholds (attendance >= 80%, assessment >= 60%, practical >= 80%).
- Computes weighted certification score.
- Produces recommendation: `CERTIFIED`, `NEEDS_IMPROVEMENT`, `NOT_READY`.

### PracticalCultivation

- Scaffolds hands-on lab exercises with guided steps, prerequisites, and success criteria.
- Supports multi-step practical workflows with checkpoints.

### BatchManagement

- CRUD operations for training batches.
- Assigns trainers, manages student rosters, tracks batch status.

### KnowledgeRetrieval

- Connects to the Knowledge Base via gRPC for RAG-based content retrieval.
- Returns relevant documents, reference materials, and past assessments.

## Data Flow Diagrams

### Lesson Generation Flow

```
Trainer → LMS Frontend → POST /v1/trainer-copilot/lessons/generate
                              │
                              ▼
                    Trainer Copilot API
                              │
                              ▼
                    LessonGenerator Engine
                              │
                    ┌─────────┴──────────┐
                    ▼                    ▼
            KnowledgeRetrieval    TrainingPlanner
            (fetch references)    (validate sequence)
                    │                    │
                    └─────────┬──────────┘
                              ▼
                    LessonPlan Response
                              │
                              ▼
                    LMS Frontend → Trainer
```

### Assessment & Certification Flow

```
Student completes assessment → POST /v1/trainer-copilot/assessments/evaluate
                                      │
                                      ▼
                              Assessment Engine
                              (grade answers)
                                      │
                                      ▼
                              StudentAnalytics
                              (update metrics)
                                      │
                                      ▼
                              Certification Engine
                              (check eligibility → compute score)
                                      │
                                      ▼
                              GET /v1/trainer-copilot/analytics/certification/{studentId}
                              → CertificationResponse
```

## Security and RBAC

### Roles

| Role | Scope |
|---|---|
| `trainer` | Full access to own batches, lessons, assessments, and student analytics |
| `admin` | Cross-batch access, certification overrides, system configuration |
| `student` | Read-only access to own assessments, analytics, and certification status |

### Security controls

- All endpoints require valid JWT authentication.
- Endpoints enforce role-based access via the `X-Sporekart-Roles` claim.
- Sensitive data (PII) is masked in logs and API responses for non-admin roles.
- Rate limiting applied per tenant and per endpoint at the gateway level.
