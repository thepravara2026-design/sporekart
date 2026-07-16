# Placement Domain Model

## Placement Statuses
`placement-ready` | `resume-shortlisted` | `interview-scheduled` | `interview-completed` | `selected` | `offer-received` | `offer-accepted` | `joined` | `rejected` | `not-placed` | `withdrawn` | `deregistered`

## Placement Types
`on-campus` | `off-campus` | `virtual` | `international` | `corporate-tieup` | `government-scheme` | `entrepreneurial` | `freelance`

## Placement Drive Stages
`announced` → `registrations-open` → `registrations-closed` → `scheduled` → `in-progress` → `results-pending` → `completed` | `cancelled`

## Key Interfaces
- **PlacementDrive** — Drive lifecycle with company, stage, registrations, rounds
- **StudentCareerProfile** — Student's career data, skills, resume, placement status
- **PlacementDashboard** — Executive KPIs: placement rate, packages, top recruiters

## Pages
- PlacementHub — Drive lifecycle view with stage tabs and pagination
- PlacementAnalytics — Placement trends, top recruiters, summary metrics
