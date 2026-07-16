# Certificate Platform — Achievement Architecture

## Overview

The Achievement Center tracks student accomplishments across 12 achievement types, each with associated points and optional badge/certificate linkage.

## Achievement Types (12)

| Type | Points Range | Description |
|------|-------------|-------------|
| course-completed | 50-550 | Successfully completed a course |
| perfect-attendance | 50-550 | 100% attendance record |
| top-performer | 50-550 | Top performance in batch |
| fast-learner | 50-550 | Completed ahead of schedule |
| outstanding-project | 50-550 | Exceptional project work |
| highest-marks | 50-550 | Highest assessment marks |
| innovation-award | 50-550 | Innovative solution |
| research-excellence | 50-550 | Research contribution |
| industry-ready | 50-550 | Industry readiness certified |
| leadership-award | 50-550 | Demonstrated leadership |
| community-contributor | 50-550 | Community engagement |
| ai | 50-550 | AI achievement placeholder |

## Relationships

- Achievement → optional badgeId (links to DigitalBadge)
- Achievement → optional certificateId (links to Certificate)
- Achievement → isPublic flag for sharing control

## Points System

- Each achievement awards 50-550 points
- Total points shown in Achievement Center summary
- Points calculated in mockData generators
