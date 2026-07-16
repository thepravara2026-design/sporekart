# Learning Progress Platform — Timeline / Academic Journey

## Overview

The Learning Timeline visualizes each student's academic journey as a chronological sequence of events across 9 event types.

## Event Types (9)

| Type | Icon | Description |
|------|------|-------------|
| `enrollment` | 📝 | Student enrolled in course |
| `lesson-started` | 📖 | Student began a lesson |
| `lesson-completed` | ✅ | Student completed a lesson |
| `assignment-submitted` | 📤 | Student submitted an assignment |
| `assessment-passed` | 🎯 | Student passed an assessment |
| `competency-achieved` | 🏆 | Student achieved a competency |
| `milestone-earned` | ⭐ | Student earned a milestone |
| `certification-eligible` | 🎓 | Student became certification eligible |
| `course-completed` | 🎉 | Student completed the course |

## Visualization

- **LearningTimelinePage**: Side-by-side layout with timeline on left, summary panel on right
- **LearningTimeline**: Vertical timeline with colored dots (primary=completed, gray=pending), connecting lines, event icons
- Summary shows total/completed/pending counts

## Filtering

- Completed/pending/all filter
- Text search across label, description, and student ID
