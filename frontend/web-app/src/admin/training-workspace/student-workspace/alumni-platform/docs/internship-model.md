# Internship Domain Model

## Internship Statuses
`applied` | `shortlisted` | `interview-scheduled` | `interview-completed` | `offer-received` | `offer-accepted` | `ongoing` | `completed` | `terminated` | `converted-to-placement` | `withdrawn`

## Types
`internship` | `apprenticeship` | `fellowship` (subset of JobOpportunityType)

## Key Interfaces
- **JobOpportunity** — Job listing with type, company, required skills, eligibility criteria
- **InternshipCenter** — Filtered view of JobOpportunities matching internship/apprenticeship/fellowship types

## Pages
- InternshipCenter — Tabbed view (All / Internships / Apprenticeships / Fellowships) with search and pagination
