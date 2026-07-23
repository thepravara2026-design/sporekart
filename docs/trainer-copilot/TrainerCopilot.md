# Trainer Copilot

## Purpose

Trainer Copilot is an AI-powered assistant within the SporeKart Enterprise Copilot Framework that helps trainers design, deliver, and evaluate training programs. It automates lesson plan generation, assessment creation, student analytics, batch management, and certification workflows, reducing administrative overhead and enabling personalised learning at scale.

## Key Capabilities

| Capability | Description |
|---|---|
| **Lesson Generation** | Generate structured lesson plans for lecture, demonstration, lab, workshop, and review formats. |
| **Assessment Engine** | Create assessments across MCQ, short/long answer, scenario, practical, lab, and certification types with configurable difficulty. |
| **Student Analytics** | Track attendance, performance, weak/strong topics, and certification readiness per batch or individual. |
| **Batch Management** | Create and manage training batches, assign trainers, and monitor progress. |
| **Certification Engine** | Evaluate eligibility, compute scores, and issue certification recommendations. |
| **Practical Cultivation** | Scaffold hands-on exercises with guided steps and evaluation criteria. |
| **Knowledge Retrieval** | Retrieve relevant training content, reference materials, and past assessments via RAG. |
| **Training Planner** | Generate course roadmaps, schedule sessions, and sequence learning objectives. |

## Architecture Summary

Trainer Copilot runs as a dedicated microservice on port 8101. It integrates with the Enterprise Copilot Framework for authentication, authorisation, logging, and observability. The service exposes a REST API consumed by the SporeKart LMS frontend and external clients via the API gateway.

Six internal engine components handle domain logic:

- **TrainingPlanner** — course roadmapping and session scheduling
- **LessonGenerator** — lesson plan creation per type
- **Assessment** — question generation, answer keys, and evaluation
- **StudentAnalytics** — metrics computation and trend analysis
- **Certification** — eligibility checks and certification scoring
- **PracticalCultivation** — hands-on exercise scaffolding
- **BatchManagement** — batch CRUD and trainer assignment
- **KnowledgeRetrieval** — RAG-based content retrieval

## Integration Points

| System | Protocol | Purpose |
|---|---|---|
| Enterprise Copilot Framework | gRPC / REST | Auth, RBAC, logging, tracing |
| SporeKart API Gateway | HTTPS | External request routing |
| LMS Frontend | HTTPS (REST) | UI client |
| Knowledge Base | gRPC | RAG document retrieval |
| Student Database | SQL | Persist student records and analytics |
| Assessment Store | SQL | Persist assessments and results |
