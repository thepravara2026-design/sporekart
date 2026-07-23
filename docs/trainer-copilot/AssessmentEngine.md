# Trainer Copilot — Assessment Engine

## Supported Assessment Types

| Type | Description | Grading |
|---|---|---|
| `MCQ` | Multiple-choice with one or more correct answers | Auto-graded |
| `SHORT_ANSWER` | Brief written response (1–2 sentences) | Rubric-based |
| `LONG_ANSWER` | Detailed written response (paragraph or more) | Rubric-based |
| `SCENARIO` | Real-world situational question with analysis | Rubric-based |
| `PRACTICAL` | Hands-on task evaluated against criteria | Checklist-based |
| `LAB` | Multi-step lab exercise with checkpoints | Auto + checklist |
| `CERTIFICATION` | Comprehensive summative assessment | Weighted scoring |

## Difficulty Levels

| Level | Description |
|---|---|
| `BEGINNER` | Recall and comprehension — factual questions, basic application |
| `INTERMEDIATE` | Application and analysis — scenario-based, multi-step reasoning |
| `ADVANCED` | Synthesis and evaluation — complex scenarios, design tasks |

## Question Generation Approach

1. **Ingestion** — The engine receives topic, type, difficulty, and count.
2. **Knowledge Retrieval** — Relevant material is fetched from the Knowledge Base.
3. **Question Drafting** — A language model generates candidate questions with distractors (for MCQ) or rubrics (for subjective).
4. **Validation** — Questions are checked for duplication, ambiguity, and curriculum alignment.
5. **Assembly** — The final set is ordered by difficulty and assembled into an `Assessment` object.

## Answer Key Generation

- **MCQ:** Correct options are flagged; distractors are labelled with explanations.
- **SHORT_ANSWER / LONG_ANSWER:** A rubric is generated with key points, acceptable variations, and scoring weights.
- **SCENARIO:** Model answer with analysis steps and evaluation criteria.
- **PRACTICAL / LAB:** A checklist of pass/fail criteria with observation notes.
- **CERTIFICATION:** Composite answer key using all of the above weighted by section.

## Evaluation Methodology

- **Auto-graded types (MCQ):** Responses are compared against the answer key. Partial credit is supported for multi-select questions.
- **Rubric-based types:** Responses are scored against a rubric by the engine (LLM-assisted) or optionally by a human reviewer.
- **Checklist types (PRACTICAL, LAB):** Each criterion is marked pass/fail. A configurable threshold determines overall pass/fail.
- **CERTIFICATION:** Combines scores from all sections using a predefined weight configuration.

## Example Request

### Generate an Assessment

```
POST /v1/trainer-copilot/assessments/generate
Content-Type: application/json
Authorization: Bearer <token>

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
```

### Response

```json
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
      "text": "Which Docker network driver provides isolation between containers on the same host?",
      "options": [
        {"key": "A", "text": "bridge"},
        {"key": "B", "text": "host"},
        {"key": "C", "text": "overlay"},
        {"key": "D", "text": "macvlan"}
      ],
      "correctAnswer": "A",
      "explanation": "The bridge driver creates an internal network isolated from the host."
    }
  ],
  "totalQuestions": 10,
  "estimatedDurationMinutes": 12,
  "createdAt": "2026-07-23T09:00:00Z"
}
```

### Evaluate a Submission

```
POST /v1/trainer-copilot/assessments/evaluate
Content-Type: application/json
Authorization: Bearer <token>

{
  "assessmentId": "assess_b2c3d4e5",
  "studentId": "student_42",
  "answers": [
    {"questionId": "q1", "response": "A"},
    {"questionId": "q2", "response": "C"}
  ]
}
```

```json
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
    {
      "section": "Docker Networking",
      "score": 85,
      "status": "PASSED"
    }
  ],
  "evaluatedAt": "2026-07-23T09:15:00Z"
}
```
