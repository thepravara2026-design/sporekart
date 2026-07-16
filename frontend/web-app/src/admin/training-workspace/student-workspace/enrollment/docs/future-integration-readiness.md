# Enrollment Platform — Future Integration Readiness

All interfaces are prepared for future integration. None are implemented.

## Integration Points

### Attendance Platform
- Enrollment student data feeds attendance roster per batch
- Student status (`enrolled`, `batch-assigned`) determines attendance eligibility
- **Interface**: `EnrollmentStatus` values serve as attendance readiness flags

### Assessment Platform
- Enrolled students are eligible for assessments
- Batch assignment links student to assessment schedule
- **Interface**: `batchId` on `EnrollmentRequest` connects to assessment routing

### Certificate Platform
- Completed enrollments (`enrolled` status) qualify students for certification
- `enrolledDate` establishes certification eligibility timeline
- **Interface**: `EnrollmentTimelineEvent` type `enrollment-completed` triggers certificate workflow

### Learning Progress Platform
- Student batch assignment determines curriculum and progress tracking scope
- Training mode (`online`/`offline`/`hybrid`) influences progress measurement approach
- **Interface**: `batchId` + `trainingMode` on enrollment request

### Finance Platform
- Admission type (`corporate`/`government`/`scholarship`/`regular`) maps to billing rules
- Approval status gates financial processing
- **Interface**: `admissionType` and `approvalStatus` on enrollment request

### Scholarship Engine
- `scholarship` admission type flag signals scholarship eligibility
- Referral source and government scheme fields store supporting data
- **Interface**: `admissionType === 'scholarship'`, `governmentScheme` field

### Government Training Programs
- `government` admission type and `governmentScheme` field support PM-KMY/PMFBY/NABARD/State Agri programs
- Enrollment source tracks program attribution
- **Interface**: `admissionType === 'government'`, `governmentScheme` field

### Corporate HRMS
- `corporate` admission type and `corporateProgram` field support employer-sponsored training
- Batch slot `corporate` segregates corporate cohorts
- **Interface**: `admissionType === 'corporate'`, `corporateProgram` field, `BatchSlot.corporate`

### CRM Platform
- Enrollment status transitions create customer lifecycle events
- Student name, contact, course, and batch data populate CRM records
- **Interface**: `EnrollmentTimelineEvent` provides full activity history

### Communication Platform
- Approval workflow actions trigger notification events
- Status changes at each pipeline stage generate message queues
- **Interface**: `enrollmentStatus` transitions map to notification templates

### Analytics Platform
- `EnrollmentDashboardStats` provides executive KPIs
- `CapacityInfo` feeds utilization analytics
- `AdmissionPipeline` stage tracking enables funnel analysis
- **Interface**: All state data is available as analytics events

### AI Recommendation Engine
- Student profile + course selection + admission type + enrollment history provides recommendation features
- Learning preferences and interests guide course suggestions
- **Interface**: `courseId`, `admissionType`, enrollment history on each request

### AI Admission Assistant
- Application attributes (priority, source, referral) enable automated routing suggestions
- Approval history patterns inform decision recommendations
- **Interface**: `priority`, `enrollmentSource`, `referralSource` fields

### Placement Platform
- Enrolled students with completed training are placement-ready
- Course and batch data determine placement eligibility
- **Interface**: `enrollmentStatus === 'enrolled'` as placement gate

### Third-party Student Verification
- Document verification status flags from Student Profile integration
- Government ID and scheme fields support verification workflows
- **Interface**: `governmentId` on profile for Aadhaar/PAN verification

## Capacity Integration

| Integration | Data | Trigger |
|------------|------|---------|
| Auto-scaling | `CapacityInfo.utilizationPercent` | When utilization > 90% |
| Overflow batch | `Batch.status === 'full'` | When batch capacity exhausted |
| Waitlist | `EnrollmentStatus.waitlisted` | When no batch has available seats |

## Enrollment Status Transition Contract

```
draft → submitted → under-review → pending-approval → approved → seat-reserved → batch-assigned → enrolled
  ↓         ↓            ↓               ↓               ↓           ↓               ↓
cancelled  rejected     rejected       rejected        cancelled   cancelled       cancelled
                                                                                      ↓
                                                                                  archived
```

Each transition publishes an `EnrollmentTimelineEvent` consumable by any integrated platform.
