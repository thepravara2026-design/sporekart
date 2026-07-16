# Assignment Platform — Future Integration Readiness

All interfaces prepared. None implemented.

## Integration Points

| Platform | Interface | Trigger |
|----------|-----------|---------|
| Attendance | studentId + batchId | Assignment eligibility check |
| Assessment | Assessment model | Assignment marks contribute to assessment |
| Certificate | completion status | Certificate eligibility gate |
| Learning Progress | Assignment completion % | Progress tracking per course |
| Communication | Assignment status changes | Deadline reminders, submission notifications |
| Analytics Platform | AssignmentAnalytics | Cross-module analytics |
| Cloud Storage | attachments[] | File upload for submissions |
| Google Drive | externalLinks | Document integration |
| OneDrive | externalLinks | Document integration |
| Dropbox | externalLinks | Document integration |
| AWS S3 | attachments[] | Scalable file storage |
| Cloudinary | attachments[] | Media management |
| AI Evaluation | Evaluation model | Auto-grading and feedback |
| AI Feedback | comments + suggestions | Personalized learning recommendations |
| AI Rubrics | future rubric model | Standards-based grading |
| Plagiarism Detection | plagiarismScore | Academic integrity checks |
| GitHub/GitLab | submissionType: git-repository | Code submission integration |
| CRM | Assignment completion | Student lifecycle events |
| ERP | Assignment stats | Enterprise reporting |
| Placement Platform | completion + grade | Placement eligibility |
| Internship Platform | project portfolio | Internship matching |
