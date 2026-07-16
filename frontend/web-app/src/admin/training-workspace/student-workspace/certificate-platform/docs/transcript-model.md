# Certificate Platform — Transcript Model

## Overview

The Academic Transcript provides a comprehensive academic record for each student, including course-level performance, cumulative metrics, and credential counts.

## Transcript Structure

```
AcademicTranscript
  ├── Student Info: id, name, email, photo
  ├── Summary: enrollmentDate, completionDate, status
  ├── Metrics:
  │   ├── totalCourses, completedCourses
  │   ├── totalAttendancePercent
  │   ├── overallAssignmentScore, overallAssessmentScore
  │   ├── competenciesAchieved / totalCompetencies
  │   ├── achievementsCount, certificatesCount
  │   ├── totalLearningHours
  │   ├── overallPerformance (%)
  │   ├── gpa (1.0–4.0)
  │   └── credits
  └── courseRecords: TranscriptCourseRecord[]
```

## Course Record Fields

| Field | Description |
|-------|-------------|
| attendancePercent | Per-course attendance (70-100%) |
| assignmentScore | Per-course assignment score (70-100%) |
| assessmentScore | Per-course assessment score (70-100%) |
| competenciesCount | Competencies earned in course |
| learningHours | Hours spent on course |
| grade | Letter grade (A+, A, B+, B, C+, C, D) |
| gradePoint | Numeric grade point (1-4) |
| status | in-progress / completed / certified / failed |

## Transcript Status

- **active**: Student currently enrolled
- **completed**: All courses completed
- **graduated**: Graduated from program
