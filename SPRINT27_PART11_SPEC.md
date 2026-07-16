# ==========================================================================================================
#
#                                   SPOREKART ENTERPRISE PLATFORM
#
#                                           PHASE 12
#
#                     ENTERPRISE STUDENT MANAGEMENT PLATFORM (ESMP)
#
#                                           SPRINT 27
#
#                                             PART 11
#
#       ENTERPRISE PLACEMENT, INTERNSHIP, CAREER DEVELOPMENT & ALUMNI PLATFORM (EPICDAP)
#
# ==========================================================================================================

ROLE

You are the Enterprise Career & Alumni Engineering Team responsible for designing,
architecting and implementing the complete Placement, Internship, Career Development
and Alumni Management Platform for the SporeKart Enterprise Learning Management System.

Act as

• Google Staff Software Engineer (Google Careers Platform Architect)
• LinkedIn Principal Engineer (Talent Solutions Architect)
• Amazon Principal Software Engineer (Amazon University Talent Acquisition)
• Microsoft Distinguished Engineer (Microsoft Alumni & Career Services)
• SAP SuccessFactors Architect
• Oracle HCM Cloud Architect
• Workday Student Architect
• Principal Enterprise Architect
• Principal Product Architect
• Principal UX Architect
• Principal Accessibility Engineer
• Principal Performance Engineer
• Principal Security Engineer
• Principal QA Automation Engineer

This implementation extends

✓ Sprint 27 Parts 1–10
✓ Entire Phase 11 Foundation

Everything must integrate seamlessly into the certified Enterprise LMS architecture.

==========================================================================================================
MISSION
==========================================================================================================

Build the Enterprise Placement, Internship, Career Development & Alumni Platform.

This is NOT a job board.

This sprint establishes the centralized career services platform responsible for

Placement Management
Internship Management
Career Development
Alumni Network Management
Job Opportunity Repository
Placement Drives
Company Partnerships
Student Career Profiles
Resume Management
Interview Preparation
Career Counseling
Skill Gap Analysis
Placement Analytics
Alumni Directory
Alumni Engagement
Alumni Mentorship
Alumni Contributions
Alumni Events
Alumni Giving
Alumni Career Services
Future AI Career Recommendations
Future ERP Integration

Everything operates entirely in Mock Mode.

NO Backend.
NO APIs.
NO Database.
NO Real Job Listings.
NO Real Company Data.
NO AI Engine.
NO Resume Parsing.
NO Email Notifications.
No External Integrations.

Architecture only.

==========================================================================================================
DO NOT MODIFY
==========================================================================================================

Authentication
RBAC
Commerce Platform
Inventory
Warehouse
Orders
Training Workspace
Course Registry
Curriculum
Learning Resources
Enrollment
Attendance
Assignments
Assessments
Learning Progress
Certificate Platform
Analytics Platform
Communication Platform
Enterprise Design System
Search Framework
Filter Framework
Pagination Framework
Student Workspace Core (types.ts, StudentWorkspaceContext)

==========================================================================================================
PRIMARY OBJECTIVES
==========================================================================================================

Develop reusable enterprise architecture supporting

Enterprise Placement Hub
Internship Management Center
Career Development Center
Alumni Network
Job & Opportunity Repository
Company Partnership Management
Placement Drive Lifecycle
Student Career Profile
Resume Portfolio
Interview Preparation Tools
Career Counseling Sessions
Skill Gap Assessment
Placement Analytics Dashboard
Alumni Directory & Mapping
Alumni Engagement Tracking
Alumni Mentorship Program
Alumni Contributions & Giving
Alumni Events Management
Future AI Career Counseling
Future ERP/HRMS Integration

==========================================================================================================
DOMAIN MODEL
==========================================================================================================

Placement & Internship Types

on-campus | off-campus | virtual | international | corporate-tieup | government-scheme | entrepreneurial | freelance

Placement Status

placement-ready | resume-shortlisted | interview-scheduled | interview-completed | selected | offer-received | offer-accepted | joined | rejected | not-placed | withdrawn | deregistered

Internship Status

applied | shortlisted | interview-scheduled | interview-completed | offer-received | offer-accepted | ongoing | completed | terminated | converted-to-placement | withdrawn

Career Development Stage

exploration | skill-building | resume-preparation | interview-preparation | job-search | placement | post-placement | alumni | mentorship | lifelong-learning

Company Partnership Tier

platinum | gold | silver | bronze | strategic | academic | government

Alumni Engagement Level

active | moderate | low | disengaged | lifetime | ambassador | mentor | donor

Job Opportunity Type

full-time | part-time | internship | contract | freelance | apprenticeship | fellowship | government-job

Placement Drive Stage

announced | registrations-open | registrations-closed | scheduled | in-progress | results-pending | completed | cancelled

Alumni Contribution Type

monetary-donation | equipment-donation | scholarship-fund | mentorship-hours | guest-lecture | industry-project | curriculum-advisory | placement-support | event-participation | media-testimonial

Alumni Event Type

networking | workshop | webinar | reunion | mentorship-session | industry-visit | hackathon | career-fair | cultural | sports | annual-meet | chapter-meet

Skill Category

technical | domain | soft-skill | leadership | language | certification | tool | methodology

==========================================================================================================
CORE INTERFACES
==========================================================================================================

StudentCareerProfile
- id, studentId, studentName, studentPhoto, email, phone, dateOfBirth
- courseId, courseName, batchId, batchName
- overallScore, technicalScore, domainScore, softSkillScore
- resumeUrl (placeholder), portfolioUrl (placeholder), linkedInUrl (placeholder), githubUrl (placeholder)
- careerObjective, targetRole, targetIndustry, preferredLocation
- totalExperience (placeholder), noticePeriod (placeholder), currentCtc (placeholder), expectedCtc (placeholder)
- workHistory (placeholder array), educationHistory, certifications, projects, achievements
- skills: { category: SkillCategory; name: string; proficiency: 'beginner' | 'intermediate' | 'advanced' | 'expert' }[]
- placementStatus: PlacementStatus
- careerStage: CareerDevelopmentStage
- interviewCount, offerCount, placementDate (nullable)
- createdDate, lastUpdated

JobOpportunity
- id, jobId, jobType: JobOpportunityType
- companyId, companyName, companyLogo (placeholder)
- title, description, requiredSkills, preferredSkills
- location, salaryRange, experienceRequired
- totalPositions, filledPositions
- postedDate, applicationDeadline, startDate
- status: 'active' | 'closed' | 'on-hold' | 'cancelled' | 'filled'
- placementDriveId (nullable), partnerTier: CompanyPartnershipTier
- eligibilityCriteria (courseIds, minScore, minAttendance)

PlacementDrive
- id, driveId, name, description
- stage: PlacementDriveStage
- companyId, companyName, companyLogo
- jobOpportunities: JobOpportunity[]
- registrationsOpen, registrationsClose
- driveDate, venue (placeholder), mode: 'on-campus' | 'off-campus' | 'virtual'
- registeredStudents, shortlistedStudents, selectedStudents
- totalRounds, currentRound
- createdDate

CompanyPartnership
- id, companyId, companyName, companyLogo, website (placeholder), industry
- tier: CompanyPartnershipTier
- partnershipDate, lastActivityDate
- totalHires, totalInternshipsOffered, totalPlacementDrives
- contactPerson (placeholder), contactEmail (placeholder), contactPhone (placeholder)
- status: 'active' | 'inactive' | 'suspended'
- notes (placeholder)

CareerCounselingSession
- id, sessionId, studentId, studentName
- counselorName (placeholder), sessionDate, sessionType: 'one-on-one' | 'group' | 'workshop' | 'webinar'
- topic, notes (placeholder), actionItems (placeholder)
- status: 'scheduled' | 'completed' | 'cancelled' | 'no-show'
- feedback (placeholder), rating (placeholder)
- createdDate

InterviewPreparation
- id, prepId, studentId, studentName
- companyName, jobTitle, roundNumber, roundType: 'aptitude' | 'technical' | 'hr' | 'managerial' | 'group-discussion' | 'presentation'
- scheduledDate, status: 'upcoming' | 'completed' | 'cancelled' | 'rescheduled'
- preparationNotes (placeholder), feedback (placeholder), result: 'cleared' | 'failed' | 'awaiting' (nullable)
- createdDate

SkillGapAssessment
- id, assessmentId, studentId, studentName
- category: SkillCategory, skillName
- currentLevel: number (0-100), requiredLevel: number (0-100)
- gap: number, priority: 'critical' | 'high' | 'medium' | 'low'
- suggestedResources (placeholder), targetDate (nullable)
- status: 'identified' | 'in-progress' | 'addressed'

AlumniProfile
- id, alumniId, studentId (nullable)
- fullName, photo, email, phone
- batchId, batchName, courseId, courseName
- graduationYear, completionDate
- currentCompany, currentPosition, industry
- location, linkedInUrl (placeholder)
- engagementLevel: AlumniEngagementLevel
- mentorshipStatus: 'available' | 'active' | 'unavailable'
- totalMentorshipHours, contributionsCount
- isAmbassador, isDonor
- bio (placeholder), achievements (placeholder)
- createdDate, lastActiveDate

AlumniMentorship
- id, mentorshipId, mentorAlumniId, mentorName
- menteeStudentId, menteeStudentName
- startDate, endDate (nullable)
- focusArea: 'career-guidance' | 'technical' | 'domain' | 'interview-prep' | 'entrepreneurship' | 'leadership'
- status: 'active' | 'completed' | 'paused' | 'cancelled'
- sessionsCompleted, totalHours
- feedback (placeholder), rating (placeholder)

AlumniContribution
- id, contributionId, alumniId, alumniName
- type: AlumniContributionType
- description, date, value (placeholder)
- associatedEventId (nullable), associatedProgram (nullable)
- status: 'acknowledged' | 'pending-acknowledgment' | 'featured'
- createdDate

AlumniEvent
- id, eventId, eventType: AlumniEventType
- title, description, date, time, venue (placeholder), mode: 'online' | 'offline' | 'hybrid'
- organizer (placeholder), maxAttendees, registeredCount, attendedCount
- registrationDeadline, fee (placeholder)
- status: 'draft' | 'announced' | 'open' | 'closed' | 'in-progress' | 'completed' | 'cancelled'
- createdBy, createdDate

AlumniAnalytics
- totalAlumni, activeAlumni, engagedAlumni
- totalMentors, activeMentorships, completedMentorships
- totalContributions, totalDonations (placeholder)
- totalEvents, totalEventRegistrations
- placementRate, averageSalary (placeholder)
- topRecruiters: { companyName, hires }[]
- alumniByBatch: { batchName, count }[]
- alumniByIndustry: { industry, count }[]
- alumniByLocation: { location, count }[]
- engagementTrends: { month, activeCount, contributionCount }[]
- placementTrends: { month, placedCount }[]

PlacementDashboard
- totalStudents, placementReady, shortlisted, placed, notPlaced
- activeDrives, activeJobOpenings, partnerCompanies
- placementPercentage, averagePackage (placeholder), highestPackage (placeholder)
- upcomingDrives: PlacementDrive[]
- recentPlacements: StudentCareerProfile[]
- topRecruiters: CompanyPartnership[]

==========================================================================================================
MESSAGE STATUSES
==========================================================================================================

For Placement Drive: announced | registrations-open | registrations-closed | scheduled | in-progress | results-pending | completed | cancelled
For Job Opportunity: active | closed | on-hold | cancelled | filled
For Counseling Session: scheduled | completed | cancelled | no-show
For Interview Prep: upcoming | completed | cancelled | rescheduled
For Mentorship: active | completed | paused | cancelled
For Alumni Event: draft | announced | open | closed | in-progress | completed | cancelled
For Contribution: acknowledged | pending-acknowledgment | featured

==========================================================================================================
NAVIGATION ITEMS
==========================================================================================================

alumni — Alumni Dashboard (default)
alumni/placement — Placement Hub
alumni/internships — Internship Center
alumni/career — Career Development Center
alumni/companies — Company Partnerships
alumni/jobs — Job Opportunities
alumni/directory — Alumni Directory
alumni/mentorship — Alumni Mentorship
alumni/events — Alumni Events
alumni/contributions — Alumni Contributions
alumni/analytics — Placement & Alumni Analytics

Route base: /admin/training/student-workspace/alumni

Move from STUDENT_FUTURE_ITEMS to STUDENT_NAV_ITEMS with label 'Alumni', icon 'graduation-cap',
description 'Placement, internships, career development & alumni network'.

==========================================================================================================
PAGES
==========================================================================================================

1. AlumniIndex — Wrapper with sub-navigation tabs + provider
2. AlumniDashboard — Executive KPIs: total alumni, placement rate, active drives, partners, mentors
3. PlacementHub — Placement drives lifecycle view: announced → registrations → in-progress → completed
4. InternshipCenter — Internship opportunities and student applications
5. CareerDevelopmentCenter — Student career profiles, resume prep, interview prep, counseling, skill gaps
6. CompanyPartnerships — Partner company registry with tier and hiring stats
7. JobOpportunities — Active job listings with filters (type, company, location, skills)
8. AlumniDirectory — Searchable alumni directory with engagement filters
9. AlumniMentorship — Mentor-mentee matching and session tracking
10. AlumniEvents — Event lifecycle management
11. AlumniContributions — Contribution tracking and recognition
12. PlacementAnalytics — Placement trends, company stats, alumni engagement metrics

==========================================================================================================
COMPONENTS
==========================================================================================================

Minimum 20 reusable components:

1. DashboardWidget — Reusable widget wrapper (reuse from communication or create new)
2. MetricCard — Single stat display card
3. StatusBadge — Placement/Internship/Event status chip
4. TierBadge — Company partnership tier badge (platinum/gold/silver/bronze)
5. EngagementBadge — Alumni engagement level badge
6. AlumniProfileCard — Alumni directory card with photo, batch, company, engagement
7. StudentCareerCard — Career profile card with skills, status, offers
8. PlacementDriveCard — Placement drive lifecycle card with stages
9. JobCard — Job opportunity card with company, salary, skills
10. CompanyCard — Company partnership card with tier, hires, drives
11. CounselingSessionCard — Counseling session card
12. InterviewPrepCard — Interview preparation round card
13. SkillGapCard — Skill gap assessment card with progress
14. MentorshipCard — Mentorship pairing card
15. ContributionCard — Alumni contribution record card
16. EventCard — Alumni event card
17. EmptyStates — 6 typed empty states (noPlacements, noInternships, noJobs, noAlumni, noEvents, noSearchResults)
18. Skeletons — Dashboard, list, grid skeleton loaders
19. SharedFilters — Search bar + status/type/company/tier/engagement dropdowns + date range + sort
20. ProgressRing — Circular progress for skill gap, placement rate (reuse or create)

==========================================================================================================
STATE MANAGEMENT
==========================================================================================================

Create AlumniContext with the following state shape:

{
  studentProfiles: StudentCareerProfile[]
  jobOpportunities: JobOpportunity[]
  placementDrives: PlacementDrive[]
  companies: CompanyPartnership[]
  counselingSessions: CareerCounselingSession[]
  interviewPrep: InterviewPreparation[]
  skillGaps: SkillGapAssessment[]
  alumni: AlumniProfile[]
  mentorships: AlumniMentorship[]
  contributions: AlumniContribution[]
  events: AlumniEvent[]
  analytics: AlumniAnalytics
  dashboard: PlacementDashboard
  filters: { search, status, type, company, tier, engagement, dateFrom, dateTo, sortKey, sortDirection }
  page: number
  pageSize: number
}

Actions:
- setSearch, setStatusFilter, setTypeFilter, setCompanyFilter, setTierFilter, setEngagementFilter, setDateFrom, setDateTo, setSort
- setPage, setPageSize
- getFiltered(Students | Jobs | Drives | Companies | Alumni | Events | Contributions)
- Add/update operations on preferences, mentorship, contributions (local mock state only)

==========================================================================================================
MOCK DATA
==========================================================================================================

Generate 10 deterministic mock data generators:

1. getStudentCareerProfiles() — 25 students with career profiles, skills, placement status
2. getJobOpportunities() — 20 jobs across 8+ partner companies
3. getPlacementDrives() — 5 placement drives at various stages
4. getCompanyPartnerships() — 8 partner companies across all tiers
5. getCounselingSessions() — 15 career counseling sessions
6. getInterviewPreparations() — 20 interview preparation records
7. getSkillGapAssessments() — 30 skill gap assessments across categories
8. getAlumniProfiles() — 20 alumni with engagement levels
9. getAlumniMentorships() — 10 active/completed mentor pairs
10. getAlumniContributions() — 15 contribution records
11. getAlumniEvents() — 8 alumni events
12. generateAnalytics() — Computed from profiles, placements, alumni
13. generateDashboard() — Computed from all sources

==========================================================================================================
SEARCH, FILTERS, SORT, PAGINATION
==========================================================================================================

Search: Reuse enterprise search pattern — search by student name, company name, job title, event title, alumni name
Filters: Status, Type, Company, Tier, Engagement Level, Date Range
Sort: Newest, Oldest, Priority/Tier, Alphabetical, Placement Rate, Engagement Level
Pagination: Reuse enterprise Pagination component with page size options [10, 20, 50, 100]

==========================================================================================================
EMPTY STATES
==========================================================================================================

noPlacements | noInternships | noJobs | noAlumni | noEvents | noSearchResults

==========================================================================================================
LOADING STATES
==========================================================================================================

Use enterprise skeleton loaders. NEVER display "Loading..." text.

==========================================================================================================
ERROR STATES
==========================================================================================================

Profile Missing | Placement Unavailable | Directory Error | Permission Denied | Unexpected Error

==========================================================================================================
RESPONSIVE DESIGN
==========================================================================================================

Desktop | Laptop | Tablet | Mobile (320px)
All dashboards, directory, and career tools must remain fully usable.

==========================================================================================================
ACCESSIBILITY
==========================================================================================================

WCAG 2.2 AA: Keyboard navigation, ARIA labels, accessible forms, screen reader support, reduced motion, high contrast.

==========================================================================================================
PERFORMANCE
==========================================================================================================

Memoization, lazy loading, virtualization preparation, code splitting. Future million-alumni readiness.

==========================================================================================================
DESIGN SYSTEM
==========================================================================================================

Reuse existing cards, badges, tables, buttons, skeletons, typography, icons. No duplicate UI.

==========================================================================================================
ROUTING
==========================================================================================================

Wire in App.tsx under student-workspace/*:

<Route path="alumni" element={<StudentAlumniPage />} />
<Route path="alumni/*" element={<StudentAlumniPage />} />

Lazy import: const StudentAlumniPage = lazy(() => import('./...alumni-platform/pages/AlumniIndex'));

==========================================================================================================
DOCUMENTATION
==========================================================================================================

Generate 16 documentation files:

1. architecture.md
2. placement-model.md
3. internship-model.md
4. career-development-model.md
5. alumni-model.md
6. company-partnership-model.md
7. analytics-architecture.md
8. folder-structure.md
9. component-inventory.md
10. state-management.md
11. responsive.md
12. accessibility.md
13. performance.md
14. future-integration-readiness.md (ERP/HRMS/LinkedIn/AI/Job Portals)
15. developer-guide.md
16. sprint27-part11-completion.md

==========================================================================================================
ENGINEERING STANDARDS
==========================================================================================================

SOLID | DRY | KISS | Feature-first Architecture | Domain Driven Design
Atomic Design | Composition over Inheritance | Strict Typing | Reusable Components
Dependency Inversion | Zero Dead Code | Zero Duplicate Components
Zero Inline Business Logic | Zero Hardcoded Mock Data outside Mock Layer

==========================================================================================================
QUALITY GATE
==========================================================================================================

Sprint 27 Part 11 is NOT complete until

✓ Enterprise Placement Platform implemented
✓ Internship Management completed
✓ Career Development Center completed
✓ Alumni Network completed
✓ Placement Hub completed
✓ Job Opportunity Repository completed
✓ Company Partnership Management completed
✓ Career Counseling completed
✓ Interview Preparation completed
✓ Skill Gap Assessment completed
✓ Alumni Directory completed
✓ Alumni Mentorship completed
✓ Alumni Contributions completed
✓ Alumni Events completed
✓ Placement Analytics completed
✓ Alumni Analytics completed
✓ Search reused
✓ Filters reused
✓ Pagination reused
✓ Sort controls implemented
✓ Date range filter implemented
✓ Empty states completed
✓ Skeleton loading implemented
✓ Responsive validation passed
✓ Accessibility validation passed
✓ Performance optimized
✓ 16 documentation files generated
✓ TypeScript typecheck passes with ZERO new errors
✓ Zero duplicate components
✓ Zero regressions to Sprint 27 Parts 1–10
✓ Zero regressions to Phase 11
✓ Customer Platform unaffected
✓ Commerce Platform unaffected
✓ Inventory Platform unaffected
✓ Warehouse Platform unaffected
✓ LMS Foundation unaffected
✓ All previous platforms unaffected

==========================================================================================================
OUTPUT
==========================================================================================================

Generate:

1. Domain types (types.ts)
2. Mock data generators (data/mockData.ts) — 10+ generators
3. State management (state/AlumniContext.tsx)
4. 20+ reusable components
5. 9+ pages (AlumniIndex + all dashboards/centers)
6. Routes in App.tsx + navigation.ts
7. 16 documentation files
8. Typecheck verification

==========================================================================================================
EXISTING INTEGRATION POINTS
==========================================================================================================

The following already reference alumni/placement and must be considered:

- StudentWorkspace/types.ts: 'alumni' is a valid StudentStatus
- Profile/types.ts: 'placement' and 'alumni' are valid TimelineEvent types
- ProfileTimeline.tsx: placement icon '💼', alumni icon '🎓'
- Profile mockData: placement timeline event exists
- LearningProgress/types.ts: 'alumni' is a final LearningStage
- CommunicationPlatform/types.ts: 'alumni' is a TimelineStage
- CertificatePlatform/types.ts: 'internship' is a CertificateType
- StudentWorkspace types.ts: StudentDashboardStats has futureAlumniCount
- Navigation: alumni is in STUDENT_FUTURE_ITEMS (must be moved to STUDENT_NAV_ITEMS)
- Enrollment future-integration: placement platform integration point documented

DO NOT modify any of these existing files. The new Alumni Platform must integrate with them via the established patterns.

==========================================================================================================
FINAL ENGINEERING NOTE
==========================================================================================================

This sprint establishes the Enterprise Placement, Internship, Career Development &
Alumni Platform as the centralized career services layer of the SporeKart Enterprise
Learning Management System.

The platform becomes the authoritative domain for all placement drives, internship
management, career development services, alumni networking, mentorship programs,
and employer partnerships. Every student career journey from enrollment through
placement to alumni engagement must originate from this centralized service.

The architecture must remain modular, scalable to millions of alumni, provider-
independent, enterprise-grade, and fully prepared for future integration with
LinkedIn API, job portals (Naukri, Indeed, Monster), ERP systems (SAP, Oracle),
HRMS platforms, AI career counseling engines, government skill databases
(NSDC, Skill India), and enterprise alumni engagement platforms without requiring
structural changes to the certified platform.

==========================================================================================================
