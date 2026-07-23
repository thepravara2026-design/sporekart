# Trainer Copilot — Lesson Generation Engine

## Supported Lesson Types

| Type | Description | Typical Duration |
|---|---|---|
| `LECTURE` | Instructor-led presentation with slides and Q&A | 30–60 min |
| `DEMONSTRATION` | Live walkthrough of a tool, process, or concept | 15–45 min |
| `LAB_EXERCISE` | Hands-on guided exercise with defined steps | 30–120 min |
| `WORKSHOP` | Collaborative session with group activities | 60–180 min |
| `REVIEW` | Recap of previous topics with quizzes and discussion | 15–30 min |

## Generation Parameters

| Parameter | Type | Required | Description |
|---|---|---|---|
| `topic` | string | yes | Subject matter of the lesson |
| `lessonType` | enum | yes | One of the five types above |
| `durationMinutes` | integer | yes | Target duration in minutes |
| `difficulty` | enum | no | `BEGINNER`, `INTERMEDIATE`, `ADVANCED` (default: `INTERMEDIATE`) |
| `prerequisites` | string[] | no | List of prerequisite topics |
| `learningObjectives` | string[] | yes | What students should learn |
| `materialReferences` | string[] | no | Specific documents or URLs to incorporate |
| `language` | string | no | Output language (default: `en`) |

## Output Format

```json
{
  "lessonId": "lgen_a1b2c3d4",
  "topic": "Introduction to Docker",
  "lessonType": "LAB_EXERCISE",
  "difficulty": "BEGINNER",
  "durationMinutes": 60,
  "sections": [
    {
      "title": "Setup Environment",
      "durationMinutes": 10,
      "activities": [
        {"type": "instruction", "content": "Install Docker Desktop"},
        {"type": "checkpoint", "content": "Run `docker --version`"}
      ],
      "materialRefs": ["docs/docker-setup.pdf"]
    },
    {
      "title": "First Container",
      "durationMinutes": 20,
      "activities": [
        {"type": "instruction", "content": "Pull nginx image"},
        {"type": "instruction", "content": "Run container on port 8080"},
        {"type": "checkpoint", "content": "Verify http://localhost:8080 responds"}
      ],
      "materialRefs": []
    }
  ],
  "totalActivities": 5,
  "checkpoints": 2,
  "createdAt": "2026-07-23T09:00:00Z"
}
```

## Example Request

### Generate a Lesson

```
POST /v1/trainer-copilot/lessons/generate
Content-Type: application/json
Authorization: Bearer <token>

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
```

### Response

```json
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
        {"type": "explanation", "content": "What is a Pod? — the smallest deployable unit"}
      ]
    },
    {
      "title": "Writing a Pod YAML",
      "durationMinutes": 15,
      "activities": [
        {"type": "live-coding", "content": "Write pod.yaml with nginx container"},
        {"type": "checkpoint", "content": "Validate YAML syntax"}
      ]
    },
    {
      "title": "Deploy and Inspect",
      "durationMinutes": 15,
      "activities": [
        {"type": "live-coding", "content": "Run `kubectl apply -f pod.yaml`"},
        {"type": "checkpoint", "content": "Run `kubectl get pods` and `kubectl logs`"}
      ]
    },
    {
      "title": "Q&A and Recap",
      "durationMinutes": 5,
      "activities": [
        {"type": "discussion", "content": "Common pitfalls and best practices"}
      ]
    }
  ],
  "totalActivities": 6,
  "checkpoints": 2,
  "createdAt": "2026-07-23T09:00:00Z"
}
```

## Customization Options

- **Template override:** Pass a `templateId` to use a custom section structure.
- **Material injection:** Provide `materialReferences` to ground the lesson in specific documents.
- **Activity style:** Set `activityStyle` to `INTERACTIVE`, `READING`, or `MIXED` to control the mix of activity types.
- **Brand voice:** Supply a `brandGuide` URL for tone and terminology alignment.
