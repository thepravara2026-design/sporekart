# Component Inventory

## Badges
| Component | Props | Purpose |
|-----------|-------|---------|
| StatusBadge | `status: string` | Colored pill for placement/internship/event status |
| TierBadge | `tier: string` | Colored pill for company partnership tier |
| EngagementBadge | `level: string` | Colored pill for alumni engagement level |

## Cards
| Component | Props | Purpose |
|-----------|-------|---------|
| AlumniProfileCard | `alumni: AlumniProfile` | Alumni directory card with photo, batch, company, badges |
| StudentCareerCard | `profile: StudentCareerProfile` | Career profile card with status, skills, scores |
| PlacementDriveCard | `drive: PlacementDrive` | Placement drive lifecycle card with stage, counts, rounds |
| JobCard | `job: JobOpportunity` | Job listing card with company, salary, skills, tier |
| CompanyCard | `company: CompanyPartnership` | Partner company card with tier, hires, drives |
| CounselingSessionCard | `session: CareerCounselingSession` | Session card with student, counselor, status, rating |
| InterviewPrepCard | `prep: InterviewPreparation` | Interview round card with company, type, result |
| SkillGapCard | `gap: SkillGapAssessment` | Skill gap card with progress bar, current/required levels |
| MentorshipCard | `mentorship: AlumniMentorship` | Mentorship pair card with focus area, sessions, rating |
| ContributionCard | `contribution: AlumniContribution` | Contribution record card with type, value, status |
| EventCard | `event: AlumniEvent` | Event card with type, date, venue, registration stats |

## Layout
| Component | Props | Purpose |
|-----------|-------|---------|
| DashboardWidget | `title, subtitle, children, actions` | Widget wrapper with header |
| MetricCard | `label, value, color, icon, subtitle` | Single stat display card |
| ProgressRing | `value, size, strokeWidth, color` | Circular progress indicator |

## Utility
| Component | Props | Purpose |
|-----------|-------|---------|
| SharedFilters | `currentPage?, showSort?` | Search bar + contextual filter dropdowns + date range + sort |
| EmptyState | `type: EmptyStateType, onClearFilters?` | 6 typed empty states |
| DashboardSkeleton | — | Dashboard loading skeleton |
| ListSkeleton | `rows?` | List loading skeleton |
