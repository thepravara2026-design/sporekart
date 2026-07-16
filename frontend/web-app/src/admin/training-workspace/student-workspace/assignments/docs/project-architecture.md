# Project Architecture

## Project Types

| Type | Description | Team Support |
|------|-------------|--------------|
| Individual | Solo project | Single student |
| Team | Group project | 2–5 members |
| Research | Academic research | Variable |
| Commercial | Industry-sponsored | Variable |
| Field | Field work project | Variable |
| Laboratory | Lab-based project | Variable |
| Innovation | Innovation/patent | Variable |
| Startup | Business venture | Variable |

## Project Model

| Field | Type |
|-------|------|
| id, projectCode | Identifiers |
| title, description | Content |
| projectType | Type enum |
| courseId, courseName | Course link |
| batchId, batchName | Batch link |
| status | AssignmentStatus |
| maxMarks, passingMarks | Scoring |
| dueDate | Deadline |
| teamSize | Number of members or null |
| members | Student ID array |

## Future Enhancements

- Team management interface
- Milestone tracking
- Project submission workflow
- Group evaluation
- Presentation scheduling
