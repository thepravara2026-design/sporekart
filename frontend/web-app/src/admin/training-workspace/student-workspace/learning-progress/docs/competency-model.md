# Learning Progress Platform — Competency Model

## Overview

The competency model tracks 12 competency categories across knowledge, practical skills, business skills, communication, problem-solving, leadership, critical thinking, innovation, industry readiness, farmer readiness, entrepreneur readiness, and AI competency.

## Competency Lifecycle

```
not-started → in-progress → achieved → mastered
```

## Data Shape

Each Competency record includes:
- Category classification (CompetencyCategory)
- Current level (1–5) and max level
- Percentage score and status
- Associated course and student
- Earned date (nullable)

## Visualization

- **CompetencyCenterPage**: Grid of CompetencyCard components with category filter
- **CompetencyCard**: Shows name, category, level progress bar, percentage, status badge
- **Health Dashboard**: CompetencyCoverage metric shows overall coverage percentage

## Mock Data

- 60 competency records generated across 9 categories
- Status distribution: ~40% achieved/mastered, ~60% in-progress
