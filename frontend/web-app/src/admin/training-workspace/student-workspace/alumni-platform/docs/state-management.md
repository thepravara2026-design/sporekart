# State Management — AlumniContext

## Provider
`AlumniProvider` wraps the entire alumni platform (in `AlumniIndex.tsx`).

## State Shape
```
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
filters: AlumniFilters
page: number
pageSize: number
```

## Filters
```
search: string
status: PlacementStatus | 'all'
type: JobOpportunityType | 'all'
company: string
tier: CompanyPartnershipTier | 'all'
engagement: AlumniEngagementLevel | 'all'
driveStage: PlacementDriveStage | 'all'
dateFrom: string
dateTo: string
sortKey: 'name' | 'date' | 'status' | 'company' | 'tier' | 'engagement' | 'score' | 'salary' | 'skill'
sortDirection: 'asc' | 'desc'
```

## Actions
- **Setters**: setSearch, setStatusFilter, setTypeFilter, setCompanyFilter, setTierFilter, setEngagementFilter, setDriveStageFilter, setDateFrom, setDateTo, setSort, setPage, setPageSize
- **Filtered Getters**: getFilteredProfiles, getFilteredJobs, getFilteredDrives, getFilteredCompanies, getFilteredAlumni, getFilteredEvents, getFilteredContributions, getFilteredMentorships
