# Evaluation Architecture

## Evaluation Model

| Field | Type | Description |
|-------|------|-------------|
| id | string | Unique identifier |
| maxMarks | number | Maximum possible score (50–100) |
| passingMarks | number | Minimum passing score (35) |
| scoredMarks | number \| null | Actual score |
| grade | string \| null | Letter grade (A/B/C/D) |
| comments | string | Evaluator feedback |
| suggestions | string | Improvement suggestions |
| evaluationStatus | enum | pending \| in-progress \| completed \| appealed |

## Evaluation Statuses

- **pending** — In review queue, not yet assigned
- **in-progress** — Evaluator actively reviewing
- **completed** — Scores and feedback published
- **appealed** — Student requested re-evaluation (future)

## Grade Scale

| Score Range | Grade |
|-------------|-------|
| 85–100 | A |
| 70–84 | B |
| 50–69 | C |
| 0–49 | D |

## Future Enhancements

- Rubric-based evaluation
- AI-assisted grading
- Peer review workflow
- Appeal workflow
- Re-evaluation queue
